package com.example.android.poet.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.poet.Partner;
import com.example.android.poet.PartnerEditorFragment;
import com.example.android.poet.PartnerEditorInfoFragment;
import com.example.android.poet.data.PersonContract.ContactEntry;

import com.example.android.poet.R;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener,
        PartnerEditorFragment.OnDataPass {

    private static final String LOG_TAG = "EditorActivity".getClass().getSimpleName();
    private static final int EXISTING_PARTNER_LOADER = 0;

    private Uri mCurrentPartnerUri;
    private Partner mPartner;

    private PartnerEditorInfoFragment partnerEditorInfoFragment;
    private PartnerEditorFragment partnerEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentPartnerUri = intent.getData();

        if(mCurrentPartnerUri == null) {
            setTitle(R.string.add_partner);

            FragmentManager fragmentManager = getSupportFragmentManager();

            partnerEditorFragment = new PartnerEditorFragment();
            partnerEditorFragment.setPartner(mPartner);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_editor_img_container, partnerEditorFragment)
                    .commit();

            partnerEditorInfoFragment = new PartnerEditorInfoFragment();
            partnerEditorInfoFragment.setPartner(mPartner);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_editor_info_container, partnerEditorInfoFragment)
                    .commit();
        } else {
            setTitle(R.string.edit_partner);
            getSupportLoaderManager().initLoader(EXISTING_PARTNER_LOADER, null, this);
        }

    }

    /**
     * Get user input from editor and save new partner into database.
     */
    private void savePartner() {
        if(mPartner == null) mPartner = partnerEditorInfoFragment.getPartnerFromFields();

        String name = mPartner.getName();
        String notes = mPartner.getNotes();
        byte[] imgByte = mPartner.getImgByte();
        int genderEnum = mPartner.getGenderEnum();
        int statusEnum = mPartner.getStatusEnum();

        ContentValues cv = new ContentValues();
        cv.put(ContactEntry.COLUMN_PERSON_NAME, name);
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, genderEnum);
        cv.put(ContactEntry.COLUMN_PERSON_STATUS, statusEnum);
        cv.put(ContactEntry.COLUMN_PERSON_NOTES, notes);

        if(mCurrentPartnerUri == null && TextUtils.isEmpty(name)) {
            return;
        }

        if(imgByte != null) {
            cv.put(ContactEntry.COLUMN_PERSON_IMG, imgByte);
        }

        if(mCurrentPartnerUri == null) {
            Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, cv);
        } else {
            int rowsAffected = getContentResolver().update(mCurrentPartnerUri, cv, null, null);
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_insert_partner_failed),
                        Toast.LENGTH_SHORT).show();
                Log.v(LOG_TAG, "There was an error the update");
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_partner_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
            // Create an AlertDialog.Builder and set the message, and click listeners
            // for the positive and negative buttons on the dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.unsaved_changes_dialog_msg);
            builder.setPositiveButton(R.string.discard, discardButtonClickListener);
            builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked the "Keep editing" button, so dismiss the dialog
                    // and continue editing the partner.
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the partner hasn't changed, continue with handling back button press
        if (!mPartner.getHasChanged()) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                savePartner();
                finish();
                return true;
            case android.R.id.home:
                if (!mPartner.getHasChanged()) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_PERSON_NAME,
                ContactEntry.COLUMN_PERSON_GENDER,
                ContactEntry.COLUMN_PERSON_STATUS,
                ContactEntry.COLUMN_PERSON_NOTES,
                ContactEntry.COLUMN_PERSON_IMG
        };

        return new CursorLoader(this,
                mCurrentPartnerUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null || cursor.getCount() < 1) return;
        if (cursor.moveToFirst()) {
            // Find the columns of partner attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NAME);
            int genderColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_GENDER);
            int statusColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_STATUS);
            int notesColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NOTES);
            int imgColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_IMG);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int status = cursor.getInt(statusColumnIndex);
            String notes = cursor.getString(notesColumnIndex);
            byte[] imgByte = cursor.getBlob(imgColumnIndex);

            mPartner = new Partner(name);
            if(imgByte != null) mPartner.setImgByte(imgByte);
            if(imgByte != null) mPartner.setHasImg(imgByte != null);
            mPartner.setName(name);
            mPartner.setNotes(notes);
            mPartner.setGenderEnum(gender);
            mPartner.setStatusEnum(status);

            setTitle(name);

            FragmentManager fragmentManager = getSupportFragmentManager();

            partnerEditorFragment = new PartnerEditorFragment();
            partnerEditorFragment.setPartner(mPartner);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_editor_img_container, partnerEditorFragment)
                    .commit();

            partnerEditorInfoFragment = new PartnerEditorInfoFragment();
            partnerEditorInfoFragment.setPartner(mPartner);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_editor_info_container, partnerEditorInfoFragment)
                    .commit();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //
    }

    @Override
    public void onDataPass(byte[] imgData) {
        Log.d(LOG_TAG,"onDataPass: called " + imgData);
        mPartner.setImgByte(imgData);
    }
}
