package com.example.android.poet;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ProfileActivity".getClass().getSimpleName();
    private static final int EXISTING_PARTNER_LOADER = 1;
    private ActivityProfileBinding mBinding;
    private FloatingActionButton editPartnerFAB;
    private Uri mCurrentPartnerUri;

    private TextView.OnTouchListener mTouchListener = new TextView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onOpenEdit();
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        mCurrentPartnerUri = getIntent().getData();
        getSupportLoaderManager().initLoader(EXISTING_PARTNER_LOADER, null, this);

        editPartnerFAB = (FloatingActionButton) findViewById(R.id.edit_partner_fab);
        editPartnerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenEdit();
            }
        });
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new partner, hide the "Delete" menu item.
        if (mCurrentPartnerUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void onOpenEdit() {
        Intent intent = new Intent(ProfileActivity.this, EditorActivity.class);
        intent.setData(mCurrentPartnerUri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the partner.
                deletePartner();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
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

    private void deletePartner() {
        // Only perform the delete if this is an existing partner.
        if (mCurrentPartnerUri != null) {
            // Call the ContentResolver to delete the partner at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPartnerUri
            // content URI already identifies the p that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentPartnerUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_partner_failed),
                        Toast.LENGTH_SHORT).show();
                Log.v(LOG_TAG, "There was an error with the delete");
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_partner_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                onOpenEdit();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ProfileActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to get the gender from the database using the cursor passed to it.
     * @param cursor
     * @return
     */
    private String getGender(Cursor cursor) {
        int genderEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_GENDER));
        return getApplicationContext().getResources().getStringArray(R.array.array_gender_options)[genderEnum];
    }

    /**
     * Helper method to get the status from the database using the cursor passed to it.
     * @param cursor
     * @return
     */
    private String getStatus(Cursor cursor) {
        int statusEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_STATUS));
        return getApplicationContext().getResources().getStringArray(R.array.array_status_options)[statusEnum];
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_PERSON_NAME,
                ContactEntry.COLUMN_PERSON_GENDER,
                ContactEntry.COLUMN_PERSON_STATUS,
                ContactEntry.COLUMN_PERSON_NOTES
        };

        Log.v(LOG_TAG, "onCreateLoader called");

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
            int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NAME);
            int notesColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NOTES);

            String name = cursor.getString(nameColumnIndex);
            setTitle(name);
            String gender = getGender(cursor);
            String status = getStatus(cursor);
            String notes = cursor.getString(notesColumnIndex);

            Log.v(LOG_TAG, "onLoadFinished called: " + "name: " + name + "\ngender: "
                    + gender + "\nstatus: " + status + "\nnotes: " + notes);

            mBinding.partnerProfileGenderEntry.setText(gender);
            mBinding.partnerProfileStatusEntry.setText(status);
            mBinding.partnerProfileNotesEntry.setText(notes);
            mBinding.partnerProfileGenderEntry.setOnTouchListener(mTouchListener);
            mBinding.partnerProfileStatusEntry.setOnTouchListener(mTouchListener);
            mBinding.partnerProfileNotesEntry.setOnTouchListener(mTouchListener);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO: Add something here eventually
        Log.v(LOG_TAG, "onLoaderReset called");
    }

}