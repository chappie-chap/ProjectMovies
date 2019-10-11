package com.chappie.made.favoriteapp.helper;

import android.content.ContentValues;

import com.chappie.made.favoriteapp.parcleable.Movie;
import com.chappie.made.favoriteapp.parcleable.TvShow;

import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.BACKDROP;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.OVERVIEW;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.POPULARITY;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.POSTER;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns.TITLE;
import static com.chappie.made.favoriteapp.database.DatabaseContract.TableColumns._ID;

public class ContentValueHelper {

    public static ContentValues getContentValueMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(_ID, movie.getId_movie());
        values.put(TITLE, movie.getTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(POPULARITY, movie.getPopularity());
        values.put(POSTER, movie.getImg_poster());
        values.put(BACKDROP, movie.getImg_backdrop());
        return values;
    }

    public static ContentValues getContentValueTVShow(TvShow tvShow) {
        ContentValues values = new ContentValues();
        values.put(_ID, tvShow.getId_tvshow());
        values.put(TITLE, tvShow.getTitle());
        values.put(OVERVIEW, tvShow.getOverview());
        values.put(POPULARITY, tvShow.getPopularity());
        values.put(POSTER, tvShow.getImg_poster());
        values.put(BACKDROP, tvShow.getImg_backdrop());
        return values;
    }
}
