package ru.neoanon.simpleweather.view.aboutapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;

import ru.neoanon.simpleweather.BuildConfig;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutAppBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about_app);

        binding.tvVersionAppAndVersionBuild.setText(getString(R.string.version_app_and_version_build, BuildConfig.VERSION_NAME, String.valueOf(BuildConfig.VERSION_CODE)));
        binding.tvSourceWeathers.setMovementMethod(LinkMovementMethod.getInstance());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.about_application));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
