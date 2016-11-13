package com.task.krith.taskreminder;

/**
 * Created by krith on 12/11/16.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.task.krith.taskreminder.Database.DbModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskModule {

    private final Application application;

    public TaskModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
