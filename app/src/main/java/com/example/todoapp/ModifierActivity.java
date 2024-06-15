package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModifierActivity extends AppCompatActivity {
    private Spinner statusSpinner;
    private View statusIndicator;
    private EditText editTextTask;
    private EditText editTextDescription;
    private Button buttonModifier;
    private TextView statusLabel;
    private Button buttonCancel;

    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.modifier);

        statusSpinner = findViewById(R.id.statusSpinner);
        statusIndicator = findViewById(R.id.statusIndicator);
        editTextTask = findViewById(R.id.New1);
        editTextDescription = findViewById(R.id.Remplir1);
        statusLabel = findViewById(R.id.statusLabel);
        buttonCancel = findViewById(R.id.button7);
        buttonModifier = findViewById(R.id.button3);

        // Configurer le Spinner
        String[] statuses = getResources().getStringArray(R.array.status_array);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item, statuses);
        statusSpinner.setAdapter(adapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                Toast.makeText(ModifierActivity.this, "Selected: " + selectedStatus, Toast.LENGTH_SHORT).show();

                // Changer la couleur du point en fonction du statut sélectionné
                switch (selectedStatus) {
                    case "Todo":
                        statusIndicator.setBackgroundResource(R.drawable.circle_grey);
                        statusLabel.setText("To do");
                        break;
                    case "In progress":
                        statusIndicator.setBackgroundResource(R.drawable.circle_yellow);
                        statusLabel.setText("In Progress");
                        break;
                    case "Done":
                        statusIndicator.setBackgroundResource(R.drawable.circle_green);
                        statusLabel.setText("Done");
                        break;
                    case "Bug":
                        statusIndicator.setBackgroundResource(R.drawable.circle_red);
                        statusLabel.setText("Bug");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Récupérer les données passées
//        taskId = getIntent().getIntExtra("TASK_ID", -1);
        String taskName = getIntent().getStringExtra("TASK_NAME");
        String taskStatus = getIntent().getStringExtra("TASK_STATUS");
        String taskDescription = getIntent().getStringExtra("TASK_DESCRIPTION");
        DatabaseHelper db1 = new DatabaseHelper(this);
        taskId = db1.getTaskId(taskName, taskStatus, taskDescription);

        // Définir les valeurs des champs avec les données reçues
        editTextTask.setText(taskName);
        int spinnerPosition = adapter.getPosition(taskStatus);
        statusSpinner.setSelection(spinnerPosition);
        editTextDescription.setText(taskDescription);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser les champs de saisie ou effectuer d'autres actions pour annuler les modifications
                editTextTask.setText(""); // Réinitialiser le champ de saisie de tâche
                editTextDescription.setText(""); // Réinitialiser le champ de saisie de description
                statusSpinner.setSelection(spinnerPosition); // Réinitialiser la sélection du spinner
                statusIndicator.setBackgroundResource(R.drawable.circle_grey); // Réinitialiser l'indicateur de statut
                statusLabel.setText("Status"); // Réinitialiser le texte du statut
            }
        });

        buttonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs modifiées
                String newTask = editTextTask.getText().toString();
                String newStatus = statusSpinner.getSelectedItem().toString();
                String newDescription = editTextDescription.getText().toString();

                // Créer une instance de DatabaseActivity
                DatabaseActivity databaseActivity = new DatabaseActivity(ModifierActivity.this);

                // Enregistrer les modifications en appelant la méthode updateTask de DatabaseActivity
                if (databaseActivity.updateTask(taskId, newTask, newStatus, newDescription)) {
                    // Modifications enregistrées avec succès
                    Toast.makeText(ModifierActivity.this, "Modifications enregistrées !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModifierActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Une erreur s'est produite lors de l'enregistrement des modifications
                    Toast.makeText(ModifierActivity.this, "Erreur lors de l'enregistrement des modifications", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
