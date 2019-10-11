package com.chappie.made.projectmovies.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chappie.made.projectmovies.DetailActivity;
import com.chappie.made.projectmovies.MainActivity;
import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.parcleable.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.chappie.made.projectmovies.BuildConfig.API_KEY;
import static com.chappie.made.projectmovies.BuildConfig.BASE_URL;
import static com.chappie.made.projectmovies.BuildConfig.MOVIE;
import static com.chappie.made.projectmovies.BuildConfig.POSTER_PATH;
import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_MOVIE_URI;

public class TodayReleaseReminder extends BroadcastReceiver {
    private final int ID_NOTIFICATION = 99;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        getRelease(context);
    }

    private void getRelease(Context context) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(date);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = BASE_URL+MOVIE+ API_KEY +"&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        //if (today.equals(movie.getString("release_date"))) {
                            Movie m = new Movie();
                            String id = String.valueOf(movie.getInt("id"));
                            m.setId_movie(id);
                            if(String.valueOf(movie.getDouble("popularity")).isEmpty()){
                                m.setPopularity("No Popularity");
                            }else {
                                m.setPopularity(String.valueOf(movie.getDouble("popularity")));
                            }
                            if(movie.getString("overview").isEmpty()){
                                m.setOverview("No Overview");
                            }else {
                                m.setOverview(movie.getString("overview"));
                            }
                            if(movie.getString("poster_path").isEmpty()){
                                m.setImg_poster("emptyPoster");
                            }else {
                                m.setImg_poster(POSTER_PATH + movie.getString("poster_path"));
                            }
                            if(movie.getString("backdrop_path").isEmpty()){
                                m.setImg_backdrop("emptyBackdrop");
                            }else {
                                m.setImg_backdrop(POSTER_PATH + movie.getString("backdrop_path"));
                            }
                            if(movie.getString("release_date").isEmpty()){
                                m.setTitle(movie.getString("title")+" "+ "(UnknownDate)");
                            }else {
                                m.setTitle(movie.getString("title")+" "+ movie.getString("release_date").substring(0,4));
                            }
                            movies.add(m);
                        //}
                    }
                    showNotification(context);
                } catch (Exception e) {
                    Log.d("Release Movie : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailureToConnect : ", error.getMessage());
            }
        });

    }

    private void showNotification(Context context) {
        int REQUEST_CODE = 112;
        String ID = "channel_02";
        String NAME = context.getString(R.string.reminder_release);
        String title = context.getString(R.string.today_release);
        String msg;
        Intent intent;
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_active);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID);

        if (movies.size() > 0) {
            intent = new Intent(context, DetailActivity.class);
            for (int i = 0; i < movies.size(); i++) {
                Uri uri = Uri.parse(CONTENT_MOVIE_URI + "/" + movies.get(i).getId_movie());
                intent.setData(uri);
                intent.putExtra(DetailActivity.EXTRA_MOVIE, movies.get(i));
                PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent).getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
                msg = movies.get(i).getTitle() + " " + context.getString(R.string.has_release);
                builder.setSmallIcon(R.drawable.ic_notifications_active)
                        .setLargeIcon(icon)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            }
        } else {
            intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent).getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
            msg = context.getString(R.string.no_release);
            builder.setSmallIcon(R.drawable.ic_notifications_active)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(ID);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        if (manager != null) {
            manager.notify(ID_NOTIFICATION, builder.build());
        }
    }

    public void activateToday(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TodayReleaseReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIFICATION, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void deactivatedToday(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TodayReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIFICATION, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
