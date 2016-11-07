package com.task.krith.taskreminder;

import android.app.Application;

import com.squareup.sqlbrite.BriteDatabase;
import com.task.krith.taskreminder.Database.DatabaseHelper;
import com.task.krith.taskreminder.Database.DbModule;
import com.task.krith.taskreminder.Utilities.Toaster;

/**
 * Created by krith on 05/11/16.
 */

public class TaskApp extends Application {

    public static TaskApp instance;
    public static BriteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.initializeInstance();
    }

    public void initializeInstance() {
        DatabaseHelper.getInstance(instance.getApplicationContext());
        Toaster.init(instance.getApplicationContext());
        DbModule dbModule = new DbModule();
        db = dbModule.provideDatabase(dbModule.provideSqlBrite(),
                dbModule.provideOpenHelper(instance));
    }

    public static TaskApp getInstance() {
        return instance;
    }

    public static BriteDatabase getDb() {
        return db;
    }
}
