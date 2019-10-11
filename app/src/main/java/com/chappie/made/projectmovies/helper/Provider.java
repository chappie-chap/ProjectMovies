package com.chappie.made.projectmovies.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.chappie.made.projectmovies.database.FavMovieHelper;
import com.chappie.made.projectmovies.database.FavTvShowHelper;
import com.chappie.made.projectmovies.fragment.favorite.FavMovieFragment;
import com.chappie.made.projectmovies.fragment.favorite.FavTvShowFragment;

import static com.chappie.made.projectmovies.database.DatabaseContract.AUTHORITY;
import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_MOVIE_URI;
import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_TVSHOW_URI;
import static com.chappie.made.projectmovies.database.DatabaseContract.TABLE_MOVIE;
import static com.chappie.made.projectmovies.database.DatabaseContract.TABLE_TVSHOW;

public class Provider extends ContentProvider {

    private int deleted;
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    private static final UriMatcher matcher = new
            UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        matcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);
        matcher.addURI(AUTHORITY, TABLE_TVSHOW, TVSHOW);
        matcher.addURI(AUTHORITY, TABLE_TVSHOW + "/#", TVSHOW_ID);
    }

    private FavMovieHelper favMovieHelper;
    private FavTvShowHelper favTvShowHelper;

    @Override
    public boolean onCreate() {
        favMovieHelper = FavMovieHelper.getInstance(getContext());
        favTvShowHelper = FavTvShowHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        favMovieHelper.open();
        favTvShowHelper.open();
        Cursor cursor;
        switch (matcher.match(uri)) {
            case MOVIE:
                cursor = favMovieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = favMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = favTvShowHelper.queryProvider();
                break;
            case TVSHOW_ID:
                cursor = favTvShowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri uri1;
        favMovieHelper.open();
        favTvShowHelper.open();
        long added;
        if (MOVIE == matcher.match(uri)) {
            added = favMovieHelper.insertProvider(values);
            uri1 = Uri.parse(CONTENT_MOVIE_URI + "/" + added);
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE_URI,
                        new FavMovieFragment.DataObserver(new Handler(), getContext()));
            }
        } else if (TVSHOW == matcher.match(uri)) {
            added = favTvShowHelper.insertProvider(values);
            uri1 = Uri.parse(CONTENT_TVSHOW_URI + "/" + added);
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(CONTENT_TVSHOW_URI,
                        new FavTvShowFragment.DataObserver(new Handler(), getContext()));
            }
        } else {
            throw new SQLException("Failed to insert data " + uri);
        }
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        favMovieHelper.open();
        favTvShowHelper.open();
        if (MOVIE_ID == matcher.match(uri)) {
            deleted = favMovieHelper.deleteProvider(uri.getLastPathSegment());
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE_URI,
                        new FavMovieFragment.DataObserver(new Handler(), getContext()));
            }
        } else if (TVSHOW_ID == matcher.match(uri)) {
            deleted = favTvShowHelper.deleteProvider(uri.getLastPathSegment());
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(CONTENT_TVSHOW_URI,
                        new FavTvShowFragment.DataObserver(new Handler(), getContext()));
            }
        } else {
            throw new SQLException("Failed to insert data " + uri);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
