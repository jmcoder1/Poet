package com.example.android.poet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

    private static final String LOG_TAG = "EditorActivity";

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
    private EditText mNotesEditText;

    private Spinner mStatusSpinner;
    private Spinner mGenderSpinner;

    private int mGender = ContactEntry.GENDER_UNKNOWN;
    private int mStatus = ContactEntry.STATUS_BOYFRIEND;

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

        mNotesEditText = (EditText) findViewById(R.id.notes);
        mNotesEditText.setOnTouchListener(mTouchListener);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        mStatusSpinner = (Spinner) findViewById(R.id.spinner_status);
        mStatusSpinner.setOnTouchListener(mTouchListener);

        setGenderSpinner();
        setStatusSpinner();
    }

    private void setGenderSpinner() {
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
                        mGender = ContactEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = ContactEntry.GENDER_FEMALE;
                    } else if (selection.equals(getString(R.string.gender_androgyne))) {
                        mGender = ContactEntry.GENDER_ANDROGYNE;
                    } else if (selection.equals(getString(R.string.gender_neutrosis))) {
                        mGender = ContactEntry.GENDER_NEUTROSIS;
                    } else if (selection.equals(getString(R.string.gender_agender))) {
                        mGender = ContactEntry.GENDER_AGENDER;
                    } else if (selection.equals(getString(R.string.gender_intergender))) {
                        mGender = ContactEntry.GENDER_INTERGENDER;
                    } else if (selection.equals(getString(R.string.gender_demiboy))) {
                        mGender = ContactEntry.GENDER_DEMIBOY;
                    } else if (selection.equals(getString(R.string.gender_demigirl))) {
                        mGender = ContactEntry.GENDER_DEMIGIRL;
                    } else if (selection.equals(getString(R.string.gender_third_gender))) {
                        mGender = ContactEntry.GENDER_THIRD_GENDER;
                    } else if (selection.equals(getString(R.string.gender_genderqueer))) {
                        mGender = ContactEntry.GENDER_GENDERQUEER;
                    } else if (selection.equals(getString(R.string.gender_pangender))){
                        mGender = ContactEntry.GENDER_PANGENDER;
                    } else if (selection.equals(getString(R.string.gender_epicene))) {
                        mGender = ContactEntry.GENDER_EPICENE;
                    } else if (selection.equals(getString(R.string.gender_genderfluid))) {
                        mGender = ContactEntry.GENDER_GENDERFLUID;
                    } else if (selection.equals(getString(R.string.gender_transgender))) {
                        mGender = ContactEntry.GENDER_TRANSGENDER;
                    } else if (selection.equals(getString(R.string.gender_bigender))) {
                        mGender = ContactEntry.GENDER_BIGENDER;
                    } else if (selection.equals(getString(R.string.gender_demiagender))) {
                        mGender = ContactEntry.GENDER_DEMIAGENDER;
                    } else if (selection.equals(getString(R.string.gender_femme))) {
                        mGender = ContactEntry.GENDER_FEMME;
                    } else if (selection.equals(getString(R.string.gender_butch))) {
                        mGender = ContactEntry.GENDER_BUTCH;
                    } else if (selection.equals(getString(R.string.gender_transvesti_nb))) {
                        mGender = ContactEntry.GENDER_TRANSVESTI_NB;
                    } else if (selection.equals(getString(R.string.gender_aliagender))) {
                        mGender = ContactEntry.GENDER_ALIAGENDER;
                    } else {
                        mGender = ContactEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = ContactEntry.GENDER_UNKNOWN;
            }
        });
    }

    private void setStatusSpinner() {
        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_status_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mStatusSpinner.setAdapter(statusSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.status_boyfriend))) {
                        mStatus = ContactEntry.STATUS_BOYFRIEND;
                    } else if (selection.equals(getString(R.string.status_girlfriend))) {
                        mStatus = ContactEntry.STATUS_GIRLRIEND;
                    } else if (selection.equals(getString(R.string.status_husband))) {
                        mStatus = ContactEntry.STATUS_HUSBAND;
                    } else if (selection.equals(getString(R.string.status_wife))) {
                        mStatus = ContactEntry.STATUS_WIFE;
                    } else if (selection.equals(getString(R.string.status_complicated))) {
                        mStatus = ContactEntry.STATUS_COMPLICATED;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mStatus = ContactEntry.STATUS_COMPLICATED;
            }
        });    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertPet() {

        String firstNameString = mFirstNameEditText.getText().toString().trim();
        String middleNameString = mMiddleNameEditText.getText().toString().trim();
        String lastNameString = mLastNameEditText.getText().toString().trim();
        String phoneNumberString = mPhoneNumberEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();

        ContentValues cv = new ContentValues();
        cv.put(ContactEntry.COLUMN_PERSON_FIRST_NAME, firstNameString);
        cv.put(ContactEntry.COLUMN_PERSON_MIDDLE_NAME, middleNameString);
        cv.put(ContactEntry.COLUMN_PERSON_LAST_NAME, lastNameString);
        cv.put(ContactEntry.COLUMN_PERSON_PHONE_NUMBER, phoneNumberString);
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, mGender);
        cv.put(ContactEntry.COLUMN_PERSON_STATUS, mStatus);
        cv.put(ContactEntry.COLUMN_PERSON_NOTES, notesString);

        // Insert a new row for pet in the database, returning the ID of that new row.
        Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, cv);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_partner_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_partner_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
