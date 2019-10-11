package com.chappie.made.projectmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.chappie.made.projectmovies.parcleable.Cast;
import com.chappie.made.projectmovies.parcleable.Movie;
import com.chappie.made.projectmovies.parcleable.TvShow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.chappie.made.projectmovies.BuildConfig.API_KEY;
import static com.chappie.made.projectmovies.BuildConfig.BASE_URL;
import static com.chappie.made.projectmovies.BuildConfig.LANGUAGE;
import static com.chappie.made.projectmovies.BuildConfig.MOVIE;
import static com.chappie.made.projectmovies.BuildConfig.POSTER_PATH;
import static com.chappie.made.projectmovies.BuildConfig.TVSHOW;

public class MainViewModel extends ViewModel {
    private static final String MOVIE_URL = BASE_URL+ MOVIE+ API_KEY+ LANGUAGE;
    private static final String TVSHOW_URL = BASE_URL+ TVSHOW+ API_KEY+ LANGUAGE;
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> resultMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> listTv = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> resultTv = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Cast>> listCast = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> movieGenres = new MutableLiveData<>();


    public  void  setListMovies(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        client.get(MOVIE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list  = responseObject.getJSONArray("results");
                    for ( int i=0; i<list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        Movie movie1= new Movie();
                        String id = String.valueOf(movie.getInt("id"));
                        movie1.setId_movie(id);
                        if(String.valueOf(movie.getDouble("popularity")).isEmpty()){
                            movie1.setPopularity("No Popularity");
                        }else {
                            movie1.setPopularity(String.valueOf(movie.getDouble("popularity")));
                        }
                        if(movie.getString("overview").isEmpty()){
                            movie1.setOverview("No Overview");
                        }else {
                            movie1.setOverview(movie.getString("overview"));
                        }
                        if(movie.getString("poster_path").isEmpty()){
                            movie1.setImg_poster("emptyPoster");
                        }else {
                            movie1.setImg_poster(POSTER_PATH + movie.getString("poster_path"));
                        }
                        if(movie.getString("backdrop_path").isEmpty()){
                            movie1.setImg_backdrop("emptyBackdrop");
                        }else {
                            movie1.setImg_backdrop(POSTER_PATH + movie.getString("backdrop_path"));
                        }
                        if(movie.getString("release_date").isEmpty()){
                            movie1.setTitle(movie.getString("title")+" "+ "(UnknownDate)");
                        }else {
                            movie1.setTitle(movie.getString("title")+" "+ movie.getString("release_date").substring(0,4));
                        }
                            listItems.add(movie1);
                    }
                    listMovies.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("View Model Movie : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure Movie : ", error.getMessage());
                listMovies.postValue(listItems);
            }
        });
    }

    public LiveData<ArrayList<Movie>> getListMovies(){
        return listMovies;
    }

    public  void  setResultMovies(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list  = responseObject.getJSONArray("results");
                    for ( int i=0; i<list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        Movie movie1= new Movie();
                        String id = String.valueOf(movie.getInt("id"));
                        movie1.setId_movie(id);
                        if(String.valueOf(movie.getDouble("popularity")).isEmpty()){
                            movie1.setPopularity("No Popularity");
                        }else {
                            movie1.setPopularity(String.valueOf(movie.getDouble("popularity")));
                        }
                        if(movie.getString("overview").isEmpty()){
                            movie1.setOverview("No Overview");
                        }else {
                            movie1.setOverview(movie.getString("overview"));
                        }
                        if(movie.getString("poster_path").isEmpty()){
                            movie1.setImg_poster("emptyPoster");
                        }else {
                            movie1.setImg_poster(POSTER_PATH + movie.getString("poster_path"));
                        }
                        if(movie.getString("backdrop_path").isEmpty()){
                            movie1.setImg_backdrop("emptyBackdrop");
                        }else {
                            movie1.setImg_backdrop(POSTER_PATH + movie.getString("backdrop_path"));
                        }
                        if(movie.getString("release_date").isEmpty()){
                            movie1.setTitle(movie.getString("title")+" "+ "(UnknownDate)");
                        }else {
                            movie1.setTitle(movie.getString("title")+" "+ movie.getString("release_date").substring(0,4));
                        }
                        listItems.add(movie1);
                    }
                    resultMovies.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("Model Search Movie : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure SearchM : ", error.getMessage());
                listMovies.postValue(listItems);
            }
        });
    }

    public LiveData<ArrayList<Movie>> getResultMovies(){
        return resultMovies;
    }

    public void setListTv() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        client.get(TVSHOW_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        TvShow tvShow = new TvShow();
                        String id = String.valueOf(tv.getInt("id"));
                        tvShow.setId_tvshow(id);
                        if(String.valueOf(tv.getDouble("popularity")).isEmpty()){
                            tvShow.setPopularity("No Popularity");
                        }else {
                            tvShow.setPopularity(String.valueOf(tv.getDouble("popularity")));
                        }
                        if(tv.getString("overview").isEmpty()){
                            tvShow.setOverview("No Overview");
                        }else {
                            tvShow.setOverview(tv.getString("overview"));
                        }
                        if(tv.getString("poster_path").isEmpty()){
                            tvShow.setImg_poster("emptyPoster");
                        }else {
                            tvShow.setImg_poster(POSTER_PATH + tv.getString("poster_path"));
                        }
                        if(tv.getString("backdrop_path").isEmpty()){
                            tvShow.setImg_backdrop("emptyBackdrop");
                        }else {
                            tvShow.setImg_backdrop(POSTER_PATH + tv.getString("backdrop_path"));
                        }
                        if(tv.getString("first_air_date").isEmpty()){
                            tvShow.setTitle(tv.getString("name")+" "+ "(UnknownDate)");
                        }else {
                            tvShow.setTitle(tv.getString("name")+" "+ tv.getString("first_air_date").substring(0,4));
                        }
                        listItems.add(tvShow);
                    }
                    listTv.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("View Model Tv : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure Tv : ", error.getMessage());
                listTv.postValue(listItems);
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getListTv() {
        return listTv;
    }

    public void setResultTv(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        TvShow tvShow = new TvShow();
                        String id = String.valueOf(tv.getInt("id"));
                        tvShow.setId_tvshow(id);
                        if(String.valueOf(tv.getDouble("popularity")).isEmpty()){
                            tvShow.setPopularity("No Popularity");
                        }else {
                            tvShow.setPopularity(String.valueOf(tv.getDouble("popularity")));
                        }
                        if(tv.getString("overview").isEmpty()){
                            tvShow.setOverview("No Overview");
                        }else {
                            tvShow.setOverview(tv.getString("overview"));
                        }
                        if(tv.getString("poster_path").isEmpty()){
                            tvShow.setImg_poster("emptyPoster");
                        }else {
                            tvShow.setImg_poster(POSTER_PATH + tv.getString("poster_path"));
                        }
                        if(tv.getString("backdrop_path").isEmpty()){
                            tvShow.setImg_backdrop("emptyBackdrop");
                        }else {
                            tvShow.setImg_backdrop(POSTER_PATH + tv.getString("backdrop_path"));
                        }
                        if(tv.getString("first_air_date").isEmpty()){
                            tvShow.setTitle(tv.getString("name")+" "+ "(UnknownDate)");
                        }else {
                            tvShow.setTitle(tv.getString("name")+" "+ tv.getString("first_air_date").substring(0,4));
                        }
                        listItems.add(tvShow);
                    }
                    resultTv.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("Model Search Tv : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure SearchTv : ", error.getMessage());
                listTv.postValue(listItems);
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getResultTv() {
        return resultTv;
    }

    public void setGenre(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<String> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("genres");
                    for (int i=0; i<list.length(); i++){
                        JSONObject genre = list.getJSONObject(i);
                        listItems.add(genre.getString("name"));
                    }
                    movieGenres.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("View Model Genre : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure Genre : ", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<String>> getGenre() {
        return movieGenres;
    }

    public void setCast(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Cast> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("cast");
                    for (int i=0; i<list.length(); i++){
                        JSONObject cast = list.getJSONObject(i);
                        Cast cast1 = new Cast();
                        cast1.setImg_cast(POSTER_PATH + cast.getString("profile_path"));
                        cast1.setRole(cast.getString("character"));
                        cast1.setName(cast.getString("name"));
                        listItems.add(cast1);
                    }
                    listCast.postValue(listItems);
                } catch (JSONException e) {
                    Log.d("View Model Cast : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure Cast : ", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Cast>> getCast() {
        return listCast;
    }

}
