package com.chappie.made.favoriteapp.fragment;


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

import com.chappie.made.favoriteapp.R;
import com.chappie.made.favoriteapp.adapter.TvShowAdapter;
import com.chappie.made.favoriteapp.helper.LoadCallback;
import com.chappie.made.favoriteapp.parcleable.TvShow;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.chappie.made.favoriteapp.database.DatabaseContract.CONTENT_TVSHOW_URI;
import static com.chappie.made.favoriteapp.helper.MappingHelper.mapCursorTvShow;

public class FavTvShowFragment extends Fragment implements LoadCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private RecyclerView rvFavTvShow;
    private ProgressBar pbFavTv;
    private TvShowAdapter tvShowAdapter;
    private ImageView emptyImg;
    private TextView tv_empty;

    public FavTvShowFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fav_tv_show, container, false);

        emptyImg = view.findViewById(R.id.emptyTv);
        tv_empty = view.findViewById(R.id.txt_emptyTv);
        rvFavTvShow = view.findViewById(R.id.rv_favTv);
        pbFavTv = view.findViewById(R.id.progressBarFavTv);
        rvFavTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver observer = new DataObserver(handler, getContext());
        if (getActivity() != null) {
            getActivity().getContentResolver().registerContentObserver(CONTENT_TVSHOW_URI, true, observer);
        }
        tvShowAdapter = new TvShowAdapter(getActivity());
        rvFavTvShow.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnItemClickCallback(this::showSelectedTV);

        if (savedInstanceState == null) {
            new TvAsync(getContext(), this).execute();
        } else {
            ArrayList<TvShow> tv = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (tv != null) {
                tvShowAdapter.setListTv(tv);
            }
        }
        return view;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, tvShowAdapter.getlistTv());
    }

    @Override
    public void onResume() {
        super.onResume();
        new TvAsync(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(()
                    -> pbFavTv.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        pbFavTv.setVisibility(View.GONE);
        ArrayList<TvShow> tvshows = mapCursorTvShow(cursor);
        if (tvshows.size() > 0) {
            empty(false);
            tvShowAdapter.setListTv(tvshows);
        } else {
            rvFavTvShow.setVisibility(View.INVISIBLE);
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

    private void showSelectedTV(TvShow tv) {
        Toast.makeText(getContext(), tv.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private static class TvAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        private TvAsync(Context context, LoadCallback callback) {
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
            return context.getContentResolver().query(CONTENT_TVSHOW_URI, null, null, null, null);
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
