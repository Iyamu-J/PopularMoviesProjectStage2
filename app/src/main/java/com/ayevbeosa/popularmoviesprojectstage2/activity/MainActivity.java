package com.ayevbeosa.popularmoviesprojectstage2.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayevbeosa.popularmoviesprojectstage2.R;
import com.ayevbeosa.popularmoviesprojectstage2.adapters.MoviesAdapter;
import com.ayevbeosa.popularmoviesprojectstage2.model.Movie;
import com.ayevbeosa.popularmoviesprojectstage2.model.MovieResponse;
import com.ayevbeosa.popularmoviesprojectstage2.rest.ApiClient;
import com.ayevbeosa.popularmoviesprojectstage2.rest.WebService;
import com.ayevbeosa.popularmoviesprojectstage2.viewmodel.MovieViewModel;
import com.ayevbeosa.popularmoviesprojectstage2.viewmodel.MovieViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.api_key;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private MoviesAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private WebService webService;
    private boolean isPopular = true;
    private int currentPage = 1;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            isPopular = savedInstanceState.getBoolean(getString(R.string.is_popular_state_key));
        }

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        webService = ApiClient.getRetrofit().create(WebService.class);
        loadProgressBar(true);
        setupViewModels();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.emptyMovieList();
                currentPage = 1;
                setupViewModels();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        recyclerViewState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(getString(R.string.recycler_view_state_key), recyclerViewState);
        outState.putBoolean(getString(R.string.is_popular_state_key), isPopular);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recyclerViewState = savedInstanceState.getParcelable(getString(R.string.recycler_view_state_key));
        isPopular = savedInstanceState.getBoolean(getString(R.string.is_popular_state_key));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerViewState != null) {
            mLayoutManager.onRestoreInstanceState(recyclerViewState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular_action:
                loadPopularMovies();
                isPopular = true;
                return true;
            case R.id.sort_by_rated_action:
                loadTopRatedMovies();
                isPopular = false;
                return true;
            case R.id.view_favourites:
                Intent intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(Movie movie) {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, DetailsActivity.class);
        extras.putParcelable(getString(R.string.extra_movie), movie);
        extras.putInt(getString(R.string.movie_extra_id), movie.getId());
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void populateUI() {
        mAdapter = new MoviesAdapter(this, this);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = true;
            int firstVisibleItem, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            loading = false;
                            currentPage += 1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreMovies(currentPage);
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });
    }

    private void setupViewModels() {
        loadProgressBar(true);
        if (!isPopular) {
            loadTopRatedMovies();
        } else {
            loadPopularMovies();
        }
    }

    private void loadPopularMovies() {
        MovieViewModelFactory factory = new MovieViewModelFactory(currentPage);
        MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        viewModel.getPopularMovieResponse().observe(this, new Observer<Response<MovieResponse>>() {
            @Override
            public void onChanged(@Nullable Response<MovieResponse> movieResponse) {
                swipeRefreshLayout.setRefreshing(false);
                loadProgressBar(false);
                if (movieResponse != null) {
                    if (movieResponse.isSuccessful()) {
                        List<Movie> movies = getMovieResponse(movieResponse);
                        if (movies != null) {
                            populateUI();
                            mAdapter.setMovieList(movies);
                            if (recyclerViewState != null) {
                                mLayoutManager.onRestoreInstanceState(recyclerViewState);
                            }
                        }
                    } else {
                        switch (movieResponse.code()) {
                            case 401:
                                makeToast(getString(R.string.error_401));
                                break;
                            case 404:
                                makeToast(getString(R.string.error_404));
                                break;
                        }
                    }
                } else {
                    makeToast(getString(R.string.connection_error_message));
                }
            }
        });
    }

    private void loadTopRatedMovies() {
        MovieViewModelFactory factory = new MovieViewModelFactory(currentPage);
        MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        viewModel.getTopRatedMoviesResponse().observe(this, new Observer<Response<MovieResponse>>() {
            @Override
            public void onChanged(@Nullable Response<MovieResponse> movieResponse) {
                swipeRefreshLayout.setRefreshing(false);
                loadProgressBar(false);
                if (movieResponse != null) {
                    if (movieResponse.isSuccessful()) {
                        List<Movie> movies = getMovieResponse(movieResponse);
                        if (movies != null) {
                            populateUI();
                            mAdapter.setMovieList(movies);
                            if (recyclerViewState != null) {
                                mLayoutManager.onRestoreInstanceState(recyclerViewState);
                            }
                        }
                    } else {
                        switch (movieResponse.code()) {
                            case 401:
                                makeToast(getString(R.string.error_401));
                                break;
                            case 404:
                                makeToast(getString(R.string.error_404));
                                break;
                            default:
                                makeToast(getString(R.string.connection_error_message));
                        }
                    }
                } else {
                    makeToast(getString(R.string.connection_error_message));
                }
            }
        });
    }

    private List<Movie> getMovieResponse(Response<MovieResponse> response) {
        MovieResponse movieResponse = response.body();
        return movieResponse != null ? movieResponse.getMovieList() : null;
    }

    public void loadMoreMovies(int currentPage) {
        loadProgressBar(true);
        Call<MovieResponse> call;
        if (!isPopular) {
            call = webService.getTopRatedMovies(currentPage, api_key);
        } else {
            call = webService.getPopularMovies(currentPage, api_key);
        }
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                loadProgressBar(false);
                swipeRefreshLayout.setRefreshing(false);
                List<Movie> movies = getMovieResponse(response);
                if (movies != null) {
                    mAdapter.setMovieList(movies);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadProgressBar(false);
                swipeRefreshLayout.setRefreshing(false);
                makeToast(getString(R.string.connection_error_message));
            }
        });
    }

    private void loadProgressBar(boolean load) {
        if (load) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
