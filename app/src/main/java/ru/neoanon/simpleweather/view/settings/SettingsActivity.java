package ru.neoanon.simpleweather.view.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.app.App;
import ru.neoanon.simpleweather.databinding.ActivitySettingsBinding;
import ru.neoanon.simpleweather.model.Unit;
import ru.neoanon.simpleweather.model.enumerations.PressureType;
import ru.neoanon.simpleweather.model.enumerations.TempType;
import ru.neoanon.simpleweather.view.aboutapp.AboutAppActivity;

public class SettingsActivity extends AppCompatActivity {
    @Inject
    SettingsViewModelFactory settingsViewModelFactory;

    private SettingsViewModel settingsViewModel;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Unit changedSettings;
    private RadioButton rbCelsius;
    private RadioButton rbKelvin;
    private RadioButton rbPressureMmHg;
    private RadioButton rbPressureHectopascal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        rbCelsius = binding.rbCelsius;
        rbKelvin = binding.rbKelvin;
        rbPressureMmHg = binding.rbPressureMmHg;
        rbPressureHectopascal = binding.rbPressureHectopascal;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.setting_menu_item));

        changedSettings = new Unit();

        binding.rgTemperatureType.setOnCheckedChangeListener((radioGroup, i) -> {
            if (rbCelsius.getId() == i) {
                rbCelsius.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorCheckedText));
                rbKelvin.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorUncheckedText));
                changedSettings.setTempType(TempType.celsius.name());
            } else {
                rbCelsius.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorUncheckedText));
                rbKelvin.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorCheckedText));
                changedSettings.setTempType(TempType.kelvin.name());
            }
        });

        binding.rgPressureType.setOnCheckedChangeListener((radioGroup, i) -> {
            if (rbPressureMmHg.getId() == i) {
                rbPressureMmHg.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorCheckedText));
                rbPressureHectopascal.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorUncheckedText));
                changedSettings.setPressureType(PressureType.torr.name());
            } else {
                rbPressureMmHg.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorUncheckedText));
                rbPressureHectopascal.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.colorCheckedText));
                changedSettings.setPressureType(PressureType.hectoPascal.name());
            }
        });

        binding.tvAboutApp.setOnClickListener((view -> {
            startActivity(new Intent(SettingsActivity.this, AboutAppActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }));

        ((App) getApplication()).getAppComponent().inject(this);

        settingsViewModel = ViewModelProviders.of(this, settingsViewModelFactory).get(SettingsViewModel.class);

        disposable.add(settingsViewModel.subscribeToUnits()
                .subscribe(unit -> initToggles(unit), throwable -> {
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void onBackPressed() {
        disposable.add(settingsViewModel.saveSettings(changedSettings)
                .subscribe(() -> {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }, throwable -> {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }));

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

    private void initToggles(Unit unit) {
        String temp = unit.getTempType();
        String pressure = unit.getPressureType();
        if (temp.equals(TempType.celsius.name())) {
            rbCelsius.setChecked(true);
        } else {
            rbKelvin.setChecked(true);
        }

        if (pressure.equals(PressureType.torr.name())) {
            rbPressureMmHg.setChecked(true);
        } else {
            rbPressureHectopascal.setChecked(true);
        }
    }
}
