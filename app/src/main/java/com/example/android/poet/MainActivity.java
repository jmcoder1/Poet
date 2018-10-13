package com.example.android.poet;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
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
        implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "MainActivity".getClass().getSimpleName();
    private static final int PARTNER_LOADER = 0;

    private PersonCursorAdapter mPersonCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addPartnerFab = (FloatingActionButton) findViewById(R.id.add_partner_fab);
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
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

                //  forms a content uri with the URI of the specific item that was clicked on
                Uri currentPartnerUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI, id);
                intent.setData(currentPartnerUri);
                startActivity(intent);
            }
        });

    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadThemeFromPreferences(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadThemeFromPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_default_theme_label));

        if(sharedPreferenceTheme.equals(getString(R.string.pref_show_default_theme_key))) {
            setTheme(R.style.AppTheme);
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_aqua_theme_key))) {
            setTheme(R.style.AppThemeAqua);
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_dark_theme_key))) {
            setTheme(R.style.AppThemeDark);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_theme_key))) {
            loadThemeFromPreferences(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_genders_key))) {
            //mVisualizerView.setShowMid(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_show_genders_default)));
        }
    }

    /**
     * Helper method to delete all partners in the db.
     */
    private void deleteAllPartners() {
        int rowsDeleted = getContentResolver().delete(ContactEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + " rows deleted from" + ContactEntry.TABLE_NAME + " database");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                deleteAllPartners();
                Log.v(LOG_TAG, "All rows deleted from" + ContactEntry.TABLE_NAME + "  database");
                return true;
            case R.id.action_settings:
                Log.v(LOG_TAG, "opened Settings activity");
                Intent startSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_PERSON_NAME,
                ContactEntry.COLUMN_PERSON_GENDER,
                ContactEntry.COLUMN_PERSON_STATUS,
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
