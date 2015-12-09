package com.justinpriday.nonodegree.projectone1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.justinpriday.nonodegree.projectone1.adapter.MovieAdapter;
import com.justinpriday.nonodegree.projectone1.models.MovieData;
import com.justinpriday.nonodegree.projectone1.tasks.FetchMovieListTask;
import com.justinpriday.nonodegree.projectone1.util.MDBConsts;

import java.util.ArrayList;

public class MovieGridFragment extends Fragment {

    private static final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    private ArrayList<MovieData> mMovieList = null;
    private MovieAdapter mMovieAdaptor;

    public MovieGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(MDBConsts.MOVIE_BUNDLE_PARCELABLE_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    private Void setSortPreference(String inPref) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY, inPref);
        editor.apply();
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_one : {
                Log.v(LOG_TAG,"Menu Item One Selected");
                setSortPreference(MDBConsts.MOVIE_SORT_POPULARITY);
                updateMovies();
                break;}

            case R.id.menu_item_two: {
                Log.v(LOG_TAG, "Menu Item Two Selected");
                setSortPreference(MDBConsts.MOVIE_SORT_RATING);
                updateMovies();
                break;}

            case R.id.menu_refresh: {
                updateMovies();
                break;}
        }

        SharedPreferences x = PreferenceManager.getDefaultSharedPreferences(getContext());
        String prefString = x.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,"");
        Log.v(LOG_TAG, "Set preference to " + prefString);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        if (mMovieList == null) {
            mMovieAdaptor = new MovieAdapter(getActivity(),new ArrayList<MovieData>());
        } else {
            mMovieAdaptor = new MovieAdapter(getActivity(),mMovieList);
        }

        GridView mGridView = (GridView) view.findViewById(R.id.movie_overview_grid);
        mGridView.setAdapter(mMovieAdaptor);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MovieDetailActivity.class);
                ImageView posterImage = (ImageView)view.findViewById(R.id.grid_item_image_poster);
                Bitmap bitmap = ((BitmapDrawable)posterImage.getDrawable()).getBitmap();
                if (bitmap != null) {
                    intent.putExtra(MDBConsts.MOVIE_POSTER_BITMAP_KEY,bitmap);
                }
                MovieData movieItem = mMovieList.get(position);
                if (movieItem != null) {
                    intent.putExtra(MDBConsts.MOVIE_DATA_KEY,movieItem);
                    startActivity(intent);
                }
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<MovieData> saveArray = new ArrayList<>();
        for (int i = 0;i < mMovieAdaptor.getCount();i++) {
            saveArray.add(mMovieAdaptor.getItem(i));
        }
        if (saveArray.size() > 0) {
            outState.putParcelableArrayList(MDBConsts.MOVIE_BUNDLE_PARCELABLE_KEY,saveArray);
        }
        super.onSaveInstanceState(outState);
    }

    private void updateMovies() {
        FetchMovieListTask movieTask = new FetchMovieListTask(new GotMoviesCallback() {
            @Override
            public void movieListTaskDone(ArrayList<MovieData> movieList) {
                gotNewMovies(movieList);
            }
        });
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        movieTask.execute(mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,MDBConsts.MOVIE_SORT_DEFAULT));
    }

    public interface GotMoviesCallback {
        void movieListTaskDone(ArrayList<MovieData> movieList);
    }

    private void gotNewMovies(ArrayList<MovieData> newMovies) {
        Log.v(LOG_TAG,"Got "+newMovies.size()+" new movies");
        mMovieList = newMovies;
        Log.v(LOG_TAG, mMovieList.size() + " Items from task");

        mMovieAdaptor.clear();

        for (MovieData aMovie : newMovies) {
            mMovieAdaptor.insert(aMovie, mMovieAdaptor.getCount());
        }
        mMovieAdaptor.notifyDataSetChanged();
        if (getContext() != null) {
            Toast.makeText(getContext(), "Movie List Updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovieList != null) {
            if (mMovieList.size() < 1) {
                updateMovies();
            }
        } else {
            updateMovies();
        }
    }
}
