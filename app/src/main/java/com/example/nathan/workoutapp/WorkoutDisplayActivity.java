package com.example.nathan.workoutapp;

//Imports
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.lang.Math;


public class WorkoutDisplayActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workoutProgramsRef;
    private String workoutDocument, weekDocument, dayDocument;
    private FloatingActionButton addButton, addExercise;

    //Animations
    private Animation rotate_forward, rotate_backward, fab_open, fab_close;

    //FAB status
    //false -> fab = closed
    private Boolean isFABOpen = false;

    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_display);

        //Getting user inputs from the selection activity
        Intent intent = getIntent();
        String workoutProgram = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_PROGRAM);
        String workoutWeek = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_WEEK);
        String workoutDay = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_DAY);
        int targetWeight = intent.getIntExtra(WorkoutSelectionActivity.TARGET_WEIGHT,0);

        //Setting header text
        TextView workoutWeekTextview = (TextView) findViewById(R.id.workoutWeek);
        workoutWeekTextview.setText(workoutWeek + ", " + workoutDay);

        //Setting name of action bar based on workout program
        getSupportActionBar().setTitle(workoutProgram);

        //Removing back button to display long workout program title(s)
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Creating document IDs for Firebase from user input
        workoutDocument = workoutProgram.replaceAll("\\s+","");
        weekDocument = workoutWeek.replaceAll("\\s+","");
        dayDocument = workoutDay.replaceAll("\\s+","");

        //Firebase CollectionReference
        workoutProgramsRef = db.collection("WorkoutPrograms").document(workoutDocument)
                .collection("Weeks").document(weekDocument).collection(dayDocument);

        //Updating weights based on user input
        calculateWeight(workoutDocument, weekDocument, dayDocument, targetWeight);

        setUpRecyclerView();

        //Floating action buttons
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addExercise = (FloatingActionButton) findViewById(R.id.add_exercise);

        //Animations
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        addButton.setOnClickListener(this);
        addExercise.setOnClickListener(this);
    }

    //Determining animations for the two FABs
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button:
                animateFAB();
                break;
            case R.id.add_exercise:
                addExercise();
                break;
        }
    }

    //Setting up the recycler view for the exercise cards
    private void setUpRecyclerView() {
        Query query = workoutProgramsRef.orderBy("set", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query, Exercise.class)
                .build();

        adapter = new ExerciseAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Adding swiping functionality to workout cards
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Don't want to actually delete workouts from database, just swipe off screen when completed
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    //FAB animations
    public void animateFAB() {
        if (isFABOpen) {
            addButton.startAnimation(rotate_backward);
            addExercise.startAnimation(fab_close);
            addExercise.setClickable(false);
            isFABOpen = false;
        } else {
            addButton.startAnimation(rotate_forward);
            addExercise.startAnimation(fab_open);
            addExercise.setClickable(true);
            isFABOpen = true;
        }
    }

    //Launches a dialog to enter a new exercise
    public void addExercise(){
        AddExerciseDialog addExerciseDialog = new AddExerciseDialog();
        addExerciseDialog.show(getSupportFragmentManager(), "add_exercise_dialog");
    }

    //Calculates the weights for each set based on the user input
    public void calculateWeight(String workoutDocument, String weekDocument, String dayDocument, int targetWeight){
        if (workoutDocument.equals("CoanPhillipi10WeekDeadlift") || workoutDocument.equals("CoanPhillipi10WeekSquat")){
            double firstSet = 0;
            double restOfSets, accessorySets;
            String key = "";
            //Code for updating Coan Phillipi 10 Week Deadlift Program document values
            if (workoutDocument.contains("Deadlift")){
                switch(weekDocument){
                    case "Week1":
                        firstSet = 5*Math.ceil((targetWeight*0.75)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.6)/5);
                        for (int i = 0; i < 8; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week2":
                        firstSet = 5*Math.ceil((targetWeight*0.8)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.65)/5);
                        for (int i = 0; i < 8; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week3":
                        firstSet = 5*Math.ceil((targetWeight*0.85)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 6; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week4":
                        firstSet = 5*Math.ceil((targetWeight*0.9)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.75)/5);
                        for (int i = 0; i < 5; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week5":
                        firstSet = 5*Math.ceil((targetWeight*0.8)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.65)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.575)/5);
                        for (int i = 0; i<5; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            if (i < 2) {
                                updateWeight(key, firstSet);
                            } else {
                                updateWeight(key, restOfSets);
                            }
                        }
                        for (int i = 5; i < 8; i++){
                            key = "ShrugsSet" + String.valueOf(i+2);
                            updateWeight(key,accessorySets);
                        }
                        break;
                    case "Week6":
                        firstSet = 5*Math.ceil((targetWeight*0.85)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.62)/5);
                        for (int i = 0; i < 6; i++){
                            if (i < 3){
                                key = "DeadliftSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "ShrugsSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week7":
                        firstSet = 5*Math.ceil((targetWeight*0.9)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.75)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.6725)/5);
                        for (int i = 0; i < 5; i++){
                            if (i < 3){
                                key = "DeadliftSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "ShrugsSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week8":
                        firstSet = 5*Math.ceil((targetWeight*0.95)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 5; i++){
                            if (i < 3){
                                key = "DeadliftSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "ShrugsSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week9":
                        firstSet = 5*Math.ceil((targetWeight*0.975)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 4; i++){
                            if (i < 2){
                                key = "DeadliftSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "ShrugsSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week10":
                        firstSet = 5*Math.ceil((targetWeight)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.6)/5);
                        for (int i = 0; i < 2; i++){
                            key = "DeadliftSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                }
                updateWeight("DeadliftSet1", firstSet);
            }

            // Code for updating Coan Phillipi 10 Week Squat Program document values
            else {
                switch(weekDocument){
                    case "Week1":
                        firstSet = 5*Math.ceil((targetWeight*0.75)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.6)/5);
                        for (int i = 0; i < 8; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week2":
                        firstSet = 5*Math.ceil((targetWeight*0.8)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.65)/5);
                        for (int i = 0; i < 8; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week3":
                        firstSet = 5*Math.ceil((targetWeight*0.85)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 6; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week4":
                        firstSet = 5*Math.ceil((targetWeight*0.9)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.75)/5);
                        for (int i = 0; i < 5; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                        break;
                    case "Week5":
                        firstSet = 5*Math.ceil((targetWeight*0.8)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.65)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.575)/5);
                        for (int i = 0; i<5; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            if (i < 2) {
                                updateWeight(key, firstSet);
                            } else {
                                updateWeight(key, restOfSets);
                            }
                        }
                        for (int i = 5; i < 8; i++){
                            key = "SingleLegSquatSet" + String.valueOf(i+2);
                            updateWeight(key,accessorySets);
                        }
                        break;
                    case "Week6":
                        firstSet = 5*Math.ceil((targetWeight*0.85)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.62)/5);
                        for (int i = 0; i < 6; i++){
                            if (i < 3){
                                key = "SquatSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "SingleLegSquatSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week7":
                        firstSet = 5*Math.ceil((targetWeight*0.9)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.75)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.6725)/5);
                        for (int i = 0; i < 5; i++){
                            if (i < 3){
                                key = "SquatSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "SingleLegSquatSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week8":
                        firstSet = 5*Math.ceil((targetWeight*0.95)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 5; i++){
                            if (i < 3){
                                key = "SquatSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "SingleLegSquatSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week9":
                        firstSet = 5*Math.ceil((targetWeight*0.975)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.7)/5);
                        accessorySets = 5*Math.ceil((targetWeight*0.7)/5);
                        for (int i = 0; i < 4; i++){
                            if (i < 2){
                                key = "SquatSet" + String.valueOf(i+2);
                                updateWeight(key, restOfSets);
                            } else{
                                key = "SingleLegSquatSet" + String.valueOf(i+2);
                                updateWeight(key, accessorySets);
                            }
                        }
                        break;
                    case "Week10":
                        firstSet = 5*Math.ceil((targetWeight)/5);
                        restOfSets = 5*Math.ceil((targetWeight*0.6)/5);
                        for (int i = 0; i < 2; i++){
                            key = "SquatSet" + String.valueOf(i+2);
                            updateWeight(key, restOfSets);
                        }
                }
                updateWeight("SquatSet1", firstSet);
            }
        }

        //Code for updating Kizen 6 Week Bench Program document values
        else{
            double benchSet, overheadPressSet = 0;
            double dumbbellInclineSet = 0.2*targetWeight;
            double rowsSet = dumbbellInclineSet;
            String key;
            switch(weekDocument){
                case "Week1":
                    if (dayDocument.equals("Day1")){
                        benchSet = targetWeight*0.775;
                        for (int i = 0; i < 11; i++){
                            if (i < 8){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else{
                                key = "DumbbellInclineSet" + String.valueOf(i-7);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.65;
                        overheadPressSet = targetWeight*0.7;
                        for (int i = 0; i < 10; i++){
                            if (i < 4){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i >= 4 && i < 7){
                                key = "OverheadPressSet" + String.valueOf(i-3);
                                updateWeight(key, overheadPressSet);
                            }
                            else {
                                key = "RowsSet" + String.valueOf(i-6);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week2":
                    if (dayDocument.equals("Day1")){
                        benchSet = targetWeight*0.8;
                        for (int i = 0; i < 9; i++){
                            if (i < 6){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else{
                                key = "DumbbellInclineSet" + String.valueOf(i-7);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.7;
                        overheadPressSet = targetWeight*0.7;
                        for (int i = 0; i < 10; i++){
                            if (i < 3){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i >= 3 && i < 7){
                                key = "OverheadPressSet" + String.valueOf(i-2);
                                updateWeight(key, overheadPressSet);
                            }
                            else {
                                key = "RowsSet" + String.valueOf(i-6);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week3":
                    if (dayDocument.equals("Day1")){
                        benchSet = targetWeight*0.825;
                        for (int i = 0; i < 8; i++){
                            if (i < 5){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else{
                                key = "DumbbellInclineSet" + String.valueOf(i-4);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.725;
                        overheadPressSet = targetWeight*0.7;
                        for (int i = 0; i < 12; i++){
                            if (i < 4){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i >= 4 && i < 9){
                                key = "OverheadPressSet" + String.valueOf(i-3);
                                updateWeight(key, overheadPressSet);
                            }
                            else {
                                key = "RowsSet" + String.valueOf(i-8);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week4":
                    if (dayDocument.equals("Day1")){
                        benchSet = targetWeight*0.85;
                        for (int i = 0; i < 8; i++){
                            if (i < 5){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else{
                                key = "DumbbellInclineSet" + String.valueOf(i-4);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.75;
                        overheadPressSet = targetWeight*0.725;
                        for (int i = 0; i < 10; i++){
                            if (i < 4){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i >= 4 && i < 7){
                                key = "OverheadPressSet" + String.valueOf(i-3);
                                updateWeight(key, overheadPressSet);
                            }
                            else {
                                key = "RowsSet" + String.valueOf(i-6);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week5":
                    if (dayDocument.equals("Day1")){
                        benchSet = targetWeight*0.875;
                        for (int i = 0; i < 9; i++){
                            if (i < 6){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else{
                                key = "DumbbellInclineSet" + String.valueOf(i-7);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.775;
                        overheadPressSet = targetWeight*0.725;
                        for (int i = 0; i < 10; i++){
                            if (i < 3){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i >= 3 && i < 7){
                                key = "OverheadPressSet" + String.valueOf(i-2);
                                updateWeight(key, overheadPressSet);
                            }
                            else {
                                key = "RowsSet" + String.valueOf(i-6);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week6":
                    if (dayDocument.equals("Day1")){
                        for (int i = 0; i < 5; i++){
                            if (i == 0){
                                benchSet = targetWeight * 0.9;
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else if (i == 1){
                                benchSet = targetWeight*0.95;
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            }
                            else{
                                key = "DumbbellInclineSet" + String.valueOf(i-1);
                                updateWeight(key, dumbbellInclineSet);
                            }
                        }
                    } else{
                        benchSet = targetWeight*0.6;
                        for (int i = 0; i < 6; i++){
                            if (i < 3){
                                key = "BenchPressSet" + String.valueOf(i+1);
                                updateWeight(key, benchSet);
                            } else {
                                key = "RowsSet" + String.valueOf(i-6);
                                updateWeight(key, rowsSet);
                            }
                        }
                    }
                    break;
                case "Week7":
                    for (int i = 0; i < 3; i++){
                            if (i == 0) {
                                benchSet = targetWeight * 0.9;
                            }
                            else if (i == 1){
                                benchSet = targetWeight * 0.95;
                            }
                            else{
                                benchSet = targetWeight;
                            }
                        key = "BenchPressSet" + String.valueOf(i+1);
                        updateWeight(key, benchSet);
                    }
                    break;
            }
        }
    }

    //Updates Firebase based on the weight calculation
    public void updateWeight(String exerciseDocument, double weightToBeUpdated){
        DocumentReference exerciseRef = workoutProgramsRef.document(exerciseDocument);

        exerciseRef.update("weight", weightToBeUpdated)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Updating weight", "Weight successfully updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Updating weight", "Error updating weight", e);
                    }
                });
    }
}
