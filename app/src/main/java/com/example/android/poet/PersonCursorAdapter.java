package com.example.android.poet;

import com.example.android.poet.data.PersonContract.ContactEntry;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * {@link PersonCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class PersonCursorAdapter extends CursorAdapter {

    private Context mContext;

    /**
     * Constructs a new {@link PersonCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PersonCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        mContext = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.partners_list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.partner_name);
        TextView genderTextView = (TextView) view.findViewById(R.id.partner_gender);
        TextView statusTextView = (TextView) view.findViewById(R.id.partner_status);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_NAME));
        String gender = getGender(cursor);
        String status = getStatus(cursor);

        nameTextView.setText(name);
        genderTextView.setText(gender);
        statusTextView.setText(status);
    }

    /**
     * Helper method gets the gender from the enumeration.
     */
    private String getGender(Cursor cursor) {
        int genderEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_GENDER));
        String gender = mContext.getResources().getStringArray(R.array.array_gender_options)[genderEnum];
        return gender;
    }

    private String getStatus(Cursor cursor) {
        int statusEnum = cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_STATUS));
        String status = mContext.getResources().getStringArray(R.array.array_status_options)[statusEnum];
        return status;
    }
}