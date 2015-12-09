package com.justinpriday.nonodegree.projectTwo;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.models.MovieReviewData;
import com.justinpriday.nonodegree.projectTwo.models.MovieTrailerData;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private ArrayList<MovieTrailerData> mTrailerList = null;
    private ArrayList <MovieReviewData> mReviewList = null;

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

    @Bind(R.id.movie_detail_trailer_card_view) CardView trailerCard;
    @Bind(R.id.movie_detail_trailer_list) LinearLayout trailerListLayout;
    @Bind(R.id.movie_detail_review_card_view) CardView reviewCard;
    @Bind(R.id.movie_detail_review_list) LinearLayout reviewListLayout;

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
//                                bgMutedColor = palette.getMutedColor(0x000000);
//                                GradientDrawable bgBorder = (GradientDrawable) moviePoster.getBackground();
//                                bgBorder.setColor(bgMutedColor);
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

        MovieTrailerData[] tList = {new MovieTrailerData("Trailer1","Y0LKWt3Ttic"),
            new MovieTrailerData("Trailer 2","Y0LKWt3Ttic")};
        mTrailerList = new ArrayList<>(Arrays.asList(tList));
        updateTrailers(mTrailerList);

        MovieReviewData[] rList = {new MovieReviewData("Author 1","Content 1")
            ,new MovieReviewData("Author 2","Content 2")};
        mReviewList = new ArrayList<>(Arrays.asList(rList));
        updateReviews(mReviewList);

        return rootView;
    }

    private void trailerSelected(String trailerURL) {
        Toast.makeText(getContext(),trailerURL+" Selected",Toast.LENGTH_SHORT).show();
    }

    private void updateTrailers(List<MovieTrailerData> trailerList) {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        trailerListLayout.removeAllViews();

        if (mTrailerList.size() > 0) {
            trailerCard.setVisibility(View.VISIBLE);
        } else {
            trailerCard.setVisibility(View.GONE);
        }

        for (final MovieTrailerData trailer : trailerList) {
            final View trailerView = inflater.inflate(R.layout.movie_detail_trailer_item, trailerListLayout, false);
            ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_item_image);
            TextView trailerTitle = ButterKnife.findById(trailerView, R.id.trailer_item_title);

            Picasso.with(getActivity())
                    .load(trailer.getTrailerThumbURL())
                    .into(trailerImage);

            trailerTitle.setText(trailer.trailerTitle);

            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trailerSelected(trailer.trailerTitle);
                }
            });
            trailerListLayout.addView(trailerView);
        }
    }

    private void updateReviews(List<MovieReviewData> reviewList) {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        reviewListLayout.removeAllViews();

        if (mReviewList.size() > 0) {
            reviewCard.setVisibility(View.VISIBLE);
        } else {
            reviewCard.setVisibility(View.GONE);
        }

        for (final MovieReviewData review : reviewList) {
            final View reviewView = inflater.inflate(R.layout.movie_detail_review_item, reviewListLayout, false);
            TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.review_item_author);

            reviewAuthor.setText(review.reviewAuthor);
            reviewListLayout.addView(reviewView);
        }
    }
}
