package com.task.krith.taskreminder.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.task.krith.taskreminder.TaskApp;
import com.task.krith.taskreminder.Model.TaskModel;

import rx.functions.Func1;

/**
 * Created by krith on 05/11/16.
 */

public class TaskTable {

    static SQLiteDatabase db;

    public static final String TABLE = "Task";
    public static final String ID = "id";
    public static final String CREATED_TIME = "created_time";
    public static final String TASK_NAME = "name";
    public static final String TASK_DESCRIPTION = "description";
    public static final String IS_REMINDER_SET = "is_reminder_set";
    public static final String IMAGES_URI_LIST = "images_uri_list";
    public static final String REMINDER_DATE_TIME = "reminder_date_time";
    public static final String QUERY_TASK = "SELECT " + ID + ", " + CREATED_TIME + ", " + TASK_NAME + ", " + TASK_DESCRIPTION + ", " + IS_REMINDER_SET + ", " + IMAGES_URI_LIST + ", " + REMINDER_DATE_TIME + " FROM " + TABLE + " WHERE " + ID + "=";
    public static final String QUERY_ALL_TASK = "SELECT " + ID + ", " + CREATED_TIME + ", " + TASK_NAME + ", " + TASK_DESCRIPTION + ", " + IS_REMINDER_SET + ", " + IMAGES_URI_LIST + ", " + REMINDER_DATE_TIME + " FROM " + TABLE;

    /*

    Inserting data into db from ArrayList :

    JSONObject json = new JSONObject();
    json.put("uniqueArrays", new JSONArray(items));
    String arrayList = json.toString();

    To read data from the json :

    JSONObject json = new JSONObject(stringreadfromsqlite);
    ArrayList items = json.optJSONArray("uniqueArrays");

    */

    public static final String CREATE_TABLE = ""
            + "CREATE TABLE " + TaskTable.TABLE + "("
            + TaskTable.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TaskTable.TASK_NAME + " TEXT NOT NULL,"
            + TaskTable.CREATED_TIME + " INTEGER NOT NULL,"
            + TaskTable.TASK_DESCRIPTION + " TEXT NOT NULL,"
            + TaskTable.IS_REMINDER_SET + " BOOLEAN NOT NULL DEFAULT FALSE,"
            + TaskTable.IMAGES_URI_LIST + " TEXT DEFAULT NULL,"
            + TaskTable.REMINDER_DATE_TIME + " INTEGER DEFAULT 0"
            + ")";

    public static final class Builder {

        private final ContentValues contentValues = new ContentValues();

        public Builder setId(long id) {
            contentValues.put(ID, id);
            return this;
        }

        public Builder setCreatedTime(long createdTime) {
            contentValues.put(CREATED_TIME, createdTime);
            return this;
        }

        public Builder setName(String name) {
            contentValues.put(TASK_NAME, name);
            return this;
        }

        public Builder setDescription(String description) {
            contentValues.put(TASK_DESCRIPTION, description);
            return this;
        }

        public Builder setIsReminderSet(boolean isReminderSet) {
            contentValues.put(IS_REMINDER_SET, isReminderSet);
            return this;
        }

        public Builder setImagesUriList(String imagesUriList) {
            contentValues.put(IMAGES_URI_LIST, imagesUriList);
            return this;
        }

        public Builder setReminderDateTime(long reminderDateTime) {
            contentValues.put(REMINDER_DATE_TIME, reminderDateTime);
            return this;
        }

        public ContentValues build() {
            return contentValues;
        }

    }

    public static Func1<Cursor, TaskModel> MAPPER = new Func1<Cursor, TaskModel>() {

        @Override
        public TaskModel call(Cursor cursor) {

            TaskModel taskObj = new TaskModel();
            taskObj.setTaskId(Db.getLong(cursor, ID));
            taskObj.setCreatedTime(Db.getLong(cursor, CREATED_TIME));
            taskObj.setTaskName(Db.getString(cursor, TASK_NAME));
            taskObj.setDescription(Db.getString(cursor, TASK_DESCRIPTION));
            taskObj.setReminderSet(Db.getBoolean(cursor, IS_REMINDER_SET));
            taskObj.setImagesUriList(Db.getString(cursor, IMAGES_URI_LIST));
            taskObj.setReminderDateTime(Db.getLong(cursor, REMINDER_DATE_TIME));
            return taskObj;
        }
    };

    public static Boolean insertTask(TaskModel obj) {
        db = DatabaseHelper.getInstance(TaskApp.getInstance()).getWritableDb();
        long insertRowId = db.insert(TABLE, null, new TaskTable.Builder()
                .setCreatedTime(obj.getTaskId())
                .setDescription(obj.getDescription())
                .setId(obj.getTaskId())
                .setImagesUriList(obj.getImagesUriList())
                .setIsReminderSet(obj.isReminderSet())
                .setName(obj.getTaskName())
                .setReminderDateTime(obj.getReminderDateTime())
                .build());
        if (insertRowId == -1)
            return false;
        return true;
    }

    public static Boolean updateTask(TaskModel obj) {
        db = DatabaseHelper.getInstance(TaskApp.getInstance()).getWritableDb();
        long updateRow = db.update(TABLE, new TaskTable.Builder()
                .setCreatedTime(System.currentTimeMillis())
                .setDescription(obj.getDescription())
                .setImagesUriList(obj.getImagesUriList())
                .setIsReminderSet(obj.isReminderSet())
                .setName(obj.getTaskName())
                .setReminderDateTime(obj.getReminderDateTime())
                .build(), ID + "=?", new String[]{String.valueOf(obj.getTaskId())});
        if (updateRow <= 0)
            return false;
        return true;
    }

    public static Cursor getTask(String[] tableColumns, String whereClause, String[] whereArgs) {
        db = DatabaseHelper.getInstance(TaskApp.getInstance()).getReadableDatabase();
        Cursor cursor = db.query(TABLE, tableColumns, whereClause, whereArgs, null, null, null);
        return cursor;
    }
}
