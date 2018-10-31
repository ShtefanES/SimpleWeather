package ru.neoanon.simpleweather.utils;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.neoanon.simpleweather.model.enumerations.Lang;

/**
 * Created by eshtefan on  24.10.2018.
 */

@Module
public class LocaleModule {

    @Singleton
    @Provides
    Locale providesLocale() {
        return (Locale.getDefault().getLanguage().equals(Lang.ru.name())) ? new Locale(Lang.ru.name()) : new Locale(Lang.en.name());
    }
}
