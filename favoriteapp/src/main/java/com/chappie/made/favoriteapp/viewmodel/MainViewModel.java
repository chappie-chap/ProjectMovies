package com.chappie.made.favoriteapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.chappie.made.favoriteapp.parcleable.Cast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Cast>> listCast = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> movieGenres = new MutableLiveData<>();

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
                        cast1.setImg_cast("https://image.tmdb.org/t/p/original" + cast.getString("profile_path"));
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
