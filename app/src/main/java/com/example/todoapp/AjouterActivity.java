package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AjouterActivity extends AppCompatActivity {

    private Spinner statusSpinner;
    private View statusIndicator;
    private EditText editTextTask;
    private EditText editTextDescription;
    private TextView statusLabel;
    private View buttonAjouter;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter);

        statusSpinner = findViewById(R.id.statusSpinner);
        statusIndicator = findViewById(R.id.statusIndicator);
        editTextTask = findViewById(R.id.New1);
        editTextDescription = findViewById(R.id.Remplir1);
        statusLabel = findViewById(R.id.statusLabel);
        buttonCancel = findViewById(R.id.button7);
        buttonAjouter = findViewById(R.id.button3);

        // Configurer le Spinner
        String[] statuses = getResources().getStringArray(R.array.status_array);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item, statuses);
        statusSpinner.setAdapter(adapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                Toast.makeText(AjouterActivity.this, "Selected: " + selectedStatus, Toast.LENGTH_SHORT).show();

                // Changer la couleur du point en fonction du statut sélectionné
                switch (selectedStatus) {
                    case "Todo":
                        statusIndicator.setBackgroundResource(R.drawable.circle_grey);
                        statusLabel.setText("Todo");
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

        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                String description = editTextDescription.getText().toString();
                String status = statusSpinner.getSelectedItem().toString();

                DatabaseActivity db = new DatabaseActivity((Context) AjouterActivity.this);
                if (db.insertTask(task, description, status)) {
                    Toast.makeText(AjouterActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AjouterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AjouterActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser les champs de saisie ou effectuer d'autres actions pour annuler les modifications
                editTextTask.setText(""); // Réinitialiser le champ de saisie de tâche
                editTextDescription.setText(""); // Réinitialiser le champ de saisie de description
                statusSpinner.setSelection(0); // Réinitialiser la sélection du spinner
                statusIndicator.setBackgroundResource(R.drawable.circle_grey); // Réinitialiser l'indicateur de statut
                statusLabel.setText("Status"); // Réinitialiser le texte du statut
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
