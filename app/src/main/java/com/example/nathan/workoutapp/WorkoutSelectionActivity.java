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
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;

public class WorkoutSelectionActivity extends AppCompatActivity {
    public static final String WORKOUT_PROGRAM = "com.exmaple.nathan.workoutapp.WORKOUT_PROGRAM";
    public static final String WORKOUT_WEEK = "com.example.nathan.workoutapp.WORKOUT_WEEK";
    public static final String WORKOUT_DAY = "com.example.nathan.workoutapp.WORKOUT_DAY";
    public static final String TARGET_WEIGHT = "com.example.nathan.workoutapp.TARGET_WEIGHT";
    String workoutProgram, workoutWeek, workoutDay = "";
    int lastPosition, programPosition, weekPosition, dayPosition, targetWeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        Log.d("start position", "will i come back in here?");

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

        /*
        //Workout selector spinner listener
        workoutSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){

                //Retrieves programPosition of the spinner to determine which program is selected
                lastPosition = programPosition;
                programPosition = i;
                if (i<=1 & lastPosition >1) {
                    weeks.add("Week 8");
                    weeks.add("Week 9");
                    weeks.add("Week 10");
                }
                if (i>1){
                    Log.d("testing", String.valueOf(weeks.size()));
                    if(weeks.size()>7) {
                        weeks.remove(9);
                        weeks.remove(8);
                        weeks.remove(7);
                    }
                }

                //ArrayAdapter for weeks and days ArrayList
                ArrayAdapter<String> weeksAdapter = new ArrayAdapter<String>
                        (WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,weeks);
                weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>
                        (WorkoutSelectionActivity.this, android.R.layout.simple_spinner_dropdown_item,days);
                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Binding ArrayAdapter to week and day selector spinner
                weekSelectorSpinner.setAdapter(weeksAdapter);
                daysSelectorSpinner.setAdapter(daysAdapter);

                select();

                workoutWeek = weeks.get(weekPosition);
                workoutDay = days.get(dayPosition);
            }

            //Method to add weeks based on workout program selection to ArrayList of week options
            /*
            private void add(){
                workoutProgram = workoutPrograms.get(programPosition);
                weeks.add("Week 8");
                weeks.add("Week 9");
                weeks.add("Week 10");
                if (programPosition>=2){
                    //days.add("Day 2");
                }
            }
            */
            /*
            //OnItemSelectedListener for week selector spinner
            private void select(){
                weekSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getBaseContext(),"Week " + i, Toast.LENGTH_SHORT).show();
                        weekPosition = i;
                        Log.d("week selection position", String.valueOf(weekPosition));
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
                        Log.d("day selection position", String.valueOf(dayPosition));
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

        */

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

    @Override
    public void onResume(){
        super.onResume();
        Log.d("resume position", "DId i get here?");
    }

    public void openRealisticWeightDialog(){
        WeightDialog realisticWeightDialog = new WeightDialog();
        realisticWeightDialog.show(getSupportFragmentManager(),"weight_warning_dialog");
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