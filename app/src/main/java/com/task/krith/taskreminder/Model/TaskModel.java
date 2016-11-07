package com.task.krith.taskreminder.Model;

import java.util.List;

/**
 * Created by krith on 05/11/16.
 */

public class TaskModel {

    private long taskId;
    private String taskName;
    private String description;
    private boolean isReminderSet;
    private String imagesUriList;
    private long reminderDateTime;
    private long createdTime;

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReminderSet() {
        return isReminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        isReminderSet = reminderSet;
    }

    public String getImagesUriList() {
        return imagesUriList;
    }

    public void setImagesUriList(String imagesUriList) {
        this.imagesUriList = imagesUriList;
    }

    public long getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(long reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }
}
