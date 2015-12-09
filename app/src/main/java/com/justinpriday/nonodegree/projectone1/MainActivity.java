package com.justinpriday.nonodegree.projectone1;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.justinpriday.nonodegree.projectone1.util.MDBConsts;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
