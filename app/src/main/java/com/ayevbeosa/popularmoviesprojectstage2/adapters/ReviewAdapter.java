package com.ayevbeosa.popularmoviesprojectstage2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayevbeosa.popularmoviesprojectstage2.R;
import com.ayevbeosa.popularmoviesprojectstage2.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;
    private Context mContext;

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        String author = review.getAuthor();
        String content = review.getContent();
        holder.bind(author, content);
    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        } else {
            return 0;
        }
    }

    public void setReviews(List<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.addAll(reviews);
            notifyDataSetChanged();
        } else {
            this.reviews = reviews;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_tv)
        TextView reviewAuthor;
        @BindView(R.id.content_tv)
        TextView reviewContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String author, String content) {
            reviewAuthor.setText(author);
            reviewContent.setText(content);
        }
    }
}
