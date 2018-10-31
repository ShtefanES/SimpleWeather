package ru.neoanon.simpleweather.data.source.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.neoanon.simpleweather.BuildConfig;

/**
 * Created by eshtefan on  19.09.2018.
 */

@Module
public class OwmApiModule {

    @Singleton
    @Provides
    OpenWeatherMap providesOwmApi() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.OWM_API_URL)
                .build();
        return retrofit.create(OpenWeatherMap.class);
    }
}
