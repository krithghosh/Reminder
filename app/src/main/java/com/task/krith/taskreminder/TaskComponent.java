package com.task.krith.taskreminder;

import com.task.krith.taskreminder.Activity.AddEditTask;
import com.task.krith.taskreminder.Activity.TaskList;
import com.task.krith.taskreminder.Database.DbModule;
import com.task.krith.taskreminder.Database.TaskTable;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by krith on 12/11/16.
 */

@Singleton
@Component(modules = {TaskModule.class, DbModule.class})
public interface TaskComponent {

    void inject(TaskApplication taskApplication);

    void inject(TaskList taskList);

    void inject(AddEditTask addEditTask);
}
