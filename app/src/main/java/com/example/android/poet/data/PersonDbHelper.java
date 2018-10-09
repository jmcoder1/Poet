package com.example.android.poet.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.poet.data.PersonContract.ContactEntry;

public class PersonDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PersonDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "partners.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_PERSON_TABLE =  "CREATE TABLE " + ContactEntry.TABLE_NAME
            + " (" + ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContactEntry.COLUMN_PERSON_NAME + " TEXT NOT NULL, "
            + ContactEntry.COLUMN_PERSON_GENDER + " INTEGER NOT NULL DEFAULT " + ContactEntry.GENDER_MALE + ", "
            + ContactEntry.COLUMN_PERSON_STATUS + " INTEGER, "
            + ContactEntry.COLUMN_PERSON_NOTES + " TEXT);";

    /**
     *
     * @param context The context of the app.
     */
    public PersonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This called when the database is created for the first time.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PERSON_TABLE);
    }

    /**
     * This is called when the database needs to eb upgraded.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
