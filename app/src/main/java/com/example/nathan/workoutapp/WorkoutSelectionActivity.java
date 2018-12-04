package com.example.nathan.workoutapp;

//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutSelectionActivity extends AppCompatActivity {
    public static final String WORKOUT_PROGRAM = "com.exmaple.nathan.workoutapp.WORKOUT_PROGRAM";
    public static final String WORKOUT_WEEK = "com.example.nathan.workoutapp.WORKOUT_WEEK";
    public static final String WORKOUT_DAY = "com.example.nathan.workoutapp.WORKOUT_DAY";
    public static final String TARGET_WEIGHT = "com.example.nathan.workoutapp.TARGET_WEIGHT";
    String workoutProgram, workoutWeek, workoutDay = "";
    int programPosition, weekPosition, dayPosition, targetWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        //Spinner elements
        Spinner workoutSelectorSpinner = (Spinner)findViewById(R.id.selectWorkoutSpinner);
        final Spinner weekSelectorSpinner = (Spinner)findViewById(R.id.selectWeekSpinner);
        final Spinner daysSelectorSpinner = (Spinner)findViewById(R.id.selectDaySpinner);

        //ArrayList of different workout programs
        final ArrayList<String> workoutPrograms = new ArrayList<String>();
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
                //Retrieves programPosition of the spinner to determine which program is selected
                programPosition = i;
                add();
            }

            //Method to add weeks based on workout program selection to ArrayList of week options
            private void add(){
                workoutProgram = workoutPrograms.get(programPosition);

                ArrayList<String> weeks = new ArrayList<String>();
                ArrayList<String> days = new ArrayList<String>();
                weeks.add("Week One");
                weeks.add("Week Two");
                weeks.add("Week Three");
                weeks.add("Week Four");
                weeks.add("Week Five");
                weeks.add("Week Six");
                days.add("Day 1");
                if (programPosition == 0 || programPosition == 1){
                    weeks.add("Week Seven");
                    weeks.add("Week Eight");
                    weeks.add("Week Nine");
                    weeks.add("Week Ten");
                }
                if (programPosition>=2){
                    days.add("Day 2");
                }

                //ArrayAdapter for weeks and days ArrayList
                ArrayAdapter<String> weeksAdapter = new ArrayAdapter<String>(WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,weeks);
                weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,days);
                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Binding ArrayAdapter to week and day selector spinner
                weekSelectorSpinner.setAdapter(weeksAdapter);
                daysSelectorSpinner.setAdapter(daysAdapter);

                select();

                workoutWeek = weeks.get(weekPosition);
                workoutDay = weeks.get(dayPosition);
            }

            //OnItemSelectedListener for week selector spinner
            private void select(){
                weekSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getBaseContext(),"Week " + i, Toast.LENGTH_SHORT).show();
                        weekPosition = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // TODO Auto-generated method stub
                    }
                });
                daysSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getBaseContext(),"Week " + i, Toast.LENGTH_SHORT).show();
                        dayPosition = i;
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
                if (!(targetWeightInput.getText().toString().equals(""))){
                    String targetWeightString = targetWeightInput.getText().toString();
                    targetWeight = Integer.parseInt(targetWeightString);
                    //Warning to user if target weight is unrealistic
                    if(targetWeight>=700){
                        openRealisticWeightDialog();
                    }
                    else{
                        openWorkoutDisplayActivity();
                    }
                }
            }
        });
    }

    public void openRealisticWeightDialog(){
        WeightDialog realisticWeightDialog = new WeightDialog();
        realisticWeightDialog.show(getSupportFragmentManager(),"example dialog");
    }

    public void openWorkoutDisplayActivity(){
        Intent intent = new Intent(this, WorkoutDisplayActivity.class);
        intent.putExtra(WORKOUT_PROGRAM, workoutProgram);
        intent.putExtra(WORKOUT_WEEK,workoutWeek);
        intent.putExtra(WORKOUT_DAY, workoutDay);
        intent.putExtra(TARGET_WEIGHT,targetWeight);
        startActivity(intent);
    }
}