package com.chappie.made.projectmovies.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.database.DatabaseContract;
import com.chappie.made.projectmovies.database.DatabaseHelper;
import com.chappie.made.projectmovies.parcleable.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RemoteWidget implements RemoteViewsService.RemoteViewsFactory {

    private final ArrayList<Movie> movies= new ArrayList<>();
    private final Context context;
    private Cursor cursor;

    RemoteWidget(Context context){
        this.context = context;
    }

    @Override
    public void onCreate() {
        loadWidgets();
    }

    @Override
    public void onDataSetChanged() {
        loadWidgets();
    }

    private void loadWidgets() {
        movies.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        cursor = database.query(DatabaseContract.TABLE_MOVIE,null,null,
                null,null,null,null);
        if (cursor !=null){
            if (cursor.moveToFirst()){
                do {
                    Movie movie = new Movie(cursor);
                    movies.add(movie);
                }while (cursor.moveToNext());
            }
        }
    }

    @Override
    public void onDestroy() {
        if (cursor!=null){
            cursor.close();
        }
        movies.clear();

    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        String backdrop = movies.get(position).getImg_backdrop();
        String title = movies.get(position).getTitle();
        CharSequence titles = movies.get(position).getTitle();
        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(backdrop)
                    .apply(new RequestOptions().centerCrop())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.widget_backdrop,bitmap);
            remoteViews.setTextViewText(R.id.widget_title,titles);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString(FavoriteWidget.EXTRA_WIDGET, title);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.widget_backdrop,intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
