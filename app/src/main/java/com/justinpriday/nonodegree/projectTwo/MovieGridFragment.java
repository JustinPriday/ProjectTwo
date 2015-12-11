package com.justinpriday.nonodegree.projectTwo;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.justinpriday.nonodegree.projectTwo.Loaders.APILoader;
import com.justinpriday.nonodegree.projectTwo.adapter.MovieAdapter;
import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.tasks.FetchMovieListTask;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieData>> {

    private static final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    private static final int MOVIE_LOADER = 0;

    private ArrayList<MovieData> mMovieList = null;
    private MovieAdapter mMovieAdaptor;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<List<MovieData>> onCreateLoader(int id, Bundle args) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,MDBConsts.MOVIE_SORT_DEFAULT).equals(MDBConsts.MOVIE_SORT_FAVOURITES)) {
             //Favourites Loader
        } else {
            return new APILoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> data) {
        gotNewMovies((ArrayList<MovieData>) data);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieData>> loader) {

    }

    public interface CallBack {
        void OnItemSelected(MovieData movieItem, Bitmap moviePoster);
    }

    @Bind(R.id.movie_overview_grid) GridView mGridView;

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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
            case R.id.menu_item_popularity : {
                setSortPreference(MDBConsts.MOVIE_SORT_POPULARITY);
                getLoaderManager().restartLoader(0, null, this);
//                updateMovies();
                break;}

            case R.id.menu_item_rating: {
                setSortPreference(MDBConsts.MOVIE_SORT_RATING);
                getLoaderManager().restartLoader(0, null, this);
//                updateMovies();
                break;}

            case R.id.menu_item_favourites: {
                Log.v(LOG_TAG, "User favourites selected");
                break;}

            case R.id.menu_refresh: {
                updateMovies();
                break;}

            default:
                Log.e(LOG_TAG,"Unknown menu item selected");
        }

        SharedPreferences x = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefString = x.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,"");
        Log.v(LOG_TAG, "Set preference to " + prefString);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        ButterKnife.bind(this, view);

        if (mMovieList == null) {
            mMovieAdaptor = new MovieAdapter(getActivity(),new ArrayList<MovieData>());
        } else {
            mMovieAdaptor = new MovieAdapter(getActivity(),mMovieList);
        }

        mGridView.setAdapter(mMovieAdaptor);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(),MovieDetailActivity.class);
                ImageView posterImage = (ImageView) view.findViewById(R.id.grid_item_image_poster);
                Bitmap bitmap = ((BitmapDrawable) posterImage.getDrawable()).getBitmap();
//                if (bitmap != null) {
//                    intent.putExtra(MDBConsts.MOVIE_POSTER_BITMAP_KEY,bitmap);
//                }
                MovieData movieItem = mMovieList.get(position);
//                if (movieItem != null) {
//                    intent.putExtra(MDBConsts.MOVIE_DATA_KEY,movieItem);
//                    startActivity(intent);
//                }
                ((CallBack) getActivity()).OnItemSelected(movieItem, bitmap);
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
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieListType = mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,MDBConsts.MOVIE_SORT_DEFAULT);
        if (movieListType.equals(MDBConsts.MOVIE_SORT_FAVOURITES)) {
            //TODO:Collect Movie List from Provider.
        } else {
//            FetchMovieListTask movieTask = new FetchMovieListTask(new GotMoviesCallback() {
//                @Override
//                public void movieListTaskDone(ArrayList<MovieData> movieList) {
//                    gotNewMovies(movieList);
//                }
//            });
//            movieTask.execute(mPrefs.getString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY, MDBConsts.MOVIE_SORT_DEFAULT));
        }
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
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Movie List Updated", Toast.LENGTH_SHORT).show();
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
