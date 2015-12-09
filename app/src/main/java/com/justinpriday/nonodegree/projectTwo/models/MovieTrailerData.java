package com.justinpriday.nonodegree.projectTwo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justin on 15/12/09.
 */
public class MovieTrailerData implements Parcelable {

    private static final String LOG_TAG = MovieTrailerData.class.getSimpleName();

    public String id; //Unique String ID of video from MDB
    public String trailerTitle;
    public String trailerKey; //Unique Identifier of video on site
    public int trailerSite; //Internal Key for site, based on URL parsing support. Unsupported sites rejected at API receipt stage.
    //1=Youtube


    protected MovieTrailerData(Parcel in) {
        this.id = in.readString();
        this.trailerTitle = in.readString();
        this.trailerKey = in.readString();
        this.trailerSite = in.readInt();
    }

    public MovieTrailerData(String inTitle,String inKey) {
        this.id = "";
        this.trailerTitle = inTitle;
        this.trailerKey = inKey;
        this.trailerSite = 1;
    }

    public MovieTrailerData(JSONObject inJSON) {
        try {
            this.id = inJSON.getString(MDBConsts.MOVIE_TRAILER_ID);
            this.trailerTitle = inJSON.getString(MDBConsts.MOVIE_TRAILER_TITLE);
            this.trailerKey = inJSON.getString(MDBConsts.MOVIE_TRAILER_KEY);
            this.trailerSite = MDBConsts.GET_SITE_ID(inJSON.getString(MDBConsts.MOVIE_TRAILER_SITE));
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }
    }

    public static final Creator<MovieTrailerData> CREATOR = new Creator<MovieTrailerData>() {
        @Override
        public MovieTrailerData createFromParcel(Parcel in) {
            return new MovieTrailerData(in);
        }

        @Override
        public MovieTrailerData[] newArray(int size) {
            return new MovieTrailerData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.trailerTitle);
        dest.writeString(this.trailerKey);
        dest.writeInt(this.trailerSite);
    }

    public String getTrailerThumbURL() {
        switch (this.trailerSite) {
            case 1:
                return MDBConsts.MOVIE_TRAILER_YOUTUBE_THUMBNAIL_URL + this.trailerKey + "/"
                        + MDBConsts.MOVIE_TRAILER_YOUTUBE_THUMBNAIL_VERSION;
            default:
                return null;
        }
    }

    public String getTrailerURL() {
        switch (this.trailerSite) {
            case 1:
                return MDBConsts.MOVIE_TRAILER_YOUTUBE_URL + this.trailerKey;

            default:
                return null;
        }
    }
}
