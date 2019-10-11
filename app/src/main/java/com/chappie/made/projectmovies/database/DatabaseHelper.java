package com.chappie.made.projectmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.chappie.made.projectmovies.database.DatabaseContract.TABLE_MOVIE;
import static com.chappie.made.projectmovies.database.DatabaseContract.TABLE_TVSHOW;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbmovie";

    private static final int DATABASE_VERSION = 4;

    private static final String CREATE_TABLE_MOVIE = "create table " + TABLE_MOVIE + " ( "
            + DatabaseContract.TableColumns._ID + " text primary key, "
            + DatabaseContract.TableColumns.TITLE + " text not null, "
            + DatabaseContract.TableColumns.OVERVIEW + " text not null, "
            + DatabaseContract.TableColumns.POPULARITY + " text not null, "
            + DatabaseContract.TableColumns.POSTER + " text not null, "
            + DatabaseContract.TableColumns.BACKDROP + " text not null)";

    private static final String CREATE_TABLE_TVSHOW = "create table " + TABLE_TVSHOW + " ( "
            + DatabaseContract.TableColumns._ID + " text primary key, "
            + DatabaseContract.TableColumns.TITLE + " text not null, "
            + DatabaseContract.TableColumns.OVERVIEW + " text not null, "
            + DatabaseContract.TableColumns.POPULARITY + " text not null, "
            + DatabaseContract.TableColumns.POSTER + " text not null, "
            + DatabaseContract.TableColumns.BACKDROP + " text not null)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
        db.execSQL(CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MOVIE);
        db.execSQL("drop table if exists " + TABLE_TVSHOW);
        onCreate(db);
    }
}