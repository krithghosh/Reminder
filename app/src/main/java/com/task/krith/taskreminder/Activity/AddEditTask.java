package com.task.krith.taskreminder.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.sqlbrite.BriteDatabase;
import com.task.krith.taskreminder.Database.Db;
import com.task.krith.taskreminder.Adapters.ImageAdapter;
import com.task.krith.taskreminder.R;
import com.task.krith.taskreminder.Model.TaskModel;
import com.task.krith.taskreminder.Database.TaskTable;
import com.task.krith.taskreminder.Utilities.Toaster;
import com.task.krith.taskreminder.Utilities.ActivityUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @BindView(R.id.camera_iv)
    ImageView cameraIv;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.task_name)
    EditText taskName;

    @BindView(R.id.reminder_btn)
    Button reminderBtn;

    @BindView(R.id.save_btn)
    Button saveBtn;

    @Inject
    BriteDatabase db;

    Calendar reminderCalendar = null;

    Context context;

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private int imageTask = 0;
    private String type = null;
    private TaskModel taskObj;
    private List<String> imageList = new ArrayList<String>();
    private boolean isDateSet;
    private boolean isTimeSet;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        ButterKnife.bind(this);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Task");
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color));
        taskObj = new TaskModel();
        isDateSet = false;
        isTimeSet = false;

        imageAdapter = new ImageAdapter(imageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageAdapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        onNewIntent(getIntent());
        /*if (getIntent().hasExtra("taskid")) {

        }*/
    }

    @OnClick(R.id.camera_iv)
    public void onClickCamera() {
        selectImage();
    }

    @OnClick(R.id.reminder_btn)
    public void onClickReminderDateBtn() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddEditTask.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(Calendar.getInstance());
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.save_btn)
    public void onClickSaveBtn() {
        try {
            if (getIntent().hasExtra("taskid")) {
                if (!taskObj.getTaskName().equals(taskName.getText().toString())
                        || !taskObj.getDescription().equals(description.getText().toString())
                        || !checkForImageList()
                        || taskObj.getReminderDateTime() != reminderCalendar.getTimeInMillis()) {
                    if (taskName.getText().length() <= 0 || description.getText().length() <= 0) {
                        Toaster.toast("Please fill all the details");
                    } else {
                        taskObj.setTaskName(taskName.getText().toString());
                        taskObj.setDescription(description.getText().toString());
                        if (imageList.size() != 0) {
                            JSONObject json = new JSONObject();
                            json.put("imageArray", new JSONArray(imageList));
                            String arrayList = json.toString();
                            taskObj.setImagesUriList(arrayList);
                        } else {
                            taskObj.setImagesUriList(null);
                        }
                        if (taskObj.getReminderDateTime() != reminderCalendar.getTimeInMillis()) {
                            taskObj.setReminderDateTime(reminderCalendar.getTimeInMillis());
                        }
                        updateData(taskObj);
                    }
                } else {
                    Toaster.toast("Everything is Already updated");
                }
            } else {
                if (taskName.getText().length() != 0 && description.getText().length() != 0 && isDateSet && isTimeSet) {
                    taskObj.setTaskName(taskName.getText().toString());
                    if (description.getText().length() != 0)
                        taskObj.setDescription(description.getText().toString());
                    if (imageList.size() != 0) {
                        JSONObject json = new JSONObject();
                        json.put("imageArray", new JSONArray(imageList));
                        String arrayList = json.toString();
                        taskObj.setImagesUriList(arrayList);
                    } else {
                        taskObj.setImagesUriList(null);
                    }
                    taskObj.setReminderDateTime(reminderCalendar.getTimeInMillis());
                    taskObj.setReminderSet(true);
                    long timeInMillis = System.currentTimeMillis();
                    taskObj.setTaskId(timeInMillis);
                    taskObj.setCreatedTime(timeInMillis);
                    insertData(taskObj);
                } else if (taskName.getText().length() != 0 && description.getText().length() != 0) {
                    taskObj.setTaskName(taskName.getText().toString());
                    if (description.getText().length() != 0)
                        taskObj.setDescription(description.getText().toString());
                    if (imageList.size() != 0) {
                        JSONObject json = new JSONObject();
                        json.put("imageArray", new JSONArray(imageList));
                        String arrayList = json.toString();
                        taskObj.setImagesUriList(arrayList);
                    }
                    taskObj.setReminderSet(false);
                    long timeInMillis = System.currentTimeMillis();
                    taskObj.setTaskId(timeInMillis);
                    taskObj.setCreatedTime(timeInMillis);
                    insertData(taskObj);
                } else {
                    Toaster.toast("Please fill all the details");
                }
            }
        } catch (Exception e) {
            Toaster.toast("Please fill all the details");
        }
    }

    public boolean checkForImageList() {
        try {
            if (imageList.size() != 0) {
                JSONObject json = new JSONObject();
                json.put("imageArray", new JSONArray(imageList));
                String arrayList = json.toString();
                return arrayList.equals(taskObj.getImagesUriList());
            }
            return true;
        } catch (Exception e) {
            Toaster.toast("Error");
        }
        return true;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ActivityUtils.checkPermission(context);
                switch (item) {
                    case REQUEST_CAMERA:
                        imageTask = item;
                        if (result)
                            cameraIntent();
                        break;

                    /*case SELECT_FILE:
                        imageTask = item;
                        if (result)
                            galleryIntent();
                        break;*/
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType(getString(R.string.set_image_type));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ActivityUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (imageTask == REQUEST_CAMERA)
                        cameraIntent();
                    else if (imageTask == SELECT_FILE)
                        galleryIntent();
                } else {
                    Toaster.toast("Permission Denied");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            String realPath = "";
            try {
                if (requestCode == SELECT_FILE) {
                    realPath = ActivityUtils.getRealPathFromURI_API19(getApplicationContext(), data.getData());
                    imageList.add(realPath);
                    imageAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CAMERA) {
                    imageList.add(ActivityUtils.convertCameraImageToBitmap(data));
                    imageAdapter.notifyDataSetChanged();
                } else {
                    Toaster.toast("There seems to be some problem");
                }
            } catch (Exception e) {
                Toaster.toast("There seems to be some problem");
            }
            return;
        } else {
            Toaster.toast("There seems to be some problem");
        }
    }

    public void insertData(TaskModel obj) throws UnsupportedEncodingException {

        if (!TaskTable.insertTask(obj))
            Toaster.toast("Please try adding later, there is some problem");
        else {
            Toaster.toast("Saved");
            ActivityUtils.setNotification(getApplicationContext(),
                    obj.getTaskId(), obj.getTaskName(), obj.getReminderDateTime());
            startActivity(new Intent(getApplicationContext(), TaskList.class));
        }
    }

    public void updateData(TaskModel obj) throws UnsupportedEncodingException {

        if (!TaskTable.updateTask(obj))
            Toaster.toast("Please try adding later, there is some problem");
        else {
            Toaster.toast("Updated");
            ActivityUtils.setNotification(getApplicationContext(),
                    obj.getTaskId(), obj.getTaskName(), obj.getReminderDateTime());
            startActivity(new Intent(getApplicationContext(), TaskList.class));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        reminderCalendar = calendar;
        isDateSet = true;
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AddEditTask.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reminderCalendar.set(Calendar.MINUTE, minute);
        reminderCalendar.set(Calendar.SECOND, second);
        isTimeSet = true;

        Date time = reminderCalendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a");
        reminderBtn.setText(sdf.format(time));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TaskList.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("taskid")) {
                saveBtn.setText("UPDATE");
                long taskId = getIntent().getLongExtra("taskid", 0);
                String[] tableColumns = new String[]{
                        TaskTable.ID,
                        TaskTable.TASK_NAME,
                        TaskTable.TASK_DESCRIPTION,
                        TaskTable.CREATED_TIME,
                        TaskTable.IS_REMINDER_SET,
                        TaskTable.IMAGES_URI_LIST,
                        TaskTable.REMINDER_DATE_TIME
                };
                String whereClause = TaskTable.ID + "=?";
                String[] whereArgs = new String[]{
                        String.valueOf(taskId)
                };
                try {
                    Cursor cursor = TaskTable.getTask(tableColumns, whereClause, whereArgs);
                    if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                        taskObj.setTaskId(Db.getLong(cursor, TaskTable.ID));
                        taskObj.setTaskName(Db.getString(cursor, TaskTable.TASK_NAME));
                        taskObj.setCreatedTime(Db.getLong(cursor, TaskTable.CREATED_TIME));
                        taskObj.setDescription(Db.getString(cursor, TaskTable.TASK_DESCRIPTION));
                        taskObj.setReminderSet(Db.getBoolean(cursor, TaskTable.IS_REMINDER_SET));
                        taskObj.setImagesUriList(Db.getString(cursor, TaskTable.IMAGES_URI_LIST));
                        taskObj.setReminderDateTime(Db.getLong(cursor, TaskTable.REMINDER_DATE_TIME));
                        taskName.setText(taskObj.getTaskName());
                        description.setText(taskObj.getDescription());
                        if (Db.getString(cursor, TaskTable.IMAGES_URI_LIST) != null) {
                            JSONObject json = new JSONObject(Db.getString(cursor, TaskTable.IMAGES_URI_LIST));
                            JSONArray jsonArray = json.getJSONArray("imageArray");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                imageList.add(jsonArray.get(i).toString());
                            }
                            imageAdapter.notifyDataSetChanged();
                        }
                        if (Db.getBoolean(cursor, TaskTable.IS_REMINDER_SET)) {
                            long _time = Db.getLong(cursor, TaskTable.REMINDER_DATE_TIME);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(_time);
                            Date time = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a");
                            reminderBtn.setText(sdf.format(time));
                            reminderCalendar = calendar;
                            isDateSet = true;
                            isTimeSet = true;
                        }
                    }
                    cursor.close();
                } catch (Exception e) {
                    Log.d("Error", "" + e);
                    Toaster.toast("Db cursor error");
                }
            }
        }
    }
}
