package com.example.todoapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private String[] statuses;
    private Context context;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] statuses) {
        super(context, resource, statuses);
        this.statuses = statuses;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        TextView statusText = view.findViewById(R.id.statusText);
        View statusColor = view.findViewById(R.id.statusColor);

        String status = statuses[position];
        statusText.setText(status);

        switch (status) {
            case "Todo":
                statusColor.setBackgroundResource(R.drawable.circle_grey);
                break;
            case "In progress":
                statusColor.setBackgroundResource(R.drawable.circle_yellow);
                break;
            case "Done":
                statusColor.setBackgroundResource(R.drawable.circle_green);
                break;
            case "Bug":
                statusColor.setBackgroundResource(R.drawable.circle_red);
                break;
        }

        return view;
    }
}
// ça c'est juste pour que ça affiche les couleurs quand je veux choisir le  statut d'une tâche
