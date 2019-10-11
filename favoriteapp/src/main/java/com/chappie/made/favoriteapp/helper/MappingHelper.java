package com.chappie.made.favoriteapp.helper;

import android.database.Cursor;


import com.chappie.made.favoriteapp.parcleable.Movie;
import com.chappie.made.favoriteapp.parcleable.TvShow;

import java.util.ArrayList;

import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.BACKDROP;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.OVERVIEW;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.POPULARITY;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.POSTER;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.TITLE;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns._ID;


public class MappingHelper {

    public static ArrayList<Movie> mapCursorMovie(Cursor cursor) {
        ArrayList<Movie> m = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String popularity = cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP));
            m.add(new Movie(id, title, popularity, overview, poster, backdrop));
        }
        return m;
    }

    public static ArrayList<TvShow> mapCursorTvShow(Cursor cursor) {
        ArrayList<TvShow> tv = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String popularity = cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP));
            tv.add(new TvShow(id, title, popularity, overview, poster, backdrop));
        }
        return tv;
    }
}
