package com.example.nathan.workoutapp;

public class Exercise {
    /*
    private String workoutType;
    private int sets;
    private int reps;
    private double oneRM;
    private int RPE;
    private String workoutFormula;

    public Exercise(){
        //empty constructor needed
    }

    public Exercise(String workoutType, int sets, int reps, double oneRM, int RPE, String workoutFormula){
        this.workoutType = workoutType;
        this.sets = sets;
        this.reps = reps;
        this.oneRM = oneRM;
        this.RPE = RPE;
        this.workoutFormula = workoutFormula;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getOneRM() {
        return oneRM;
    }

    public int getRPE() {
        return RPE;
    }

    public String getWorkoutFormula() {
        return workoutFormula;
    }
    */

    private String title;
    private String description;
    private int priority;

    public Exercise() {
        //empty constructor needed
    }

    public Exercise(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
