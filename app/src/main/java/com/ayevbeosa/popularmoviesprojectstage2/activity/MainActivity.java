package com.ayevbeosa.popularmoviesprojectstage2.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        loadProgressBar(true);
        setupViewModels();

        webService = ApiClient.getRetrofit().create(WebService.class);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular_action:
                loadPopularMovies();
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
                    List<Movie> movies = getMovieResponse(movieResponse);
                    populateUI();
                    mAdapter.setMovieList(movies);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_SHORT)
                            .show();
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
                    List<Movie> movies = getMovieResponse(movieResponse);
                    populateUI();
                    mAdapter.setMovieList(movies);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_SHORT)
                            .show();
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
                List<Movie> movies = getMovieResponse(response);
                if (movies != null) {
                    loadProgressBar(false);
                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter.setMovieList(movies);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadProgressBar(false);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_SHORT)
                        .show();
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
}
