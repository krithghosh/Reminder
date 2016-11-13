package com.task.krith.taskreminder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.sqlbrite.BriteDatabase;
import com.task.krith.taskreminder.R;
import com.task.krith.taskreminder.Adapters.TaskAdapter;
import com.task.krith.taskreminder.SharedPreferenceManager;
import com.task.krith.taskreminder.TaskApplication;
import com.task.krith.taskreminder.Model.TaskModel;
import com.task.krith.taskreminder.Database.TaskTable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TaskList extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    BriteDatabase db;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    private Subscription subscription;

    private TaskAdapter taskAdapter;
    private List<TaskModel> taskList = new ArrayList<TaskModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TaskApplication) getApplicationContext()).getComponent().inject(this);
        if (SharedPreferenceManager.sharedPreferences != null) {
            Timber.tag("shared pref").v("not empty");
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color));
        taskAdapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(taskAdapter);

        taskAdapter.SetOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Long id) {
                Intent intent = new Intent(getApplicationContext(), AddEditTask.class)
                        .putExtra("taskid", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String query = TaskTable.QUERY_ALL_TASK;
        String table = TaskTable.TABLE;
        subscription = db.createQuery(table, query)
                .mapToList(TaskTable.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskAdapter);
    }

    @OnClick(R.id.fab)
    public void onClickFab() {
        startActivity(new Intent(getApplicationContext(), AddEditTask.class));
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}