package com.chappie.made.projectmovies.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.SettingsActivity;
import com.chappie.made.projectmovies.adapter.ViewPagerAdapter;
import com.chappie.made.projectmovies.fragment.favorite.FavMovieFragment;
import com.chappie.made.projectmovies.fragment.favorite.FavTvShowFragment;

import org.jetbrains.annotations.NotNull;

public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        setHasOptionsMenu(true);
        TabLayout tabLayout = v.findViewById(R.id.tab_favorite);
        ViewPager viewPager = v.findViewById(R.id.viewPagerfav);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.add(new FavMovieFragment(), getString(R.string.movies));
        pagerAdapter.add(new FavTvShowFragment(), getString(R.string.tv_shows));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

}
