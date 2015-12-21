package com.justinpriday.nonodegree.projectTwo.Loaders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v4.content.AsyncTaskLoader;

import com.justinpriday.nonodegree.projectTwo.Data.MovieContract;
import com.justinpriday.nonodegree.projectTwo.models.MovieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justin on 15/12/13.
 */
public class FavouritesLoader extends AsyncTaskLoader<List<MovieData>> {

    private List<MovieData> mResults;

    private static final String LOG_TAG = FavouritesLoader.class.getSimpleName();

    public FavouritesLoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieData> loadInBackground() {
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
    protected List<MovieData> onLoadInBackground() {
        ArrayList<MovieData> rList = new ArrayList<>();
        Cursor moviesCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        ContentValues movieValues;
        try {
            while (moviesCursor.moveToNext()) {
                movieValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(moviesCursor,movieValues);
                rList.add(new MovieData(movieValues));
            }
        } finally {
            moviesCursor.close();
        }
        return rList;
    }
}
