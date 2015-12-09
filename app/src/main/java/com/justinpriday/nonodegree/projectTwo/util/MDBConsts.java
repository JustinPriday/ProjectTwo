package com.justinpriday.nonodegree.projectTwo.util;

/**
 * Created by justin on 15/11/30.
 */
public class MDBConsts {
    public static final String MDB_BASE_URL = "http://api.themoviedb.org/";

    public static final String MOVIE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "original_title";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_POSTER = "poster_path";
    public static final String MOVIE_POPULARITY = "popularity";
    public static final String MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String MOVIE_VOTE_COUNT = "vote_count";
    public static final String MOVIE_SYNOPSIS = "overview";
    public static final String MOVIE_BACKDROP = "backdrop_path";

    public static final String MOVIE_DATA_KEY = "movie_item_extra";
    public static final String MOVIE_POSTER_BITMAP_KEY = "movie_poster_extra";

    public static final String MOVIE_TRAILER_ID = "id";
    public static final String MOVIE_TRAILER_TITLE = "name";
    public static final String MOVIE_TRAILER_KEY = "key";
    public static final String MOVIE_TRAILER_SITE = "site";
    private static final String[] MOVIE_TRAILER_SITES = {"YouTube"};
    public static final String MOVIE_TRAILER_YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    public static final String MOVIE_TRAILER_YOUTUBE_THUMBNAIL_VERSION = "default.jpg";
    public static final String MOVIE_TRAILER_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static final String MOVIE_REVIEW_ID = "id";
    public static final String MOVIE_REVIEW_AUTHOR = "author";
    public static final String MOVIE_REVIEW_CONTENT = "content";

    public static final String MOVIE_SHARED_PREFERENCE_SORT_KEY = "movies_sort_by";
    public static final String MOVIE_SORT_POPULARITY = "popularity.desc";
    public static final String MOVIE_SORT_RATING = "vote_average.desc";
    public static final String MOVIE_SORT_DEFAULT = "popularity.desc";

    public static final String MOVIE_BUNDLE_PARCELABLE_KEY = "popular_movies_list_key";

    public static int GET_SITE_ID(String inSite) {
        for (int i = 0;i < MOVIE_TRAILER_SITES.length;i++) {
            if (MOVIE_TRAILER_SITES[i].equals(inSite)) {
                return i+1;
            }
        }
        return 0;
    }
}
