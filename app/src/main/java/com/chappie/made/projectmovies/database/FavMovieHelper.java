package com.chappie.made.projectmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.chappie.made.projectmovies.database.DatabaseContract.TABLE_MOVIE;
import static com.chappie.made.projectmovies.database.DatabaseContract.TableColumns._ID;

public class FavMovieHelper {
    private static final String DB_TABLE = TABLE_MOVIE;
    private static DatabaseHelper dbHelper;
    private static FavMovieHelper INSTANCE;
    private static SQLiteDatabase db;

    private FavMovieHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static FavMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }


    public Cursor queryByIdProvider(String id) {
        return db.query(DB_TABLE, null, _ID + " = ? ",
                new String[]{id}, null, null, null, null);
    }

    public Cursor queryProvider() {
        return db.query(DB_TABLE, null, null, null,
                null, null, _ID + " ASC", null);
    }

    public long insertProvider(ContentValues values) {
        return db.insert(DB_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return db.delete(DB_TABLE, _ID + " = ?", new String[]{id});
    }
}
