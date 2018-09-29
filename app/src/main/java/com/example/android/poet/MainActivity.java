package com.example.android.poet;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.poet.EditorActivity;
import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.data.PersonDbHelper;

public class MainActivity extends AppCompatActivity {

    // The edit text FAB
    private FloatingActionButton addPersonFab;

    //
    private static final String LOG_TAG = "MainActivity";

    private PersonDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPersonFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        addPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new PersonDbHelper(this);
        displayDbInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDbInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_person);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertPet() {
        ContentValues cv = new ContentValues();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        cv.put(ContactEntry.COLUMN_PERSON_FIRST_NAME, "Billy");
        cv.put(ContactEntry.COLUMN_PERSON_LAST_NAME, "Bob");
        cv.put(ContactEntry.COLUMN_PERSON_PHONE_NUMBER, "0731243212231");
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, 2);
        cv.put(ContactEntry.COLUMN_PERSON_RELATIONSHIP_STATUS, 0);

        db.insert(ContactEntry.TABLE_NAME, null, cv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDbInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method initialises the my_location gps widget that centres tha map to the
     * user device location.
     */
    private void initAddPersonFab() {

        // Sets the colour of the FAB
        setFabColors(addPersonFab,
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary));

        // Controls what happens when the my location widget is pressed
        addPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "addPersonFab: clicked my location FAB widget");
            }
        });
    }

    /**
     * This method sets the colours of the floating action button.
     *
     * @param fab The floating action button widget.
     * @param primaryColor The colour of the button when it is not pressed.
     * @param rippleColor The colour of the button when the button is pressed.
     */
    private void setFabColors(FloatingActionButton fab, int primaryColor, int rippleColor) {

        int[][] states = {
                {android.R.attr.state_enabled},
                {android.R.attr.state_pressed},
        };

        int[] colors = {
                primaryColor,
                rippleColor,
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        fab.setBackgroundTintList(colorStateList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDbInfo();
    }
}
