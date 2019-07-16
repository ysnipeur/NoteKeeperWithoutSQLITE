package com.mobile.takoumbo.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteKeeperOpenHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_FILE_NAME = "NoteKeeper2.db";
    private  static final int DATABASE_VERSION = 1;

    public NoteKeeperOpenHelper(Context context)
    {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // We create our database if it doesn't exist and create the associated tables

        db.execSQL(NoteKeeperDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(NoteKeeperDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE);

        // Let's add data to our tables using the DatabaseDataWorker class

        DatabaseDataWorker databaseDataWorker = new DatabaseDataWorker(db);

        // Calling methods to insert courses and notes

        databaseDataWorker.insertCourses();
        databaseDataWorker.insertSampleNotes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
