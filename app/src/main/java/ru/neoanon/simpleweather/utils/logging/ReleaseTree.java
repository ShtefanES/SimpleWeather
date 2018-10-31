package ru.neoanon.simpleweather.utils.logging;

import android.support.annotation.NonNull;

import timber.log.Timber;

import static android.util.Log.ERROR;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(final int priority, final String tag, @NonNull final String message, final Throwable throwable) {
        if (priority == ERROR && throwable != null) {
            //TODO add connect to crash monitoring system, for example Crashlytics
        }
    }
}
