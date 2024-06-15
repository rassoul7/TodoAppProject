package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    private int layoutId;
    private List<Task> tasks;
    private List<Task> filteredTasks;

    public TaskAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.tasks = new ArrayList<>(objects);
        this.filteredTasks = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutId, parent, false);
        }

        final Task task = filteredTasks.get(position);

        TextView name = row.findViewById(R.id.task_name);


        name.setText(task.getName());


        View statusIndicator = row.findViewById(R.id.status_indicator);
        View taskContainer = row.findViewById(R.id.task_container);

        int color = 0;
        switch (task.getStatus()) {
            case "Done":
                color = context.getResources().getColor(R.color.green);
                statusIndicator.setBackgroundResource(R.drawable.circle_green);
                break;
            case "Bug":
                color = context.getResources().getColor(R.color.red);
                statusIndicator.setBackgroundResource(R.drawable.circle_red);
                break;
            case "In progress":
                color = context.getResources().getColor(R.color.blue);
                statusIndicator.setBackgroundResource(R.drawable.circle_yellow);
                break;
            case "Todo":
                color = context.getResources().getColor(R.color.grey);
                statusIndicator.setBackgroundResource(R.drawable.circle_grey);
                break;
        }

        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setStroke(5, color); // 5dp de bordure
        border.setCornerRadius(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            border.setPadding(18, 18, 18, 18);
        }

        taskContainer.setBackground(border);

        // Click listener to open ModifierActivity with task details
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifierActivity.class);

                intent.putExtra("TASK_NAME", task.getName());
                intent.putExtra("TASK_DESCRIPTION", task.getDescription());
                intent.putExtra("TASK_STATUS", task.getStatus());
                context.startActivity(intent);
            }
        });

        return row;
    }

    @Override
    public int getCount() {
        return filteredTasks.size();
    }

    @Nullable
    @Override
    public Task getItem(int position) {
        return filteredTasks.get(position);
    }

    public void filterTasks(List<String> selectedStatuses) {
        filteredTasks.clear();
        if (selectedStatuses.isEmpty()) {
            filteredTasks.addAll(tasks);
        } else {
            for (Task task : tasks) {
                if (selectedStatuses.contains(task.getStatus())) {
                    filteredTasks.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }
}
