package com.example.android.poet;

import com.example.android.poet.data.PersonContract.ContactEntry;
import com.example.android.poet.data.PersonProvider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

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
        TextView statusTextView = (TextView) view.findViewById(R.id.partner_status);

        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.partner_profile_img);
        TextView textViewImage = (TextView) view.findViewById(R.id.partner_main_img_text_view);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PERSON_NAME));
        String status = PersonProvider.getStatus(cursor, context);
        int imgColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PERSON_IMG);
        byte[] imgByte = cursor.getBlob(imgColumnIndex);


        Partner partner = new Partner(name);
        partner.setStatus(status);

        if(imgByte != null) {
            partner.setImgByte(imgByte);
            circleImageView.setImageBitmap(partner.getImgBitmap());
            circleImageView.setVisibility(View.VISIBLE);
            textViewImage.setVisibility(View.INVISIBLE);
        } else {
            textViewImage.setVisibility(View.VISIBLE);
            circleImageView.setVisibility(View.INVISIBLE);
            String firstLetter = name.substring(0, 1);
            textViewImage.setText(firstLetter);
        }

        nameTextView.setText(partner.getName());
        statusTextView.setText(partner.getStatus());
    }



}