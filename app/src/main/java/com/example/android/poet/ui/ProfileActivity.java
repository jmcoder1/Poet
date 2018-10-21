package com.example.android.poet.ui;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
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
import android.view.View;
import android.widget.Toast;

import com.example.android.poet.PartnerProfileFragment;
import com.example.android.poet.PartnerProfileInfoFragment;
import com.example.android.poet.R;
import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.data.PersonProvider;

public class ProfileActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ProfileActivity".getClass().getSimpleName();
    private static final int EXISTING_PARTNER_LOADER = 1;
    private Uri mCurrentPartnerUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCurrentPartnerUri = getIntent().getData();

        getSupportLoaderManager().initLoader(EXISTING_PARTNER_LOADER, null, this);

        FloatingActionButton editPartnerFAB = (FloatingActionButton) findViewById(R.id.edit_partner_fab);
        editPartnerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenEdit();
            }
        });
    }

    private void onOpenEdit() {
        Intent intent = new Intent(ProfileActivity.this, EditorActivity.class);
        intent.setData(mCurrentPartnerUri);
        startActivity(intent);
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

    @Override
    public Loader<Cursor> onCreateLoader(@NonNull int id, @NonNull Bundle bundle) {

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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() < 1) return;

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NAME);
            int notesColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_NOTES);

            String name = cursor.getString(nameColumnIndex);
            setTitle(name);

            String gender = PersonProvider.getGender(cursor,getApplicationContext());
            String status = PersonProvider.getStatus(cursor, getApplicationContext());

            String notes = cursor.getString(notesColumnIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();

            PartnerProfileInfoFragment partnerProfileInfoFragment = new PartnerProfileInfoFragment();
            partnerProfileInfoFragment.setGender(gender);
            partnerProfileInfoFragment.setNotes(notes);
            partnerProfileInfoFragment.setStatus(status);
            partnerProfileInfoFragment.setCurrentPartnerUri(mCurrentPartnerUri);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_profile_info_container, partnerProfileInfoFragment)
                    .commit();

            PartnerProfileFragment partnerProfileFragment = new PartnerProfileFragment();
            partnerProfileFragment.setPartnerName(name);

            fragmentManager.beginTransaction()
                    .add(R.id.partner_profile_img_container, partnerProfileFragment)
                    .commit();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //TODO: Add something here eventually
        Log.v(LOG_TAG, "onLoaderReset called");
    }

}