package com.example.android.poet.data;

import android.provider.BaseColumns;

public final class PersonContract {

    private PersonContract() {

    }

    public static abstract class ContactEntry implements BaseColumns {

        public static final String TABLE_NAME = "contact";
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PERSON_FIRST_NAME = "firstName";
        public static final String COLUMN_PERSON_MIDDLE_NAME = "middleName";
        public static final String COLUMN_PERSON_LAST_NAME = "lastName";
        public static final String COLUMN_PERSON_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_PERSON_GENDER = "gender";
        public static final String COLUMN_PERSON_RELATIONSHIP_STATUS = "relationshipStatus";
        public static final String COLUMN_PERSON_ETHNICITY = "ethnicity";
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
        public static final int GENDER_BIAGENDER = 16;
        public static final int GENDER_DEMIAGENDER = 17;
        public static final int GENDER_FEMME = 18;
        public static final int GENDER_BUTCH = 19;
        public static final int GENDER_TRANSVESTI_NB = 20;
        public static final int GENDER_ALIAGENDER = 21;


        // Possible constant values for the relationship status
        public static final int STATUS_GIRLFRIEND = 0;
        public static final int STATUS_BOYFRIEND = 1;
        public static final int STATUS_WIFE = 2;
        public static final int STATUS_HUSBAND = 3;
        public static final int STATUS_OTHER = 4;

        // Possible constant values for the ethnicity
    }

}
