package com.justinpriday.nonodegree.projectTwo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.justinpriday.nonodegree.projectTwo.models.MovieData;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.CallBack {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_details_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Toolbar topToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);
    }



    private Void setSortPreference(String inPref) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MDBConsts.MOVIE_SHARED_PREFERENCE_SORT_KEY,inPref);
        editor.apply();
        return null;
    }

    @Override
    public void OnItemSelected(MovieData movieItem, Bitmap moviePoster) {
        if (mTwoPane) {
            Bundle args = new Bundle();
//            if (moviePoster != null) {
//                args.putParcelable(MDBConsts.MOVIE_POSTER_BITMAP_KEY, moviePoster);
//            }

            if (movieItem != null) {
                MovieDetailFragment fragment = new MovieDetailFragment();
                args.putParcelable(MDBConsts.MOVIE_DATA_KEY,movieItem);
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            }


        } else {
                Intent intent = new Intent(this,MovieDetailActivity.class);
//                if (moviePoster != null) {
//                    intent.putExtra(MDBConsts.MOVIE_POSTER_BITMAP_KEY, moviePoster);
//                }
                if (movieItem != null) {
                    intent.putExtra(MDBConsts.MOVIE_DATA_KEY,movieItem);
                    startActivity(intent);
                }

        }
    }
}
