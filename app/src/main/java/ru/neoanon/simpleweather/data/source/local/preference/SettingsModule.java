package ru.neoanon.simpleweather.data.source.local.preference;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Module
public class SettingsModule {
    private static final String MAIN_PREFERENCES = "mainPreferences";

    @Singleton
    @Provides
    ISettings providesSettings(Context appContext) {
        return new Settings(appContext.getSharedPreferences(MAIN_PREFERENCES, MODE_PRIVATE));
    }
}
