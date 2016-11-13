package com.task.krith.taskreminder.Database;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by krith on 07/11/16.
 */
@Module
@Singleton
public class DbModule {

    private static Application application;

    public DbModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public static SQLiteOpenHelper provideOpenHelper() {
        return new DatabaseHelper(application);
    }

    @Provides
    @Singleton
    public static SqlBrite provideSqlBrite() {
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("Database").v(message);
            }
        });
    }

    @Provides
    @Singleton
    public static BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
