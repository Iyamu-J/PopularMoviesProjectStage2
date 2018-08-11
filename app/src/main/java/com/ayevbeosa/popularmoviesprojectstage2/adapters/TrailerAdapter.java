package com.ayevbeosa.popularmoviesprojectstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayevbeosa.popularmoviesprojectstage2.R;
import com.ayevbeosa.popularmoviesprojectstage2.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.THUMBNAIL_QUALITY;
import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.YOUTUBE_THUMBNAIL_URL;
import static com.ayevbeosa.popularmoviesprojectstage2.utils.AppConstants.YOUTUBE_URL;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> trailerList;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        String name = trailer.getName();
        String key = trailer.getKey();
        holder.bind(name, key);
    }

    @Override
    public int getItemCount() {
        if (trailerList != null) {
            return trailerList.size();
        } else {
            return 0;
        }
    }

    public void setTrailerList(List<Trailer> trailerList) {
        if (this.trailerList != null) {
            this.trailerList.addAll(trailerList);
            notifyDataSetChanged();
        } else {
            this.trailerList = trailerList;
            notifyDataSetChanged();
        }
    }

    private void shareVideo(String key) {
        String link = YOUTUBE_URL + key;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this trailer " + link);
        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
    }

    private void viewVideo(String key) {
        String link = YOUTUBE_URL + key;
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_name_tv)
        TextView trailerName;
        @BindView(R.id.thumbnail_iv)
        ImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(String name, final String key) {
            trailerName.setText(name);
            String thumbnailPath = YOUTUBE_THUMBNAIL_URL + key + THUMBNAIL_QUALITY;
            Picasso.with(mContext)
                    .load(thumbnailPath)
                    .into(thumbnail);

            trailerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareVideo(key);
                }
            });

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewVideo(key);
                }
            });
        }
    }
}
