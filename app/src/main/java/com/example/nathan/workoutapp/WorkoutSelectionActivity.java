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
import android.util.Log;

import java.util.ArrayList;

public class WorkoutSelectionActivity extends AppCompatActivity {
    public static final String WORKOUT_PROGRAM = "com.exmaple.nathan.workoutapp.WORKOUT_PROGRAM";
    public static final String WORKOUT_WEEK = "com.example.nathan.workoutapp.WORKOUT_WEEK";
    public static final String WORKOUT_DAY = "com.example.nathan.workoutapp.WORKOUT_DAY";
    public static final String TARGET_WEIGHT = "com.example.nathan.workoutapp.TARGET_WEIGHT";
    String workoutProgram, workoutWeek, workoutDay = "";
    int lastPosition, programPosition, targetWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        //Spinner elements
        final Spinner workoutSelectorSpinner = (Spinner)findViewById(R.id.selectWorkoutSpinner);
        final Spinner weekSelectorSpinner = (Spinner)findViewById(R.id.selectWeekSpinner);
        final Spinner daysSelectorSpinner = (Spinner)findViewById(R.id.selectDaySpinner);

        //ArrayList of different workout programs, weeks, and days
        final ArrayList<String> workoutPrograms = new ArrayList<String>();
        final ArrayList<String> weeks = new ArrayList<String>();
        final ArrayList<String> days = new ArrayList<String>();

        //Adding programs into ArrayList
        workoutPrograms.add("Coan Phillipi 10 Week Squat");
        workoutPrograms.add("Coan Phillipi 10 Week Deadlift");
        workoutPrograms.add("Kizen 6 Week Bench");
        //workoutPrograms.add("Candito 6 Week Strength Program");

        //Adding weeks into ArrayList
        weeks.add("Week 1");
        weeks.add("Week 2");
        weeks.add("Week 3");
        weeks.add("Week 4");
        weeks.add("Week 5");
        weeks.add("Week 6");
        weeks.add("Week 7");
        weeks.add("Week 8");
        weeks.add("Week 9");
        weeks.add("Week 10");
        days.add("Day 1");

        //ArrayAdapter for workout program ArrayList
        ArrayAdapter<String> workoutProgramAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, workoutPrograms);
        workoutProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching ArrayAdapter to workout selector spinner
        workoutSelectorSpinner.setAdapter(workoutProgramAdapter);

        //Modifying the week and day spinners based on the program selected
        workoutSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workoutProgram = workoutPrograms.get(i);
                lastPosition = programPosition;
                programPosition = i;
                if (i <= 1 && lastPosition > 1) {
                    weeks.add("Week 8");
                    weeks.add("Week 9");
                    weeks.add("Week 10");
                    days.remove(1);
                }
                if (i > 1){
                    if (days.size() == 1 && !(workoutWeek.equals("Week 7"))) {
                        days.add("Day 2");
                    }
                    if (weeks.size()>7) {
                        weeks.remove(9);
                        weeks.remove(8);
                        weeks.remove(7);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        //ArrayAdapter for weeks and days ArrayList
        ArrayAdapter<String> weeksAdapter = new ArrayAdapter<String>
                (WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,weeks);
        weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>
                (WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,days);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching ArrayAdapter to week and day selector spinner
        weekSelectorSpinner.setAdapter(weeksAdapter);
        daysSelectorSpinner.setAdapter(daysAdapter);

        //Storing position of week spinner
        weekSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workoutWeek = weeks.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        //Storing position of day spinner
        daysSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workoutDay = days.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

    //Opens warning dialog if user target weight input is unrealistic and dangerous
    public void openRealisticWeightDialog(){
        WeightDialog realisticWeightDialog = new WeightDialog();
        realisticWeightDialog.show(getSupportFragmentManager(),"weight_warning_dialog");
    }

    //Creates new activity for displaying exercise cards and passes through user input
    public void openWorkoutDisplayActivity(){
        Intent intent = new Intent(this, WorkoutDisplayActivity.class);
        intent.putExtra(WORKOUT_PROGRAM, workoutProgram);
        intent.putExtra(WORKOUT_WEEK,workoutWeek);
        intent.putExtra(WORKOUT_DAY, workoutDay);
        intent.putExtra(TARGET_WEIGHT,targetWeight);
        startActivity(intent);
    }
}