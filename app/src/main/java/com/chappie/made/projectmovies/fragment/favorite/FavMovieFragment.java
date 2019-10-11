package com.chappie.made.projectmovies.fragment.favorite;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.adapter.MovieAdapter;
import com.chappie.made.projectmovies.helper.LoadCallback;
import com.chappie.made.projectmovies.parcleable.Movie;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_MOVIE_URI;
import static com.chappie.made.projectmovies.helper.MappingHelper.mapCursorMovie;

public class FavMovieFragment extends Fragment implements LoadCallback {

    private static final String EXTRA_STATE = "EXTRA_STATE";
    private RecyclerView rvFavMovie;
    private ProgressBar pbFavMovie;
    private MovieAdapter movieAdapter;
    private ImageView emptyImg;
    private TextView tv_empty;

    public FavMovieFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fav_movie, container, false);

        emptyImg = view.findViewById(R.id.emptyMovie);
        tv_empty = view.findViewById(R.id.txt_emptyMovie);
        rvFavMovie = view.findViewById(R.id.rv_favMovie);
        pbFavMovie = view.findViewById(R.id.progressBarFavMov);
        rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver observer = new DataObserver(handler, getContext());
        if (getActivity() != null) {
            getActivity().getContentResolver().registerContentObserver(CONTENT_MOVIE_URI, true, observer);
        }
        movieAdapter = new MovieAdapter(getActivity());
        rvFavMovie.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback(this::showSelectedMovie);

        if (savedInstanceState == null) {
            new MovieAsync(getContext(), this).execute();
        } else {
            ArrayList<Movie> m = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (m != null) {
                movieAdapter.setListMovie(m);
            }
        }
        return view;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, movieAdapter.getListMovie());
    }

    @Override
    public void onResume() {
        super.onResume();
        new MovieAsync(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(()
                    -> pbFavMovie.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        pbFavMovie.setVisibility(View.GONE);
        ArrayList<Movie> movies = mapCursorMovie(cursor);
        if (movies.size() > 0) {
            empty(false);
            movieAdapter.setListMovie(movies);
        } else {
            rvFavMovie.setVisibility(View.INVISIBLE);
            empty(true);
        }
    }

    private void empty(boolean b) {
        if (b) {
            emptyImg.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            emptyImg.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
        }
    }

    private void showSelectedMovie(Movie movie) {
        Toast.makeText(getContext(), movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private static class MovieAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        private MovieAsync(Context context, LoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_MOVIE_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }
}
