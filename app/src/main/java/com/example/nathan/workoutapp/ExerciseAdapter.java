package com.example.nathan.workoutapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseAdapter.ExerciseHolder> {

    public ExerciseAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseHolder holder, int position, @NonNull Exercise model) {
        holder.exerciseTitle.setText(model.getExercise());
        holder.sets.setText("Set " + String.valueOf(model.getSet()));
        holder.reps.setText(String.valueOf(model.getReps()) + " Reps");
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_exercise_card,
                parent,false);
        return new ExerciseHolder(v);
    }

    class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView exerciseTitle;
        TextView sets;
        TextView reps;

         public ExerciseHolder(View view){
             super(view);
             exerciseTitle = view.findViewById(R.id.exerciseTitle);
             sets = view.findViewById(R.id.setTitle);
             reps = view.findViewById(R.id.repTitle);
         }
    }
}
