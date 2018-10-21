package com.example.android.poet.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.poet.R;

public final class PersonContract {

    private PersonContract() {

    }

    public static abstract class ContactEntry implements BaseColumns {

        // Constants for the content provider
        public static final String CONTENT_AUTHORITY = "com.example.android.poet";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_PERSON = "partners";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PERSON);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of person.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single person.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;

        public static final String TABLE_NAME = "partners";
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PERSON_NAME = "name";
        public static final String COLUMN_PERSON_GENDER = "gender";
        public static final String COLUMN_PERSON_STATUS = "status";
        public static final String COLUMN_PERSON_NOTES = "notes";

        // Possible constant values for the gender
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_ANDROGYNE = 3;
        public static final int GENDER_NEUTROSIS = 4;
        public static final int GENDER_AGENDER = 5;
        public static final int GENDER_INTERGENDER = 6;
        public static final int GENDER_DEMIBOY = 7;
        public static final int GENDER_DEMIGIRL = 8;
        public static final int GENDER_THIRD_GENDER = 9;
        public static final int GENDER_GENDERQUEER = 10;
        public static final int GENDER_PANGENDER = 11;
        public static final int GENDER_EPICENE = 12;
        public static final int GENDER_GENDERFLUID = 13;
        public static final int GENDER_TRANSGENDER = 14;
        public static final int GENDER_BIGENDER = 15;
        public static final int GENDER_DEMIAGENDER = 16;
        public static final int GENDER_FEMME = 17;
        public static final int GENDER_BUTCH = 18;
        public static final int GENDER_TRANSVESTI_NB = 19;
        public static final int GENDER_ALIAGENDER = 20;

        /**
         * Returns whether or not the given gender is a valid gender
         */
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE
                    || gender == GENDER_ANDROGYNE || gender == GENDER_NEUTROSIS
                    || gender == GENDER_AGENDER || gender == GENDER_INTERGENDER
                    || gender == GENDER_DEMIBOY || gender == GENDER_DEMIGIRL
                    || gender == GENDER_THIRD_GENDER || gender == GENDER_GENDERQUEER
                    || gender == GENDER_PANGENDER || gender == GENDER_EPICENE
                    || gender == GENDER_GENDERFLUID || gender == GENDER_TRANSGENDER
                    || gender == GENDER_BIGENDER || gender == GENDER_DEMIAGENDER
                    || gender == GENDER_FEMME || gender == GENDER_BUTCH
                    || gender == GENDER_TRANSVESTI_NB || gender == GENDER_ALIAGENDER) {
                return true;
            }
            return false;
        }

        // Possible constant values for the relationship status
        public static final int STATUS_BOYFRIEND = 0;
        public static final int STATUS_GIRLRIEND = 1;
        public static final int STATUS_HUSBAND = 2;
        public static final int STATUS_WIFE = 3;
        public static final int STATUS_COMPLICATED = 4;
        //TODO Add some other categories i.e. poly/mistress...

        /**
         * Returns whether or not the given relationship status is valid.
         *
         * @param status The relationship status to the partner.
         */
        public static boolean isValidStatus(int status) {
            if(status == STATUS_BOYFRIEND || status == STATUS_GIRLRIEND || status == STATUS_HUSBAND
                    || status == STATUS_WIFE || status == STATUS_COMPLICATED) {
                return true;
            }
            return false;
        }
    }

}
