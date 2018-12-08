package com.example.nathan.workoutapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

//Dialog for creating a new exercise within a workout
public class AddExerciseDialog extends AppCompatDialogFragment {
    private EditText editTextExercise, editTextReps, editTextWeight;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_exercise_dialog, null);

        builder.setView(view)
                .setTitle("Add Exercise")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        editTextExercise = view.findViewById(R.id.addExerciseType);
        editTextReps = view.findViewById(R.id.addReps);
        editTextWeight = view.findViewById(R.id.addWeight);

        return builder.create();
    }
}
