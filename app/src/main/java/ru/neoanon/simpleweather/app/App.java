package ru.neoanon.simpleweather.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

import ru.neoanon.simpleweather.BuildConfig;
import ru.neoanon.simpleweather.utils.logging.ReleaseTree;
import timber.log.Timber;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
