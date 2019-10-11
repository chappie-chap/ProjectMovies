package com.chappie.made.favoriteapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chappie.made.favoriteapp.helper.Pref;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Pref pref;
    private TextView txtlanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = new Pref(this);
        int checked = pref.getLangPref();
        String[] lang = new String[]{getResources().getString(R.string.en), getResources().getString(R.string.in)};

        ConstraintLayout layout = findViewById(R.id.change_lang);
        txtlanguage = findViewById(R.id.txt_language);
        txtlanguage.setText(lang[checked]);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.settings));
        }

        layout.setOnClickListener(this);

    }

    private void changeLanguage() {
        int checked = pref.getLangPref();
        String[] lang = new String[]{getResources().getString(R.string.en), getResources().getString(R.string.in)};
        final int[] choose = new int[1];

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.change_language));
        alert.setSingleChoiceItems(lang, checked, (dialogInterface, which) -> choose[0] = which);
        alert.setPositiveButton(getResources().getString(R.string.change), (dialog, which) -> {
            setLocale(choose[0]);
            pref.setLangPref(choose[0]);
            txtlanguage.setText(lang[choose[0]]);
            dialog.dismiss();
            refresh();
        });
        alert.setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void setLocale(int i) {
        String[] lang = new String[]{"en", "in"};
        String language = lang[i];
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    private void refresh() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_lang) {
            changeLanguage();
        }
    }
}
