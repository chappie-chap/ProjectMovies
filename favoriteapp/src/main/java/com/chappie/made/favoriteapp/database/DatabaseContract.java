package com.chappie.made.favoriteapp.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.chappie.made.projectmovies";
    public static final String TABLE_MOVIE = "Movie";
    public static final String TABLE_TVSHOW = "TvShow";
    private static final String SCHEME = "content";
    public static final Uri CONTENT_MOVIE_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static final Uri CONTENT_TVSHOW_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_TVSHOW)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static final class TableColumns implements BaseColumns {
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String POPULARITY = "popularity";
        public static final String POSTER = "poster";
        public static final String BACKDROP = "backdrop";
        public static final String DATE = "date";
    }
}
