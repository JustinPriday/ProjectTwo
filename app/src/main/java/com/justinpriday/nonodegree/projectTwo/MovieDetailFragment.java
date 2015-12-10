package com.justinpriday.nonodegree.projectTwo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.justinpriday.nonodegree.projectTwo.API.MDBApi;
import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.models.MovieReviewData;
import com.justinpriday.nonodegree.projectTwo.models.MovieReviews;
import com.justinpriday.nonodegree.projectTwo.models.MovieTrailerData;
import com.justinpriday.nonodegree.projectTwo.models.MovieTrailers;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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

        ctb.setTitle(mMovieItem.originalTitle);
        ctb.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        ctb.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.with(getActivity())
                .load(mMovieItem.getPosterURL())
                .into(moviePoster);

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

        updateTrailers(new ArrayList<MovieTrailerData>());

        updateReviews(new ArrayList<MovieReviewData>());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MDBConsts.MDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MDBApi mdbApi = retrofit.create(MDBApi.class);

        Call<MovieTrailers> trailersCall = mdbApi.getTrailersResults(mMovieItem.id, BuildConfig.THE_MOVIE_DB_API_KEY);
        Callback<MovieTrailers> trailersCallback = new Callback<MovieTrailers>() {
            @Override
            public void onResponse(Response<MovieTrailers> response, Retrofit retrofit) {
                List<MovieTrailerData> tMovieList = response.body().results;
                for (MovieTrailerData trailer : tMovieList)
                    trailer.trailerSite = MDBConsts.GET_SITE_ID(trailer.trailerSiteName);
                updateTrailers(response.body().results);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        trailersCall.enqueue(trailersCallback);

        Call<MovieReviews> reviewsCall = mdbApi.getReviewsResults(mMovieItem.id, BuildConfig.THE_MOVIE_DB_API_KEY);
        Callback<MovieReviews> reviewsCallBack = new Callback<MovieReviews>() {
            @Override
            public void onResponse(Response<MovieReviews> response, Retrofit retrofit) {
                updateReviews(response.body().results);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        reviewsCall.enqueue(reviewsCallBack);

        return rootView;
    }

    private void trailerSelected(String trailerURL) {
//        Toast.makeText(getContext(),trailerURL+" Selected",Toast.LENGTH_SHORT).show();
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL));
        youTubeIntent.putExtra("force_fullscreen", true);
        startActivity(youTubeIntent);
    }

    private void updateTrailers(List<MovieTrailerData> trailerList) {

        trailerListLayout.removeAllViews();

        if (trailerList != null) {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            if (trailerList.size() > 0) {
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
                        trailerSelected(trailer.getTrailerURL());
                    }
                });
                trailerListLayout.addView(trailerView);
            }
        } else {
            trailerCard.setVisibility(View.GONE);
        }
    }

    private void updateReviews(List<MovieReviewData> reviewList) {

        reviewListLayout.removeAllViews();

        if (reviewList != null) {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());

            if (reviewList.size() > 0) {
                reviewCard.setVisibility(View.VISIBLE);
            } else {
                reviewCard.setVisibility(View.GONE);
            }

            Boolean firstItem = true;
            for (final MovieReviewData review : reviewList) {
                final View reviewView = inflater.inflate(R.layout.movie_detail_review_item, reviewListLayout, false);

                View divideTop = ButterKnife.findById(reviewView, R.id.review_item_top_divide);
                TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.review_item_author);
                TextView reviewContent = ButterKnife.findById(reviewView, R.id.review_item_content);

                divideTop.setVisibility(firstItem?View.GONE:View.VISIBLE);
                reviewAuthor.setText(review.reviewAuthor);
                reviewContent.setText(review.reviewContent);
                reviewListLayout.addView(reviewView);
                firstItem = false;
            }
        } else {
            reviewCard.setVisibility(View.GONE);
        }
    }
}
