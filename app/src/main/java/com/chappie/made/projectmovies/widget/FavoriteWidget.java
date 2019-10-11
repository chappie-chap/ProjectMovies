package com.chappie.made.projectmovies.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.chappie.made.projectmovies.DetailActivity;
import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.fragment.favorite.FavMovieFragment;

import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_MOVIE_URI;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {
    public static final String EXTRA_WIDGET = "com.chappie.made.projectmovies.EXTRA_WIDGET";
    private static final String TOAST = "com.chappie.made.projectmovies.TOAST_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        remoteViews.setRemoteAdapter(R.id.widget_stackView, intent);
        remoteViews.setEmptyView(R.id.widget_stackView,  R.id.widget_txtEmpty);

        Intent toast = new Intent(context, FavoriteWidget.class);
        toast.setAction(FavoriteWidget.TOAST);
        toast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,toast,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_stackView, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null ){
            if(intent.getAction().equals(TOAST)){
                String TD = intent.getStringExtra(EXTRA_WIDGET);
                Toast.makeText(context,TD, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

