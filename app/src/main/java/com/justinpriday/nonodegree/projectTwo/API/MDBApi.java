package com.justinpriday.nonodegree.projectTwo.API;

import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.models.MovieReviews;
import com.justinpriday.nonodegree.projectTwo.models.MovieTrailers;
import com.justinpriday.nonodegree.projectTwo.models.Movies;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by justin on 15/12/09.
 */
public interface MDBApi {
    @GET("/3/discover/movie")
    Call<Movies> getMovieResults(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<MovieTrailers> getTrailersResults(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<MovieReviews> getReviewsResults(@Path("id") long movieId, @Query("api_key") String apiKey);
}
