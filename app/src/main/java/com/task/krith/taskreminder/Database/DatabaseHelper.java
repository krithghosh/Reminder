package com.task.krith.taskreminder.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.task.krith.taskreminder.R;
import com.task.krith.taskreminder.Utilities.Toaster;

/**
 * Created by krith on 14/10/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TaskDB";
    private static final int DB_VERSION = 1;

    private final Context context;
    private static DatabaseHelper instance;
    private static SQLiteDatabase writableDb;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public SQLiteDatabase getWritableDb() {
        if ((writableDb == null) || (!writableDb.isOpen())) {
            writableDb = this.getWritableDatabase();
        } else {
            Toaster.toast(context.getString(R.string.db_not_writing));
        }
        return writableDb;
    }

    @Override
    public void close() {
        super.close();
        if (writableDb != null) {
            writableDb.close();
            writableDb = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TaskTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
