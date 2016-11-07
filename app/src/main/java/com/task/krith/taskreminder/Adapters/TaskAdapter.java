package com.task.krith.taskreminder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.task.krith.taskreminder.R;
import com.task.krith.taskreminder.Model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by krith on 06/11/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Action1<List<TaskModel>> {

    private List<TaskModel> taskList;
    OnItemClickListener mItemClickListener;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        TaskModel taskobj = taskList.get(position);
        holder.taskName.setText(taskobj.getTaskName());
        if (taskobj.isReminderSet())
            holder.reminderTime.setText(getCalendarDate(taskobj.getReminderDateTime()));
        else
            holder.reminderTime.setText(getCalendarDate(taskobj.getCreatedTime()));
    }

    public String getCalendarDate(long timeInMillis) {
        String _time = "";
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        calendar.setTimeInMillis(timeInMillis);
        if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            Date time = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(", hh:mm a");
            _time = "Today " + sdf.format(time);
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == tomorrow.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH) == tomorrow.get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR)) {
            Date time = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(", hh:mm a");
            _time = "Tomorrow " + sdf.format(time);
        } else {
            Date time = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a");
            _time = sdf.format(time);
        }
        return _time;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void call(List<TaskModel> taskList) {
        this.taskList = taskList;
        Collections.sort(this.taskList, new SortComparator());
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView taskName;
        public TextView reminderTime;

        public TaskViewHolder(View view) {
            super(view);
            taskName = (TextView) view.findViewById(R.id.task_name);
            reminderTime = (TextView) view.findViewById(R.id.reminder_time);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view, getAdapterPosition(), taskList.get(getAdapterPosition()).getTaskId());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, Long id);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class SortComparator implements Comparator<TaskModel> {
        @Override
        public int compare(TaskModel o1, TaskModel o2) {
            long _o1 = 0;
            long _o2 = 0;
            if (o1.isReminderSet())
                _o1 = o1.getReminderDateTime();
            else
                _o1 = o1.getCreatedTime();

            if (o2.isReminderSet())
                _o2 = o2.getReminderDateTime();
            else
                _o2 = o2.getCreatedTime();

            if (_o1 < _o2) {
                return -1;
            } else if (_o1 > _o2) {
                return 1;
            }
            return 0;
        }
    }
}
