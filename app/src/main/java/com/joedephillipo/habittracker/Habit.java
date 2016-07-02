package com.joedephillipo.habittracker;

//Object used in database
public class Habit {

    //Declare variables
    private String mTask;
    private int mAmount;
    private long mHabitId, dateCreated;

    //Constructor
    public Habit(String task, int amount, long habitId, long dateCreated){
        this.mTask = task;
        this.mAmount = amount;
        this.mHabitId = habitId;
        this.dateCreated = dateCreated;
    }

    //Get the task string
    public String getTask(){
        return mTask;
    }

    //Get the amount integer
    public int getAmount(){
        return mAmount;
    }

    //Get the id
    public long getId(){
        return mHabitId;
    }

    //Get the date created
    public long getDateCreated(){
        return dateCreated;
    }

}
