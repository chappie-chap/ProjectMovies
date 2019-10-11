package com.chappie.made.favoriteapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chappie.made.favoriteapp.adapter.CastAdapter;
import com.chappie.made.favoriteapp.parcleable.Cast;
import com.chappie.made.favoriteapp.parcleable.Movie;
import com.chappie.made.favoriteapp.parcleable.TvShow;
import com.chappie.made.favoriteapp.viewmodel.MainViewModel;

import java.util.ArrayList;

import static com.chappie.made.favoriteapp.database.DatabaseContract.CONTENT_MOVIE_URI;
import static com.chappie.made.favoriteapp.database.DatabaseContract.CONTENT_TVSHOW_URI;
import static com.chappie.made.favoriteapp.helper.ContentValueHelper.getContentValueMovie;
import static com.chappie.made.favoriteapp.helper.ContentValueHelper.getContentValueTVShow;

public class DetailActivity extends AppCompatActivity {
    private static final String API_KEY = "d7d6d1eaba7b949a93086f218e71e914";
    private static final String BASE_URL ="https://api.themoviedb.org/3/";
    private ImageView img_poster, img_backdrop, addFav;
    private TextView tv_title, tv_overview, tv_popularity, tv_genre, tv_addFav;
    private Movie movie;
    private TvShow tvShow;
    private ActionBar actionBar;
    private ProgressBar pb_detail;
    private CastAdapter castAdapter;
    private MainViewModel mainViewModel;
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TVSHOW = "extra_show";
    private boolean favorite = false;
    private boolean isMovie = false;
    private Uri uri;
    private View.OnClickListener listener = view -> {
        if (!favorite) {
            if (isMovie) {
                favorite = true;
                ContentValues values = getContentValueMovie(movie);
                getContentResolver().insert(CONTENT_MOVIE_URI, values);
                Toast.makeText(this, movie.getTitle() + " " + getString(R.string.AddFav), Toast.LENGTH_SHORT).show();
                viewFavChanger();
            } else {
                favorite = true;
                ContentValues values = getContentValueTVShow(tvShow);
                getContentResolver().insert(CONTENT_TVSHOW_URI, values);
                Toast.makeText(this, tvShow.getTitle() + " " + getString(R.string.AddFav), Toast.LENGTH_SHORT).show();
                viewFavChanger();
            }
        } else {
            if (isMovie) {
                favorite = false;
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, movie.getTitle() + " " + getString(R.string.removeFav), Toast.LENGTH_SHORT).show();
                viewFavChanger();
            } else {
                favorite = false;
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, tvShow.getTitle() + " " + getString(R.string.removeFav), Toast.LENGTH_SHORT).show();
                viewFavChanger();
            }
        }
    };
    private Observer<ArrayList<Cast>> getCast = new Observer<ArrayList<Cast>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Cast> casts) {
            if (casts != null) {
                castAdapter.setCasts(casts);
                showLoading(false);
            }
        }
    };


    private Observer<ArrayList<String>> getGenre = new Observer<ArrayList<String>>() {
        @Override
        public void onChanged(ArrayList<String> movieGenres) {
            if (movieGenres != null) {
                String temps = TextUtils.join(" | ", movieGenres);
                tv_genre.setText(temps);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pb_detail = findViewById(R.id.proggressBarDetail);
        actionBar = getSupportActionBar();
        showLoading(true);
        img_poster = findViewById(R.id.dtl_poster);
        img_backdrop = findViewById(R.id.dtl_backdrop);
        addFav = findViewById(R.id.dtl_fav);
        tv_title = findViewById(R.id.dtl_title);
        tv_overview = findViewById(R.id.dtl_overview);
        tv_popularity = findViewById(R.id.dtl_popularity);
        tv_genre = findViewById(R.id.dtl_genre);
        tv_addFav = findViewById(R.id.dtl_favorite);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView rv_cast = findViewById(R.id.rv_cast);
        castAdapter = new CastAdapter(this);
        castAdapter.notifyDataSetChanged();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getGenre().observe(this, getGenre);
        mainViewModel.getCast().observe(this, getCast);


        String url;
        String url1;
        String id;
        Cursor cursor;
        if (getIntent().getParcelableExtra(EXTRA_MOVIE) != null){
            isMovie = true;
            uri = getIntent().getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    movie = new Movie(cursor);
                    cursor.close();
                }
            }
            if (movie != null) {
                favorite = true;
            } else {
                favorite = false;
                movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            }

            url = BASE_URL + "movie/" + movie.getId_movie() + "?api_key=" + API_KEY + "&language=en-US";
            url1 = BASE_URL + "movie/" + movie.getId_movie() + "/credits?api_key=" + API_KEY;
            id = movie.getTitle();
        }else{
            isMovie = false;
            uri = getIntent().getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    tvShow = new TvShow(cursor);
                    cursor.close();
                }
            }
            if (tvShow != null) {
                favorite = true;
            } else {
                favorite = false;
                tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
            }
            url = BASE_URL+"tv/" + tvShow.getId_tvshow() + "?api_key=" + API_KEY+ "&language=en-US";
            url1 = BASE_URL+"tv/" + tvShow.getId_tvshow() + "/credits?api_key=" + API_KEY;
            id = tvShow.getTitle();
        }
        viewFavChanger();
        addFav.setOnClickListener(listener);
        setDetails(id, url, url1);
        rv_cast.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_cast.setAdapter(castAdapter);


    }

    private void setDetails(String id, String url, String url1) {
        if (isMovie) {
            Glide.with(this).load(movie.getImg_poster()).into(img_poster);
            Glide.with(this).load(movie.getImg_backdrop()).into(img_backdrop);
            tv_title.setText(movie.getTitle());
            tv_popularity.setText(String.format("%s : %s", getString(R.string.popularity), movie.getPopularity()));
            tv_overview.setText(movie.getOverview());

        } else {
            Glide.with(this).load(tvShow.getImg_poster()).into(img_poster);
            Glide.with(this).load(tvShow.getImg_backdrop()).into(img_backdrop);
            tv_title.setText(tvShow.getTitle());
            tv_popularity.setText(String.format("%s : %s", getString(R.string.popularity), tvShow.getPopularity()));
            tv_overview.setText(tvShow.getOverview());
        }
        actionBar.setTitle(id);
        mainViewModel.setGenre(url);
        mainViewModel.setCast(url1);
    }

    private void showLoading(Boolean state) {
        if (state) {
            pb_detail.setVisibility(View.VISIBLE);
            actionBar.hide();
        } else {
            actionBar.show();
            pb_detail.setVisibility(View.GONE);
            img_poster.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
            img_backdrop.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_transition));
        }
    }

    private void viewFavChanger() {
        if (favorite) {
            Glide.with(this).load("").placeholder(R.drawable.ic_favorite_fill).into(addFav);
            tv_addFav.setText(getString(R.string.removeFav));
        } else {
            Glide.with(this).load("").placeholder(R.drawable.ic_favorite_border).into(addFav);
            tv_addFav.setText(getString(R.string.AddFav));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
