package com.task.krith.taskreminder.Database;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by krith on 07/11/16.
 */

public final class DbModule {
    public SQLiteOpenHelper provideOpenHelper(Application application) {
        return new DatabaseHelper(application);
    }

    public SqlBrite provideSqlBrite() {
        return SqlBrite.create();
    }

    public BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        return db;
    }
}
