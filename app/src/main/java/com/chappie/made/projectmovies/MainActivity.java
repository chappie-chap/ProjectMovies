package com.chappie.made.projectmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.chappie.made.projectmovies.fragment.FavoriteFragment;
import com.chappie.made.projectmovies.fragment.MovieFragment;
import com.chappie.made.projectmovies.fragment.TvShowFragment;

public class MainActivity extends AppCompatActivity {

    private boolean doublePress = false;
    private ActionBar actionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            actionBar.setDisplayHomeAsUpEnabled(false);
            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    fragment = new MovieFragment();
                    actionBar.setTitle(R.string.movies);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tvshows:
                    actionBar.setTitle(R.string.tv_shows);
                    fragment = new TvShowFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_favorite:
                    actionBar.setTitle(R.string.favorite);
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.item_frame,fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState==null){
            navView.setSelectedItemId(R.id.navigation_movies);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doublePress = false;
    }

    @Override
    public void onBackPressed() {
        if (doublePress) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
        this.doublePress = true;
        Toast.makeText(MainActivity.this, R.string.press_exit, Toast.LENGTH_SHORT).show();
    }
}
