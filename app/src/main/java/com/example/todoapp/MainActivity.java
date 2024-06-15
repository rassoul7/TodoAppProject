package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;
    private CheckBox filterInProgress;
    private CheckBox filterCompleted;
    private CheckBox filterPending;
    private CheckBox filterCancelled;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        taskListView = findViewById(R.id.task_list);
        filterInProgress = findViewById(R.id.filter_in_progress);
        filterCompleted = findViewById(R.id.filter_completed);
        filterPending = findViewById(R.id.filter_pending);
        filterCancelled = findViewById(R.id.filter_cancelled);

        drawerLayout = findViewById(R.id.drawer_layout);
        taskListView = findViewById(R.id.task_list);

        databaseHelper = new DatabaseHelper(this);

        tasks = databaseHelper.getAllTasks();
        if (tasks.isEmpty()) {
            // Ajouter des tâches initiales si la base de données est vide
            tasks.add(new Task("Tâche 1", "Todo", "Description de la tâche 1"));
            tasks.add(new Task("Tâche 2", "In progress", "Description de la tâche 2"));
            tasks.add(new Task("Tâche 3", "Done", "Description de la tâche 3"));
            tasks.add(new Task("Tâche 4", "Bug", "Description de la tâche 4"));
            for (Task task : tasks) {
                databaseHelper.addTask(task);
            }
            tasks = databaseHelper.getAllTasks(); // Recharger les tâches depuis la base de données
        }

        taskAdapter = new TaskAdapter(this, R.layout.row_task, tasks);
        taskListView.setAdapter(taskAdapter);

        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AjouterActivity.class);
                startActivity(intent);
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = tasks.get(position);
                Intent intent = new Intent(MainActivity.this, ModifierActivity.class);
//               intent.putExtra("TASK_ID", selectedTask.getId());
                intent.putExtra("TASK_NAME", selectedTask.getName());
                intent.putExtra("TASK_STATUS", selectedTask.getStatus());
                intent.putExtra("TASK_DESCRIPTION", selectedTask.getDescription());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        tasks.addAll(databaseHelper.getAllTasks());
        taskAdapter.notifyDataSetChanged();
    }

    public void openFilter(View view) {
        drawerLayout.openDrawer(findViewById(R.id.filter_drawer));
    }

    public void applyFilters(View view) {
        List<String> selectedStatuses = new ArrayList<>();
        if (filterInProgress.isChecked()) {
            selectedStatuses.add("In progress");
        }
        if (filterCompleted.isChecked()) {
            selectedStatuses.add("Done");
        }
        if (filterPending.isChecked()) {
            selectedStatuses.add("Todo");
        }
        if (filterCancelled.isChecked()) {
            selectedStatuses.add("Bug");
        }
        taskAdapter.filterTasks(selectedStatuses);
        drawerLayout.closeDrawer(findViewById(R.id.filter_drawer));
    }


}
