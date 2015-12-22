package com.justinpriday.nonodegree.projectTwo.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.justinpriday.nonodegree.projectTwo.Data.MovieContract;
import com.justinpriday.nonodegree.projectTwo.MovieGridFragment;
import com.justinpriday.nonodegree.projectTwo.R;
import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by justin on 15/11/27.
 */
public class MovieAdapter extends ArrayAdapter<MovieData> {


    public MovieAdapter(Activity context, List<MovieData> movieData) {
        super(context, 0, movieData);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieData movieData = getItem(position);

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_grid_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Picasso.with(getContext())
                .load(movieData.getPosterURL())
                .placeholder(ContextCompat.getDrawable(getContext(),R.drawable.poster_placeholder))
                .error(ContextCompat.getDrawable(getContext(),R.drawable.poster_placeholder))
                .into(holder.moviePoster);

        holder.popularityText.setText(String.format("%.0f",movieData.popularity));
        holder.ratingText.setText(String.format("%.1f / 10",movieData.voteAverage));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.grid_item_image_poster) ImageView moviePoster;
        @Bind(R.id.movie_grid_popularity_text_view) TextView popularityText;
        @Bind(R.id.movie_grid_rating_text_view) TextView ratingText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}