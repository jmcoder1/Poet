package com.example.android.poet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.poet.MainActivity;
import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.data.PersonDbHelper;

import com.example.android.poet.R;

public class EditorActivity extends AppCompatActivity {

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //TODO: mPetHasChanged = true;
            return false;
        }
    };

    // EditText input form
    private EditText mFirstNameEditText;
    private EditText mMiddleNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneNumberEditText;
    private EditText mRelationshipEditText;
    private EditText mEthnicityEditText;
    private EditText mNotesEditText;

    private Spinner mGenderSpinner;

    private int mGender = ContactEntry.GENDER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mFirstNameEditText = (EditText) findViewById(R.id.first_name);
        mFirstNameEditText.setOnTouchListener(mTouchListener);

        mMiddleNameEditText = (EditText) findViewById(R.id.middle_name);
        mMiddleNameEditText.setOnTouchListener(mTouchListener);

        mLastNameEditText = (EditText) findViewById(R.id.last_name);
        mLastNameEditText.setOnTouchListener(mTouchListener);

        mPhoneNumberEditText = (EditText) findViewById(R.id.mobile_num);
        mPhoneNumberEditText.setOnTouchListener(mTouchListener);

        mRelationshipEditText = (EditText) findViewById(R.id.relationship);
        mRelationshipEditText.setOnTouchListener(mTouchListener);

        mEthnicityEditText = (EditText) findViewById(R.id.ethnicity);
        mEthnicityEditText.setOnTouchListener(mTouchListener);

        mNotesEditText = (EditText) findViewById(R.id.notes);
        mNotesEditText.setOnTouchListener(mTouchListener);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        setSpinner();
    }

    private void setSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }//TODO: Add more infomration about the different genders
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertPet() {

        String firstNameString = mFirstNameEditText.getText().toString().trim();
        String middleNameString = mMiddleNameEditText.getText().toString().trim();
        String lastNameString = mLastNameEditText.getText().toString().trim();
        String phoneNumberString = mPhoneNumberEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();
        // TODO: ignore the relationship status and ethnicity for now

        PersonDbHelper mDbHelper = new PersonDbHelper(this);

        ContentValues cv = new ContentValues();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        cv.put(ContactEntry.COLUMN_PERSON_FIRST_NAME, firstNameString);
        cv.put(ContactEntry.COLUMN_PERSON_MIDDLE_NAME, middleNameString);
        cv.put(ContactEntry.COLUMN_PERSON_LAST_NAME, lastNameString);
        cv.put(ContactEntry.COLUMN_PERSON_PHONE_NUMBER, phoneNumberString);
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, mGender);
        //cv.put(ContactEntry.COLUMN_PERSON_RELATIONSHIP_STATUS, 0);
        //cv.put(ContactEntry.COLUMN_PERSON_ETHNICITY, 0);

        cv.put(ContactEntry.COLUMN_PERSON_NOTES, notesString);
        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(ContactEntry.TABLE_NAME, null, cv);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertPet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
