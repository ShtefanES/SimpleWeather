package ru.neoanon.simpleweather.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.neoanon.simpleweather.data.source.geodata.LocationModule;
import ru.neoanon.simpleweather.utils.LocaleModule;
import ru.neoanon.simpleweather.view.detailedforecast.ForecastPagerFragment;
import ru.neoanon.simpleweather.view.mainscreen.MainActivity;
import ru.neoanon.simpleweather.data.source.local.db.DbModule;
import ru.neoanon.simpleweather.data.source.local.preference.SettingsModule;
import ru.neoanon.simpleweather.data.source.remote.OwmApiModule;
import ru.neoanon.simpleweather.view.mainscreen.MainFragment;
import ru.neoanon.simpleweather.view.places.PlacesFragment;
import ru.neoanon.simpleweather.view.settings.SettingsActivity;

/**
 * Created by eshtefan on  19.09.2018.
 */

@Singleton
@Component(modules = {AppModule.class, OwmApiModule.class, SettingsModule.class, DbModule.class, LocationModule.class, LocaleModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(PlacesFragment placesFragment);

    void inject(MainFragment mainFragment);

    void inject(ForecastPagerFragment forecastPagerFragment);

    void inject(SettingsActivity settingsActivity);
}
