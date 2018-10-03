package com.example.android.poet;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.poet.data.PersonContract.ContactEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "MainActivity".getClass().getSimpleName();

    private static final int PARTNER_LOADER = 0;

    private FloatingActionButton addPartnerFab;

    private PersonCursorAdapter mPersonCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPartnerFab = (FloatingActionButton) findViewById(R.id.add_partner_fab);
        addPartnerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PARTNER_LOADER, null, this);

        mPersonCursorAdapter = new PersonCursorAdapter(this, null);
        ListView partnerListView = (ListView) findViewById(R.id.partners_list);
        partnerListView.setAdapter(mPersonCursorAdapter);

        View emptyView = findViewById(R.id.empty_view);
        partnerListView.setEmptyView(emptyView);
        partnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                //  forms a content uri with the URI of the specific item that was clicked on
                Uri currentPartnerUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI, id);

                intent.setData(currentPartnerUri);

                startActivity(intent);
            }
        });

    }

    private void insertPartner() {
        ContentValues cv = new ContentValues();

        cv.put(ContactEntry.COLUMN_PERSON_NAME, "Billy Bob");
        cv.put(ContactEntry.COLUMN_PERSON_PHONE_NUMBER, "0731243212231");
        cv.put(ContactEntry.COLUMN_PERSON_GENDER, ContactEntry.GENDER_MALE);
        cv.put(ContactEntry.COLUMN_PERSON_STATUS, ContactEntry.STATUS_COMPLICATED);

        Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, cv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertPartner();
                return true;
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_PERSON_NAME,
                ContactEntry.COLUMN_PERSON_GENDER,
                ContactEntry.COLUMN_PERSON_STATUS,
                ContactEntry.COLUMN_PERSON_PHONE_NUMBER,
                ContactEntry.COLUMN_PERSON_NOTES
        };

        return new CursorLoader(this,
                ContactEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPersonCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPersonCursorAdapter.swapCursor(null);
    }
}
