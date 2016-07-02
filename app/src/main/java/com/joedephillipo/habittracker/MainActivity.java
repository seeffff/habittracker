package com.joedephillipo.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  The instructions said we were not supposed to implement a UI.
            The following shows that the program can make use of the methods and data in
            the database.
         */

        //Create a new database adapter
        HabitDbAdapter dbAdapter = new HabitDbAdapter(getBaseContext());

        //Open the database
        dbAdapter.open();

        //Add some data to the database
        dbAdapter.createHabit("Eat a meal", 3);
        dbAdapter.createHabit("Take a shower", 1);
        dbAdapter.createHabit("Program Android", 9);

        //Create a usable array list of habits.
        ArrayList<Habit> habitList = dbAdapter.getAllHabits();

        //Scan through the array list.  This shows we can make use of all the habits
        //in the database
        for(int i = 0; i < habitList.size(); i++){
            Habit habit = habitList.get(i);
        }

        //Looks like I'm going to be programming Android for an extra hour today
        //Let's add one to the amount column
        dbAdapter.updateHabit(3, "Program Android", 10);

        //Now we don't have time to shower so lets remove that value from the database
        dbAdapter.deleteHabit(2);

        //Close the database
        dbAdapter.close();

    }
}
