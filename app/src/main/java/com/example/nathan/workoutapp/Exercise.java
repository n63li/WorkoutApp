package com.example.nathan.workoutapp;

public class Exercise {

    private String exercise;
    private int set;
    private int reps;
    //private double oneRM;
    //private int RPE;
    private int weight;

    public Exercise(){
        //empty constructor needed
    }

    public Exercise(String exercise, int set, int reps /*, double oneRM, int RPE */, int weight){
        this.exercise = exercise;
        this.set = set;
        this.reps = reps;
        //this.oneRM = oneRM;
        //this.RPE = RPE;
        this.weight = weight;
    }

    public String getExercise() {
        return exercise;
    }

    public int getSet() {
        return set;
    }

    public int getReps() {
        return reps;
    }

    /*
    public double getOneRM() {
        return oneRM;
    }

    public int getRPE() {
        return RPE;
    }
    */

    public int getWeight() {
        return weight;
    }
}
