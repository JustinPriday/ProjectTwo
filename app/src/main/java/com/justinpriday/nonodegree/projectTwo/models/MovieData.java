package com.justinpriday.nonodegree.projectTwo.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.justinpriday.nonodegree.projectTwo.util.MDBConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by justin on 15/11/27.
 */
public class MovieData implements Parcelable {

    private static final String LOG_TAG = MovieData.class.getSimpleName();

    private static final String MDB_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String MDB_POSTER_SIZE = "w185";
    private static final String MDB_BACKDROP_SIZE = "w342";


    public long id;
    @SerializedName("original_title")
    public String originalTitle;
    public String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("cover_count")
    private int voteCount;
    public double popularity;

    public Date getFormattedDate() {
        if (!TextUtils.isEmpty(releaseDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MDBConsts.MOVIE_DATE_FORMAT);
            try {
                return simpleDateFormat.parse(releaseDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "getFormattedDate() returned error: " + e);
            }
        }
        return null;
    }

    private MovieData() {

    }

    private MovieData(Parcel in) {
        this.id = in.readLong();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.voteAverage = in.readDouble();
        this.voteCount = in.readInt();
        this.popularity = in.readDouble();
    }

    public MovieData(JSONObject inJSON) {
        try {
            this.id = inJSON.getInt(MDBConsts.MOVIE_ID);
            this.originalTitle = inJSON.getString(MDBConsts.MOVIE_TITLE);
            this.releaseDate = inJSON.getString(MDBConsts.MOVIE_RELEASE_DATE);
            this.posterPath = inJSON.getString(MDBConsts.MOVIE_POSTER);
            this.voteAverage = inJSON.getDouble(MDBConsts.MOVIE_VOTE_AVERAGE);
            this.voteCount = inJSON.getInt(MDBConsts.MOVIE_VOTE_COUNT);
            this.popularity = inJSON.getDouble(MDBConsts.MOVIE_POPULARITY);
            this.overview = inJSON.getString(MDBConsts.MOVIE_SYNOPSIS);
            this.backdropPath = inJSON.getString(MDBConsts.MOVIE_BACKDROP);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();        }
    }

    public String getPosterURL () {
        return MDB_IMAGE_URL + MDB_POSTER_SIZE + "/" + this.posterPath;
    }

    public String getBackdropURL () {
        return MDB_IMAGE_URL + MDB_BACKDROP_SIZE + "/" + this.backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeDouble(this.voteAverage);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.popularity);
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public String toString() {
        return "MovieData{" +
                "id=" + id +
                ", originalTitle='" + originalTitle +
                ", overview='" + overview + "'" +
                ", releaseDate='" + releaseDate + "'" +
                ", posterPath='" + posterPath + "'" +
                ", backdropPath='" + backdropPath + "'" +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", popularity=" + popularity +
                "}";
    }
}
