package ru.neoanon.simpleweather.view.mainscreen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.app.App;
import ru.neoanon.simpleweather.databinding.ActivityMainBinding;
import ru.neoanon.simpleweather.view.places.PlacesFragment;
import ru.neoanon.simpleweather.view.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable disposable = new CompositeDisposable();

    private DrawerLayout drawerLayout;
    private WeatherViewModel weatherViewModel;

    @Inject
    WeatherViewModelFactory weatherViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = binding.drawerLayout;
        ((App) getApplication()).getAppComponent().inject(this);

        commitFragment(R.id.main_content_frame, new MainFragment());
        commitFragment(R.id.navig_content_frame, new PlacesFragment());
        Toolbar toolbar = binding.toolbarContainer.toolbar;
        setSupportActionBar(toolbar);
        initNavigationDrawer(drawerLayout, toolbar);

        weatherViewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable.add(weatherViewModel.subscribeSelectedRegionName()
                .subscribe(regionName -> getSupportActionBar().setTitle(regionName)));

        disposable.add(weatherViewModel.getCloseDrawerSub()
                .subscribe(o -> drawerLayout.closeDrawer(GravityCompat.START)));
    }


    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            weatherViewModel.closeDrawer();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigationDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    void commitFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(containerViewId, fragment);
        fTrans.commit();
    }
}
