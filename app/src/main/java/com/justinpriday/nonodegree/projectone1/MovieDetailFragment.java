package com.justinpriday.nonodegree.projectone1;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.justinpriday.nonodegree.projectone1.models.MovieData;
import com.justinpriday.nonodegree.projectone1.util.MDBConsts;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private MovieData mMovieItem = null;
//    private Bitmap mMoviePoster = null;

    private int mutedColor;
    private int bgMutedColor;

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout ctb;
    @Bind(R.id.movie_detail_poster_image_view) ImageView moviePoster;
    @Bind(R.id.movie_detail_backdrop_image_view) ImageView backdropImage;
    @Bind(R.id.movie_detail_popularity_text_view) TextView popularityText;
    @Bind(R.id.movie_detail_rating_text_view) TextView ratingText;
    @Bind(R.id.movie_detail_synopsys_data_text_view) TextView overviewText;
    @Bind(R.id.movie_detail_year_text_view) TextView yearText;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieItem = getActivity().getIntent().getExtras().getParcelable(MDBConsts.MOVIE_DATA_KEY);
//        mMoviePoster = getActivity().getIntent().getExtras().getParcelable(MDBConsts.MOVIE_POSTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar tBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (tBar != null) {
            tBar.setDisplayHomeAsUpEnabled(true);
            tBar.setDisplayShowHomeEnabled(true);
        }

//        if (getActivity() != null) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }

        ctb.setTitle(mMovieItem.originalTitle);
        ctb.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        ctb.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.with(getActivity())
                .load(mMovieItem.getPosterURL())
                .into(moviePoster, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) moviePoster.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                            @Override
                            public void onGenerated(Palette palette) {
                                //This often doesn't look nice, I've considered returning to colorPrimary to tie in with
                                //surrounding bars, but left the poster palette code because its more interesting.
                                bgMutedColor = palette.getMutedColor(0x000000);
                                GradientDrawable bgBorder = (GradientDrawable) moviePoster.getBackground();
                                bgBorder.setColor(bgMutedColor);
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });

        Log.v(LOG_TAG, "Got Backdrop URL " + mMovieItem.getBackdropURL());
        Picasso.with(getActivity())
                .load(mMovieItem.getBackdropURL())
                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.poster_placeholder))
                .error(ContextCompat.getDrawable(getContext(), R.drawable.poster_placeholder))
                .into(backdropImage, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) backdropImage.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                            @Override
                            public void onGenerated(Palette palette) {
                                mutedColor = palette.getMutedColor(0x000000);
                                ctb.setContentScrimColor(mutedColor);
                            }
                        });

                    }

                    @Override
                    public void onError() {

                    }
                });

        popularityText.setText(String.format("%.0f",mMovieItem.popularity));
        ratingText.setText(String.format("%.1f / 10",mMovieItem.voteAverage));
        overviewText.setText(mMovieItem.overview);

        if (mMovieItem.getFormattedDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mMovieItem.getFormattedDate().getTime());
            yearText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        }

        return rootView;
    }
}
