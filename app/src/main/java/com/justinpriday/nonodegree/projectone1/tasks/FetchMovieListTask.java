package com.justinpriday.nonodegree.projectone1.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.justinpriday.nonodegree.projectone1.BuildConfig;
import com.justinpriday.nonodegree.projectone1.MovieGridFragment;
import com.justinpriday.nonodegree.projectone1.models.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by justin on 15/11/30.
 */

public class FetchMovieListTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[API_KEY]


    private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

    private final Context mContext;

    private final MovieGridFragment.GotMoviesCallback mMoviesCallback;

    private ArrayList<MovieData> movieList = null;


    private FetchMovieListTask(Context context) {
        mContext = context;
        mMoviesCallback = null;
    }

    public FetchMovieListTask(MovieGridFragment.GotMoviesCallback movieListCallBack) {
        mContext = null;
        mMoviesCallback = movieListCallBack;
    }


    private ArrayList<MovieData> getMovieDataFromJSON(String movieJSONString)
        throws JSONException {

        final String MDB_LIST = "results";

        JSONObject movieJSON = new JSONObject(movieJSONString);
        JSONArray movieArray = movieJSON.getJSONArray(MDB_LIST);

        ArrayList<MovieData> movieList = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            MovieData aMovie = new MovieData(movieArray.getJSONObject(i));
            movieList.add(aMovie);
        }
        return movieList;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJSONString;

        movieList = new ArrayList<>();

        try {

            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";

            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM,params[0])
                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieJSONString = buffer.toString();
            movieList = getMovieDataFromJSON(movieJSONString);
//            getMovieDataFromJSON(movieJSONString);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG,"Error closing stream",e);
                }
            }
            return movieList;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> movieDatas) {
//        MovieGridFragment mList = ((MainActivity) mContext).getFragmentManager().findFragmentById(R.id.movie_grid_fragment);
//        mList.gotNewMovies();
        if (mMoviesCallback != null) {
            mMoviesCallback.movieListTaskDone(movieList);
        }
        super.onPostExecute(movieDatas);

    }
}
