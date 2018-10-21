package com.example.android.poet.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.poet.R;
import com.example.android.poet.data.PersonContract.ContactEntry;


/**
 * {@link ContentProvider} for Pets app.
 */
public class PersonProvider extends ContentProvider {

    public static final String LOG_TAG = PersonProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the partners table */
    private static final int PERSON = 100;

    /** URI matcher code for the content URI for a single person in the partners table */
    private static final int PERSON_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(ContactEntry.CONTENT_AUTHORITY, "partners", PERSON);
        sUriMatcher.addURI(ContactEntry.CONTENT_AUTHORITY, "partners/#", PERSON_ID);

    }

    private PersonDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new PersonDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI using the parameters.
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PERSON:
                cursor = db.query(
                        ContactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PERSON_ID:
                // For the PERSON_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.poet/partners/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the partners table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(
                        ContactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues cv) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PERSON:
                return insertPerson(uri, cv);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPerson(Uri uri, ContentValues cv) {

        String name = cv.getAsString(ContactEntry.COLUMN_PERSON_NAME);
        Integer gender = cv.getAsInteger(ContactEntry.COLUMN_PERSON_GENDER);
        Integer status = cv.getAsInteger(ContactEntry.COLUMN_PERSON_STATUS);

        if(name == null) {
            throw new IllegalArgumentException("Person requires a valid name");
        } else if (gender ==  null || !ContactEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Person requires a valid gender");
        } else if (status == null || !ContactEntry.isValidStatus(status)) {
            throw new IllegalArgumentException("Person requires a valid relationship status");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ContactEntry.TABLE_NAME, null, cv);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues cv, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PERSON:
                return updatePerson(uri, cv, selection, selectionArgs);
            case PERSON_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePerson(uri, cv, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update persons in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more persons).
     * Return the number of rows that were successfully updated.
     */
    private int updatePerson(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // If the {@link ContactEntry#COLUMN_PERSON_FIRST_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ContactEntry.COLUMN_PERSON_NAME)) {
            String name = values.getAsString(ContactEntry.COLUMN_PERSON_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Person requires a valid name");
            }
        }

        // If the {@link ContactEntry#COLUMN_PERSON_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(ContactEntry.COLUMN_PERSON_GENDER)) {
            Integer gender = values.getAsInteger(ContactEntry.COLUMN_PERSON_GENDER);
            if (gender == null || !ContactEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Person requires valid gender");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ContactEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PERSON:
                rowsDeleted = database.delete(ContactEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PERSON_ID:
                // Delete a single row given by the ID in the URI
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ContactEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PERSON:
                return ContactEntry.CONTENT_LIST_TYPE;
            case PERSON_ID:
                return ContactEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    /** Helper methods to get the status of the person
     *
     * @param cursor
     * @param mContext
     * @return
     */
    public static String getStatus(Cursor cursor, Context mContext) {
        int statusEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_STATUS));
        String status = mContext.getResources().getStringArray(R.array.array_status_options)[statusEnum];
        return status;
    }

    /**
     * Helper method to get the gender of the person.
     * @param cursor
     * @param context
     * @return
     */
    public static String getGender(Cursor cursor, Context context) {
        int genderEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_GENDER));
        return context.getResources().getStringArray(R.array.array_gender_options)[genderEnum];
    }
}