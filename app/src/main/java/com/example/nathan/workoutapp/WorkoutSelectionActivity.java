package com.example.nathan.workoutapp;

//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Spinner;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkoutSelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        //Spinner elements
        Spinner workoutSelectorSpinner = (Spinner)findViewById(R.id.selectWorkoutSpinner);
        Spinner weekSelectorSpinner = (Spinner)findViewById(R.id.selectWeekSpinner);

        //Spinner listeners
        workoutSelectorSpinner.setOnItemSelectedListener(this);
        weekSelectorSpinner.setOnItemSelectedListener(this);

        //ArrayList of different workout programs
        ArrayList<String> workoutPrograms = new ArrayList<String>();
        workoutPrograms.add("Coan-Phillipi 10 Week Squat Routine");
        workoutPrograms.add("Coan-Phillipi 10 Week Deadlift Routine");
        workoutPrograms.add("KIZEN 6 Week Bench Program");
        workoutPrograms.add("Candito 6 Week Strength Program");

        //ArrayAdapter for workout program ArrayList
        ArrayAdapter<String> workoutProgramAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, workoutPrograms);

        workoutProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching ArrayAdapter to workout selector spinner
        workoutSelectorSpinner.setAdapter(workoutProgramAdapter);

        //Creating ArrayList of week options based on selected workout program
        String selectedProgram = workoutSelectorSpinner.getSelectedItem().toString();
        ArrayList<String> weeks = new ArrayList<String>();
        weeks.add("Week One");
        weeks.add("Week Two");
        weeks.add("Week Three");
        weeks.add("Week Four");
        weeks.add("Week Five");
        weeks.add("Week Six");
        if (selectedProgram.equals("Coan-Phillipi 10 Week Squat Routing") || selectedProgram.equals("Coan-Phillip 10 Week Deadlift Routine")){
            weeks.add("Week Seven");
            weeks.add("Week Eight");
            weeks.add("Week Nine");
            weeks.add("Week Ten");
        }

        //ArrayAdapter for weeks ArrayList
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,weeks);

        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching ArrayAdapter to week selector spinner
        weekSelectorSpinner.setAdapter(weekAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        Spinner input = (Spinner) parent;
        String selection = "";

        if (input.getId()==R.id.selectWorkoutSpinner) {
            //On selecting a workout program
            selection = parent.getItemAtPosition(position).toString();
        }
        else if (input.getId()==R.id.selectWeekSpinner){
            selection = parent.getItemAtPosition(position).toString();
        }
        //Showing selected workout program
        Toast.makeText(parent.getContext(),"Selected: " + selection, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> argo0){
        //To fill out
    }
}
