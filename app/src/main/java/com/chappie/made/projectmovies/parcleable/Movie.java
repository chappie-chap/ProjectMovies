package com.chappie.made.projectmovies.parcleable;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.chappie.made.projectmovies.database.DatabaseContract;

import static com.chappie.made.projectmovies.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    private String id_movie, title, popularity, overview,img_poster,img_backdrop;

    public Movie(String id_movie, String title, String popularity, String overview, String img_poster, String img_backdrop) {
        this.id_movie = id_movie;
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.img_poster = img_poster;
        this.img_backdrop = img_backdrop;
    }

    public Movie(Cursor cursor){
        this.id_movie = getColumnString(cursor, DatabaseContract.TableColumns._ID);
        this.title = getColumnString(cursor, DatabaseContract.TableColumns.TITLE);
        this.popularity = getColumnString(cursor, DatabaseContract.TableColumns.POPULARITY);
        this.overview = getColumnString(cursor, DatabaseContract.TableColumns.OVERVIEW);
        this.img_poster = getColumnString(cursor, DatabaseContract.TableColumns.POSTER);
        this.img_backdrop = getColumnString(cursor, DatabaseContract.TableColumns.BACKDROP);
    }

    public String getId_movie() {
        return id_movie;
    }

    public void setId_movie(String id_movie) {
        this.id_movie = id_movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImg_poster() {
        return img_poster;
    }

    public void setImg_poster(String img_poster) {
        this.img_poster = img_poster;
    }

    public String getImg_backdrop() {
        return img_backdrop;
    }

    public void setImg_backdrop(String img_backdrop) {
        this.img_backdrop = img_backdrop;
    }

    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_movie);
        dest.writeString(this.title);
        dest.writeString(this.popularity);
        dest.writeString(this.overview);
        dest.writeString(this.img_poster);
        dest.writeString(this.img_backdrop);
    }

    protected Movie(Parcel in) {
        this.id_movie = in.readString();
        this.title = in.readString();
        this.popularity = in.readString();
        this.overview = in.readString();
        this.img_poster = in.readString();
        this.img_backdrop = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}