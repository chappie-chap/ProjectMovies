package com.chappie.made.projectmovies.fragment;


import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.SettingsActivity;
import com.chappie.made.projectmovies.adapter.MovieAdapter;
import com.chappie.made.projectmovies.parcleable.Movie;
import com.chappie.made.projectmovies.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


import static com.chappie.made.projectmovies.BuildConfig.API_KEY;
import static com.chappie.made.projectmovies.BuildConfig.BASE_URL;
import static com.chappie.made.projectmovies.BuildConfig.LANGUAGE;
import static com.chappie.made.projectmovies.BuildConfig.SEARCH_MOVIE;

public class MovieFragment extends Fragment {
    private final String KEY_STATE= "search_state";
    private ArrayList<Movie> movie = new ArrayList<>();
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private ImageView imgValue;
    private TextView tvValue;
    private MainViewModel mainViewModel;
    private SearchView searchView;
    private String mSearchQuery;


    public MovieFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        progressBar = v.findViewById(R.id.proggressBarMovie);
        RecyclerView rvMovie = v.findViewById(R.id.rv_movie);
        imgValue = v.findViewById(R.id.imgNoValue);
        tvValue = v.findViewById(R.id.txt_noValue);
        setHasOptionsMenu(true);

        movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.notifyDataSetChanged();

        getMovieData();

        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(movieAdapter);

        if(savedInstanceState!=null){
            mSearchQuery = savedInstanceState.getString(KEY_STATE);
        }

        movieAdapter.setOnItemClickCallback(this::showSelectedMovie);
    }

    private void getMovieData() {
        showLoading(true);
        mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.setListMovies();
        mainViewModel.getListMovies().observe(getActivity(), getMovie);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showSelectedMovie(Movie movie) {
        Toast.makeText(getContext(), movie.getTitle(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        searchMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchMenu(Menu menu) {
        SearchManager searchManager;
        if(getContext() != null){
            searchManager=(SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
            if (searchManager!=null){
                searchView = (SearchView) (menu.findItem(R.id.searchBar).getActionView());
                searchView.setSearchableInfo(searchManager.getSearchableInfo(Objects.requireNonNull(getActivity()).getComponentName()));
                searchView.setIconifiedByDefault(false);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.setMaxWidth(Integer.MAX_VALUE);
                searchView.setQueryHint(getString(R.string.search_movie));
                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Toast.makeText(getContext(),query,Toast.LENGTH_SHORT).show();
                        imgValue.setVisibility(View.GONE);
                        tvValue.setVisibility(View.GONE);
                            searchView.setQuery(query,false);
                            searchView.setIconified(false);
                            searchView.clearFocus();
                            String url = BASE_URL + SEARCH_MOVIE + API_KEY +LANGUAGE +"&query=" + query;
                            querySearch(url);
                            hideKeyboard(searchView);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        imgValue.setVisibility(View.GONE);
                        tvValue.setVisibility(View.GONE);
                        if(!newText.equals("")){
                            String url = BASE_URL + SEARCH_MOVIE + API_KEY +LANGUAGE +"&query=" + newText;
                            querySearch(url);
                        }
                        return true;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);

                MenuItem searchItem = menu.findItem(R.id.searchBar);
                if(mSearchQuery!=null&& !mSearchQuery.isEmpty()){
                    searchItem.expandActionView();
                    searchView.setQuery(mSearchQuery,false);
                    searchView.setFocusable(true);
                }
                searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        getMovieData();
                        return true;
                    }
                });
            }
        }
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies !=null) {
                movie.clear();
                movie.addAll(movies);
                movieAdapter.setListMovie(movie);
                showLoading(false);
                imgValue.setVisibility(View.GONE);
                tvValue.setVisibility(View.GONE);
            } else {
                showLoading(false);
                imgValue.setVisibility(View.VISIBLE);
                tvValue.setText(R.string.please_check_your_connection);
                tvValue.setVisibility(View.VISIBLE);
            }
        }
    };

    private Observer<ArrayList<Movie>> getResultMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies != null) {
                movie.clear();
                movie.addAll(movies);
                movieAdapter.setListMovie(movies);
                showLoading(false);
                imgValue.setVisibility(View.GONE);
                tvValue.setVisibility(View.GONE);
            } else {
                showLoading(false);
                imgValue.setVisibility(View.VISIBLE);
                tvValue.setText(R.string.empty);
                tvValue.setVisibility(View.VISIBLE);
            }
        }
    };

    private void querySearch(String url) {
        movieAdapter.setListMovie(movie);
        showLoading(true);
        mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.setResultMovies(url);
        mainViewModel.getResultMovies().observe(getActivity(), getResultMovies);
    }

    private void hideKeyboard(SearchView searchView) {
        if(getContext()!=null){
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(searchView.getQuery()!=null) {
            mSearchQuery = String.valueOf(searchView.getQuery());
            outState.putString(KEY_STATE, mSearchQuery);
        }
    }
}