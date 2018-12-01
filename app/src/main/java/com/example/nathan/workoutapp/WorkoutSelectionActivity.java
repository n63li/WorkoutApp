package com.example.nathan.workoutapp;

//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkoutSelectionActivity extends AppCompatActivity {
    int position;
    int targetWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        //Log.i("test","does this work");

        //Spinner elements
        Spinner workoutSelectorSpinner = (Spinner)findViewById(R.id.selectWorkoutSpinner);
        final Spinner weekSelectorSpinner = (Spinner)findViewById(R.id.selectWeekSpinner);

        //ArrayList of different workout programs
        ArrayList<String> workoutPrograms = new ArrayList<String>();
        workoutPrograms.add("Coan-Phillipi 10 Week Squat Routine");
        workoutPrograms.add("Coan-Phillipi 10 Week Deadlift Routine");
        workoutPrograms.add("KIZEN 6 Week Bench Program");
        workoutPrograms.add("Candito 6 Week Strength Program");

        //ArrayAdapter for workout program ArrayList
        ArrayAdapter<String> workoutProgramAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workoutPrograms);
        workoutProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching ArrayAdapter to workout selector spinner
        workoutSelectorSpinner.setAdapter(workoutProgramAdapter);

        //Workout selector spinner listener
        workoutSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                //Retrieves position of the spinner to determine which program is selected
                position = i;
                add();
            }

            //Method to add weeks based on workout program selection to ArrayList of week options
            private void add(){
                //Toast.makeText(getBaseContext(), "" + position, Toast.LENGTH_LONG).show();

                ArrayList<String> weeks = new ArrayList<String>();
                weeks.add("Week One");
                weeks.add("Week Two");
                weeks.add("Week Three");
                weeks.add("Week Four");
                weeks.add("Week Five");
                weeks.add("Week Six");
                if (position == 0 || position == 1){
                    weeks.add("Week Seven");
                    weeks.add("Week Eight");
                    weeks.add("Week Nine");
                    weeks.add("Week Ten");
                }

                //ArrayAdapter for weeks ArrayList
                ArrayAdapter<String> weeksAdapter = new ArrayAdapter<String>(WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,weeks);
                weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Binding ArrayAdapter to week selector spinner
                weekSelectorSpinner.setAdapter(weeksAdapter);

                select();
            }

            //OnItemSelectedListener for week selector spinner
            private void select(){
                weekSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getBaseContext(),"Week " + i, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // TODO Auto-generated method stub
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                // TODO Auto-generated method stub
            }
        });

        //Reaction to submit button press
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //Retrieve target weight entered by user
                EditText targetWeightInput = (EditText) findViewById(R.id.TargetWeightInput);
                if (targetWeightInput.getText()!= null){
                    String targetWeightString = targetWeightInput.getText().toString();
                    targetWeight = Integer.parseInt(targetWeightString);
                    //Warning to user if target weight is unrealistic
                    if(targetWeight>=700){
                        openRealisticWeightDialog();
                    }
                }
            }
        });
    }

    public void openRealisticWeightDialog(){
        WeightDialog realisticWeightDialog = new WeightDialog();
        realisticWeightDialog.show(getSupportFragmentManager(),"example dialog");
    }
}