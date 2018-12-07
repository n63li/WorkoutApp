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
import android.widget.Toast;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class WorkoutDisplayActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private CollectionReference workoutProgramsRef = db.collection("WorkoutPrograms");
    private CollectionReference workoutProgramsRef;
    private DocumentReference dbTest;
    private String workoutDocument, weekDocument, dayDocument;
    private FloatingActionButton addButton, addExercise;
    private Animation rotate_forward, rotate_backward;
    private Boolean isFABOpen = false;

    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_display);


        Intent intent = getIntent();
        String workoutProgram = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_PROGRAM);
        String workoutWeek = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_WEEK);
        String workoutDay = intent.getStringExtra(WorkoutSelectionActivity.WORKOUT_DAY);
        int targetWeight = intent.getIntExtra(WorkoutSelectionActivity.TARGET_WEIGHT,0);

        //Log.d("Week position", workoutWeek);
        //Log.d("Day position", workoutDay);

        TextView workoutWeekTextview = (TextView) findViewById(R.id.workoutWeek);
        workoutWeekTextview.setText(workoutWeek + ", " + workoutDay);

        //Setting name of action bar based on workout program
        getSupportActionBar().setTitle(workoutProgram);

        //Removing back button to display long workout program title(s)
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        workoutDocument = workoutProgram.replaceAll("\\s+","");
        weekDocument = workoutWeek.replaceAll("\\s+","");
        dayDocument = workoutDay.replaceAll("\\s+","");

        Toast.makeText(getBaseContext(),weekDocument+dayDocument, Toast.LENGTH_LONG).show();

        workoutProgramsRef = db.collection("WorkoutPrograms").document(workoutDocument)
                .collection("Weeks").document(weekDocument).collection(dayDocument);

        setUpRecyclerView();

        addButton = (FloatingActionButton) findViewById(R.id.add_button);

        //fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });


    }


    private void setUpRecyclerView() {
        Query query = workoutProgramsRef.orderBy("set", Query.Direction.ASCENDING);


        //TODO: retrieve document 'week1' from weeks collection, and retrieve hashmap day1 based on priority
        //TODO: parse through hashmap array attributes and pass them into the getter function and then the cardview
        //TODO: look at tabb code for the floating action button and dialog upon press


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

    public void animateFAB(){
        if(isFABOpen){
            addButton.startAnimation(rotate_backward);
            isFABOpen = false;
        }
        else {
            addButton.startAnimation(rotate_forward);
            isFABOpen = true;
        }

    }
}
