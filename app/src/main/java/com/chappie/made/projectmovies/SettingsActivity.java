package com.chappie.made.projectmovies;

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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chappie.made.projectmovies.helper.Pref;
import com.chappie.made.projectmovies.reminder.DailyReminder;
import com.chappie.made.projectmovies.reminder.TodayReleaseReminder;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Pref pref;
    private Switch swDaily, swToday;
    private DailyReminder dailyReminder = new DailyReminder();
    private TodayReleaseReminder todayReleaseReminder = new TodayReleaseReminder();
    private TextView txtlanguage;
    private ImageView imgDaily, imgToday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = new Pref(this);
        boolean daily = pref.getDailyPref();
        boolean today = pref.getTodayPref();

        int checked = pref.getLangPref();
        String[] lang = new String[]{getResources().getString(R.string.en), getResources().getString(R.string.in)};

        ConstraintLayout layout = findViewById(R.id.change_lang);
        swDaily = findViewById(R.id.switch_daily);
        swToday = findViewById(R.id.switch_today);
        txtlanguage = findViewById(R.id.txt_language);
        imgDaily = findViewById(R.id.stg_imgDaily);
        imgToday = findViewById(R.id.stg_todayDaily);
        txtlanguage.setText(lang[checked]);

        if(daily){
            swDaily.setChecked(daily);
            Glide.with(this)
                    .load(R.drawable.ic_notifications_active)
                    .override(30,30)
                    .into(imgDaily);
        }else {
            swDaily.setChecked(daily);
            Glide.with(this)
                    .load(R.drawable.ic_notifications_off)
                    .override(30,30)
                    .into(imgDaily);
        }
        if(today){
            swToday.setChecked(today);
            Glide.with(this)
                    .load(R.drawable.ic_notifications_active)
                    .override(30,30)
                    .into(imgToday);
        }else {
            swToday.setChecked(today);
            Glide.with(this)
                    .load(R.drawable.ic_notifications_off)
                    .override(30,30)
                    .into(imgToday);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.settings));
        }

        layout.setOnClickListener(this);
        swDaily.setOnClickListener(this);
        swToday.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.change_lang:
                changeLanguage();
                break;
            case R.id.switch_daily:
                pref.setDailyPref(swDaily.isChecked());
                boolean bool = pref.getDailyPref();
                if(bool){
                    dailyReminder.activateDaily(getApplicationContext());
                    Glide.with(this)
                            .load(R.drawable.ic_notifications_active)
                            .override(30,30)
                            .into(imgDaily);
                }else {
                    dailyReminder.deactivatedDaily(getApplicationContext());
                    Glide.with(this)
                            .load(R.drawable.ic_notifications_off)
                            .override(30,30)
                            .into(imgDaily);
                }
                break;
            case R.id.switch_today:
                pref.setTodayPref(swToday.isChecked());
                boolean bool1 = pref.getTodayPref();
                if(bool1){
                    todayReleaseReminder.activateToday(getApplicationContext());
                    Glide.with(this)
                            .load(R.drawable.ic_notifications_active)
                            .override(30,30)
                            .into(imgToday);
                }else {
                    todayReleaseReminder.deactivatedToday(getApplicationContext());
                    Glide.with(this)
                            .load(R.drawable.ic_notifications_off)
                            .override(30,30)
                            .into(imgToday);
                }
        }
    }
}
