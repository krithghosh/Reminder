package com.task.krith.taskreminder;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.task.krith.taskreminder.Database.DbModule;
import com.task.krith.taskreminder.Utilities.Toaster;

import timber.log.Timber;

/**
 * Created by krith on 05/11/16.
 */

public class TaskApplication extends Application {

    private static TaskComponent taskComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReporting());
        }
        Toaster.init(this);
        taskComponent = DaggerTaskComponent.builder()
                .taskModule(new TaskModule(this))
                .dbModule(new DbModule(this))
                .build();
    }

    public static TaskComponent getComponent() {
        return taskComponent;
    }

    private static class CrashReporting extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            CrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    CrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    CrashLibrary.logWarning(t);
                }
            }
        }
    }
}
