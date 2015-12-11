package com.justinpriday.nonodegree.projectTwo.Loaders;

//import android.content.AsyncTaskLoader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.justinpriday.nonodegree.projectTwo.API.MDBApi;
import com.justinpriday.nonodegree.projectTwo.BuildConfig;
import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.models.Movies;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by justin on 15/12/11.
 */
public class APILoader extends AsyncTaskLoader<List<MovieData>> {

    private List<MovieData> mResults;

    private static final String LOG_TAG = APILoader.class.getSimpleName();

    public APILoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieData> loadInBackground() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MDBConsts.MDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MDBApi mdbApi = retrofit.create(MDBApi.class);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String sortBy = mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY, MDBConsts.MOVIE_SORT_DEFAULT);
        Call<Movies> moviesCall = mdbApi.getMovieResults(sortBy, BuildConfig.THE_MOVIE_DB_API_KEY);
        try {
            Response<Movies> result = moviesCall.execute();
            return result.body().results;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"IOException in loadInBackground()");
        }

        return null;
    }

    @Override
    public void deliverResult(List<MovieData> data) {
        mResults = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResults != null) {
            deliverResult(mResults);
        }

        if (takeContentChanged() || mResults == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStartLoading();
    }

    @Override
    protected List<MovieData> onLoadInBackground() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MDBConsts.MDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MDBApi mdbApi = retrofit.create(MDBApi.class);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String sortBy = mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY, MDBConsts.MOVIE_SORT_DEFAULT);
        Call<Movies> moviesCall = mdbApi.getMovieResults(sortBy, BuildConfig.THE_MOVIE_DB_API_KEY);
        try {
            Response<Movies> result = moviesCall.execute();
            return result.body().results;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"IOException in loadInBackground()");
        }

        return null;
    }
}
