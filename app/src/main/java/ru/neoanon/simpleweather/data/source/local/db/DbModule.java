package ru.neoanon.simpleweather.data.source.local.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Module
public class DbModule {

    @Singleton
    @Provides
    IDbSource providesDbSource(Context appContext) {
        return new DbSource(Room.databaseBuilder(appContext, AppDatabase.class, "weather-database")
                .fallbackToDestructiveMigration()
                .build());
    }
}
