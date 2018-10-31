package ru.neoanon.simpleweather.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by eshtefan on  19.09.2018.
 */

@Module
public class AppModule {
    private Application application;

    public AppModule(Application app) {
        this.application = app;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return application.getApplicationContext();
    }
}
