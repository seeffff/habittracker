package com.joedephillipo.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class HabitDbAdapter {

    //Declare variables we will need
    private static final String DATABASE_NAME="habits.db";
    private static final int  DATABASE_VERSION=1;
    public static final String HABIT_TABLE="habit";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_TASK="task";
    public static final String COLUMN_AMOUNT="amount";
    public static final String COLUMN_DATE="date";

    //String array of all the columns
    public String[] allColumns={COLUMN_ID, COLUMN_TASK, COLUMN_AMOUNT, COLUMN_DATE};

    //Create the table
    public static final String CREATE_TABLE_HABIT = " create table " + HABIT_TABLE + " ( "
            + COLUMN_ID +" integer primary key autoincrement, "
            +COLUMN_TASK +" text not null, "
            +COLUMN_AMOUNT+ " integer not null, "
            +COLUMN_DATE+" ); ";
    private SQLiteDatabase sqlDB;
    private HabitDbHelper habitDbHelper;
    private Context context;
    public HabitDbAdapter (Context ctx){
        context=ctx;
    }

    //Open the database
    public HabitDbAdapter open() throws android.database.SQLException{

        habitDbHelper=new HabitDbHelper(context);
        sqlDB=habitDbHelper.getWritableDatabase();

        return this;
    }

    //Close the database
    public void close(){
        habitDbHelper.close();
    }

    //Adds a new habit to the database
    public Habit createHabit(String task, int amount){

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(HABIT_TABLE, null, values);

        Cursor cursor = sqlDB.query(HABIT_TABLE, allColumns, COLUMN_ID  + " = " + insertId , null, null, null, null);
        cursor.moveToFirst();
        Habit newHabit = cursorToHabit(cursor);
        cursor.close();
        return  newHabit;
    }

    //Deletes a habit from the database
    public long deleteHabit(long idToDelete){
        return sqlDB.delete(HABIT_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    //Updates an existing habit in the database
    public long updateHabit(long idToUpdate, String newTask, int newAmount){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, newTask);
        values.put(COLUMN_AMOUNT, newAmount);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(HABIT_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);

    }

    //Returns an array list of all the habits in the database
    public ArrayList<Habit> getAllHabits(){
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Cursor cursor=sqlDB.query(HABIT_TABLE, allColumns, null, null, null, null, null);
        for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            Habit habit = cursorToHabit(cursor);
            habits.add(habit);
        }
        cursor.close();
        return habits;
    }

    //Moves the cursor to a new habit
    private Habit cursorToHabit(Cursor cursor){
        Habit newHabit = new Habit(cursor.getString(0), cursor.getInt(1), cursor.getLong(2),
                cursor.getLong(3));
        return newHabit;

    }
    private static class HabitDbHelper extends SQLiteOpenHelper{
        HabitDbHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }

        //Creates the database
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_HABIT);
        }

        //Upgrades the database
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

            Log.w(HabitDbHelper.class.getName(),"upgrading database from version "+ newVersion+"to"+oldVersion+",which will destroy all old data");
            db.execSQL("Drop table if exist "+ HABIT_TABLE);
            onCreate(db);

        }
    }

}