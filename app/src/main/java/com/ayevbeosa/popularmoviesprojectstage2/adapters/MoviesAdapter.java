package com.ayevbeosa.popularmoviesprojectstage2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayevbeosa.popularmoviesprojectstage2.R;
import com.ayevbeosa.popularmoviesprojectstage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.IMAGE_BASE_URL;
import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.IMAGE_SIZE;

/**
 * This class binds view data to the {@link RecyclerView}
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movieList;
    private Context mContext;

    private final ListItemClickListener mOnClickItemListener;

    public MoviesAdapter(Context context, ListItemClickListener listener) {
        this.mContext = context;
        this.mOnClickItemListener = listener;
        movieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        String originalTitle = movie.getOriginal_title();
        String posterPath = movie.getPoster_path();
        holder.bind(originalTitle, posterPath);
    }

    @Override
    public int getItemCount() {
        if (movieList == null)
            return 0;
        else
            return movieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        if (this.movieList != null) {
            this.movieList.addAll(movieList);
            notifyDataSetChanged();
        } else {
            this.movieList = movieList;
            notifyDataSetChanged();
        }
    }

    public void emptyMovieList() {
        movieList.clear();
        notifyDataSetChanged();
    }

    private List<Movie> getMovieList() {
        return movieList;
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_movie_poster_iv)
        ImageView moviePoster;
        @BindView(R.id.item_original_title_tv)
        TextView originalTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void bind(String title, String posterPath) {
            originalTitle.setText(title);
            String imagePath = IMAGE_BASE_URL + IMAGE_SIZE + posterPath;
            Picasso.with(mContext)
                    .load(imagePath)
                    .error(R.drawable.default_image_view)
                    .into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickItemListener.onListItemClick(getMovieList().get(clickedPosition));
        }
    }
}
