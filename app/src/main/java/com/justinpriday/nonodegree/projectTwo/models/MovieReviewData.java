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
public class MovieReviewData implements Parcelable {

    private static final String LOG_TAG = MovieReviewData.class.getSimpleName();

    public String id;
    public String reviewAuthor;
    public String reviewContent;

    protected MovieReviewData(Parcel in) {
        this.id = in.readString();
        this.reviewAuthor = in.readString();
        this.reviewContent = in.readString();
    }

    public MovieReviewData(JSONObject inJSON) {
        try {
            this.id = inJSON.getString(MDBConsts.MOVIE_REVIEW_ID);
            this.reviewAuthor = inJSON.getString(MDBConsts.MOVIE_REVIEW_AUTHOR);
            this.reviewContent = inJSON.getString(MDBConsts.MOVIE_REVIEW_CONTENT);
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }
    }

    public MovieReviewData(String inAuthor,String inContent) {
        this.reviewAuthor = inAuthor;
        this.reviewContent = inContent;
    }

    public static final Creator<MovieReviewData> CREATOR = new Creator<MovieReviewData>() {
        @Override
        public MovieReviewData createFromParcel(Parcel in) {
            return new MovieReviewData(in);
        }

        @Override
        public MovieReviewData[] newArray(int size) {
            return new MovieReviewData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.reviewAuthor);
        dest.writeString(this.reviewContent);
    }
}
