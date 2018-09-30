package com.example.android.poet;

import android.content.ContentValues;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.poet.EditorActivity;
import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.data.PersonDbHelper;

public class MainActivity extends AppCompatActivity {

    // The edit text FAB
    private FloatingActionButton addPersonFab;

    private static final String LOG_TAG = "MainActivity";


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
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_PERSON_FIRST_NAME,
                ContactEntry.COLUMN_PERSON_MIDDLE_NAME,
                ContactEntry.COLUMN_PERSON_LAST_NAME,
                ContactEntry.COLUMN_PERSON_GENDER,
                ContactEntry.COLUMN_PERSON_NOTES
        };

        String selection = null;
        String[] selectionArgs = null;

        Cursor cursor = getContentResolver().query(
                ContactEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        // Find the ListView which will be populated with the pet data
        ListView personListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        PersonCursorAdapter adapter = new PersonCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
        personListView.setAdapter(adapter);
    }

    private void insertPerson() {
        ContentValues cv = new ContentValues();

        cv.put(ContactEntry.COLUMN_PERSON_FIRST_NAME, "Billy");
        cv.put(ContactEntry.COLUMN_PERSON_LAST_NAME, "Bob");
        cv.put(ContactEntry.COLUMN_PERSON_PHONE_NUMBER, "0731243212231");
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, 2);
        cv.put(ContactEntry.COLUMN_PERSON_RELATIONSHIP_STATUS, 0);

        Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, cv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPerson();
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
