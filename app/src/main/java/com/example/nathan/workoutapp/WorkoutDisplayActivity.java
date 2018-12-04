package com.example.nathan.workoutapp;

//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.app.ActionBar;
import org.w3c.dom.Text;

public class WorkoutDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_display);

        Intent intent = getIntent();
        String workoutProgram = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_PROGRAM);
        String workoutWeek = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_WEEK);
        String workoutDay = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_DAY);
        int targetWeight = intent.getIntExtra(WorkoutSelectionActivity.TARGET_WEIGHT,0);

        TextView workoutWeekTextview = (TextView) findViewById(R.id.workoutWeek);
        workoutWeekTextview.setText(workoutWeek);

        //Setting name of action bar based on workout program
        getSupportActionBar().setTitle(workoutProgram);

        Toast.makeText(getBaseContext(),"Day " + workoutDay, Toast.LENGTH_SHORT).show();
    }
}
