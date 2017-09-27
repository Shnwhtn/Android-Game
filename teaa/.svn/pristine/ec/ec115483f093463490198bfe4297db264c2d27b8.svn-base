package mobile.labs.acw;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
Loosely based on the tutorial from http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */

public class Database extends SQLiteOpenHelper implements Serializable{

    // Variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "puzzledatabase.db";
    public static final String TABLE_NAME = "puzzleIndexTable";
    public static final String COL1_ID ="id";
    public static final String COL2_PUZZLENUMBER = "puzzlename";
    public static final String COL3_ROWS = "Rows";
    public static final String COL4_PUZZLESET = "Puzzleset";
    public static final String COL5_LAYOUT = "Layout";
    public static final String COL6_PLAYS = "attempts";
    public static final String COL7_CLICKS = "clicks";
    public static final String COL8_SIZE = "size";

    //Database constuctor
    public Database(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE "+ TABLE_NAME + "("+
                COL1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COL2_PUZZLENUMBER+" TEXT, " +
                COL3_ROWS+ " INTEGER, " +
                COL4_PUZZLESET+ " TEXT," +
                COL5_LAYOUT+ " TEXT, "+
                COL6_PLAYS+ " INTEGER, " +
                COL7_CLICKS+ " INTEGER, "+
                COL8_SIZE+ " TEXT "+
        ");";
        //Execute
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion)
    {
        // This is for if the database is been upgraded
        sqLiteDatabase.execSQL("DROP TABLE IF EXISITS "+TABLE_NAME);

        //Create tables
        onCreate(sqLiteDatabase);
    }

    // CRUD ACTIONS


    public void addPuzzle(Puzzle puzzle)
    {
        /*
        This method will add a puzzle object into the database
         */

        // Choose Writeable database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // Create a new Content Values
        ContentValues values = new ContentValues();
        // Fill the values
        values.put(COL2_PUZZLENUMBER,puzzle.GetPuzzleName());
        values.put(COL3_ROWS,puzzle.GetRows());
        values.put(COL4_PUZZLESET,puzzle.GetPuzzleSet());
        values.put(COL5_LAYOUT,puzzle.GetLayout());
        values.put(COL6_PLAYS, puzzle.GetAttempts());
        values.put(COL7_CLICKS, puzzle.GetClicks());
        values.put(COL8_SIZE, puzzle.GetSize());
        // Post into the database
        sqLiteDatabase.insert(TABLE_NAME,null,values);
        // Close the database connection
        sqLiteDatabase.close();
    }

    public Puzzle getPuzzle(String id)
    {
        /*
        this is to get the puzzle info based on a key, either id or puzzlenumber name
         */
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Log.d("strings",id.toString());
        // Cursor will take a sql query based on the id and the puzzle number
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[] { COL1_ID, COL2_PUZZLENUMBER,COL3_ROWS,COL4_PUZZLESET,COL5_LAYOUT,COL6_PLAYS,COL7_CLICKS,COL8_SIZE },COL2_PUZZLENUMBER + "=?", new String[]
                {String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        // Create a puzzle object based on the result from the sql, Integer.parseInt will take an int value and parse into a string
        Puzzle puzzle = new Puzzle(cursor.getInt(0), "puzzle"+cursor.getString(1), cursor.getInt(2),
                cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getString(7));
        //return object
        return puzzle;
    }


    public List<Puzzle> getAllThePuzzles()
    {
        /*
        This will get a list of all the puzzles , going through a loop to output them
         */

        //Create a list of puzzle objects
        List <Puzzle> puzzleListArray = new ArrayList<Puzzle>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        //Loop and add to the array list
        if(cursor.moveToFirst()) {
            do {
                // Create a new puzzle object
                Puzzle puzzle = new Puzzle(Integer.parseInt(cursor.getString(0)), "puzzle"+cursor.getString(1), (Integer.parseInt(cursor.getString(2))),
                        cursor.getString(3), cursor.getString(4), (Integer.parseInt(cursor.getString(5))), (Integer.parseInt(cursor.getString(6))),
                        cursor.getString(7));
                // Fill object
                puzzle.SetID(Integer.parseInt(cursor.getString(0)));
                puzzle.SetPuzzleName(cursor.getString(1));
                puzzle.SetRows(cursor.getInt(2));
                puzzle.SetPuzzleSet(cursor.getString(3));
                puzzle.SetLayout(cursor.getString(4));
                puzzle.SetAttempts(cursor.getInt(5));
                puzzle.SetClicks(cursor.getInt(6));
                puzzle.SetSize(cursor.getString(7));
                // Adds each to the array list
                puzzleListArray.add(puzzle);

            } while (cursor.moveToNext());
        }
        //Return the array list
        return puzzleListArray;
    }



    public List<Puzzle> getAllThePuzzlesOfSize(String size)
    {
        /*
        This will get a list of all the puzzles , going through a loop to output them
         */

        //Create a list of puzzle objects
        List <Puzzle> puzzleListArray = new ArrayList<Puzzle>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COL8_SIZE + " = '"+size+"'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        //Loop and add to the array list
        if(cursor.moveToFirst()) {
            do {
                // Create a new puzzle object
                Puzzle puzzle = new Puzzle(Integer.parseInt(cursor.getString(0)), "puzzle"+cursor.getString(1), (Integer.parseInt(cursor.getString(2))), cursor.getString(3), cursor.getString(4), (Integer.parseInt(cursor.getString(5))), (Integer.parseInt(cursor.getString(6))), cursor.getString(7));
                // Fill object
                puzzle.SetID(Integer.parseInt(cursor.getString(0)));
                puzzle.SetPuzzleName(cursor.getString(1));
                puzzle.SetRows(cursor.getInt(2));
                puzzle.SetPuzzleSet(cursor.getString(3));
                puzzle.SetLayout(cursor.getString(4));
                puzzle.SetAttempts(cursor.getInt(5));
                puzzle.SetClicks(cursor.getInt(6));
                puzzle.SetSize(cursor.getString(7));
                // Adds each to the array list
                puzzleListArray.add(puzzle);

            } while (cursor.moveToNext());
        }
        //Return the array list
        return puzzleListArray;
    }



    public boolean getExistingRecord(String puzzleName)
    {
        /*
        This method uses a query to see if an instance exists in the database
         */
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+COL2_PUZZLENUMBER +" = '" +puzzleName+ "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        sqLiteDatabase.close();
        return exist;
    }


    public int getCountOfPuzzles()
    {
        /*
        This method uses a query to get a query of all the puzzles in the database
         */

        String count = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(count,null);
        cursor.close();

        //return the count

        return cursor.getCount();
    }


    public int updateAPuzzle(Puzzle puzzle)
    {
        /*
        This method allows to update a single puzzle record
         */

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2_PUZZLENUMBER,puzzle.GetPuzzleName());
        values.put(COL3_ROWS,puzzle.GetRows());
        values.put(COL4_PUZZLESET,puzzle.GetPuzzleSet());
        values.put(COL5_LAYOUT,puzzle.GetLayout());
        values.put(COL6_PLAYS,puzzle.GetAttempts());
        values.put(COL7_CLICKS,puzzle.GetClicks());
        values.put(COL8_SIZE,puzzle.GetSize());
        //Update
        return sqLiteDatabase.update(TABLE_NAME, values, COL1_ID + " = ?", new String[]
                {
                        String.valueOf(puzzle.GetID())
                });
    }



    public void deleteAPuzzle(Puzzle puzzle)
    {
        /*
        Delete a puzzle
         */
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, COL1_ID + " = ?", new String[]
                {
                        String.valueOf(puzzle.GetID())
                });
        sqLiteDatabase.close();
    }



}
