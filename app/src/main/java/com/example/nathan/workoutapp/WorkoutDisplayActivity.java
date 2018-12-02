package com.example.nathan.workoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.w3c.dom.Text;

public class WorkoutDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_display);

        Intent intent = getIntent();
        String workoutProgram = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_PROGRAM);
        String workoutWeek = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_WEEK);
        int targetWeight = intent.getIntExtra(WorkoutSelectionActivity.TARGET_WEIGHT,0);

        TextView workoutProgramTextview = (TextView) findViewById(R.id.workoutProgram);
        TextView workoutWeekTextview = (TextView) findViewById(R.id.workoutWeek);
        workoutProgramTextview.setText(workoutProgram);
        workoutWeekTextview.setText(workoutWeek);
        Toast.makeText(getBaseContext(),"Week " + targetWeight, Toast.LENGTH_SHORT).show();
    }
}
