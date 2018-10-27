package com.example.android.poet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.poet.data.PersonContract.ContactEntry;

public class PartnerEditorInfoFragment  extends Fragment {

    private Partner mPartner = new Partner();

    private EditText mNameEditText;
    private EditText mNotesEditText;

    private Spinner mStatusSpinner;
    private Spinner mGenderSpinner;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(mPartner != null) mPartner.setHasChanged(true);
            return false;
        }
    };

    public PartnerEditorInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_partner_editor_info, container, false);

        mNameEditText = (EditText) rootView.findViewById(R.id.name_et);
        mNameEditText.setOnTouchListener(mTouchListener);

        mNotesEditText = (EditText) rootView.findViewById(R.id.notes_et);
        mNotesEditText.setOnTouchListener(mTouchListener);

        mGenderSpinner = (Spinner) rootView.findViewById(R.id.spinner_gender);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        mStatusSpinner = (Spinner) rootView.findViewById(R.id.spinner_status);
        mStatusSpinner.setOnTouchListener(mTouchListener);

        setGenderSpinner();
        setStatusSpinner();

        if(mPartner != null) {
            mNameEditText.setText(mPartner.getName());
            mNotesEditText.setText(mPartner.getNotes());
            mGenderSpinner.setSelection(mPartner.getGenderEnum());
            mStatusSpinner.setSelection(mPartner.getStatusEnum());
        }

        return rootView;
    }

    public void setPartner(Partner partner) {
        mPartner = partner;
    }

    /**
     * Should only be called when name is known - fix this
     * @return
     */
    public Partner getPartnerFromFields() {
        mPartner = new Partner();
        mPartner.setName(mNameEditText.getText().toString());
        mPartner.setNotes(mNotesEditText.getText().toString());
        mPartner.setGenderEnum(mGenderSpinner.getSelectedItemPosition());
        mPartner.setStatusEnum(mStatusSpinner.getSelectedItemPosition());
        return mPartner;
    }

    private void setGenderSpinner() {

        ArrayAdapter genderSpinnerAdapter = getGenderSpinner();

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection) && mPartner != null) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_MALE);
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_FEMALE);
                    } else if (selection.equals(getString(R.string.gender_androgyne))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_ANDROGYNE);
                    } else if (selection.equals(getString(R.string.gender_neutrosis))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_NEUTROSIS);
                    } else if (selection.equals(getString(R.string.gender_agender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_AGENDER);
                    } else if (selection.equals(getString(R.string.gender_intergender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_INTERGENDER);
                    } else if (selection.equals(getString(R.string.gender_demiboy))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_DEMIBOY);
                    } else if (selection.equals(getString(R.string.gender_demigirl))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_DEMIGIRL);
                    } else if (selection.equals(getString(R.string.gender_third_gender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_THIRD_GENDER);
                    } else if (selection.equals(getString(R.string.gender_genderqueer))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_GENDERQUEER);
                    } else if (selection.equals(getString(R.string.gender_pangender))){
                        mPartner.setGenderEnum(ContactEntry.GENDER_PANGENDER);
                    } else if (selection.equals(getString(R.string.gender_epicene))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_EPICENE);
                    } else if (selection.equals(getString(R.string.gender_genderfluid))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_GENDERFLUID);
                    } else if (selection.equals(getString(R.string.gender_transgender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_TRANSGENDER);
                    } else if (selection.equals(getString(R.string.gender_bigender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_BIGENDER);
                    } else if (selection.equals(getString(R.string.gender_demiagender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_DEMIAGENDER);
                    } else if (selection.equals(getString(R.string.gender_femme))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_FEMME);
                    } else if (selection.equals(getString(R.string.gender_butch))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_BUTCH);
                    } else if (selection.equals(getString(R.string.gender_transvesti_nb))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_TRANSVESTI_NB);
                    } else if (selection.equals(getString(R.string.gender_aliagender))) {
                        mPartner.setGenderEnum(ContactEntry.GENDER_ALIAGENDER);
                    } else {
                        mPartner.setGenderEnum(ContactEntry.GENDER_UNKNOWN);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mPartner.setGenderEnum(ContactEntry.GENDER_UNKNOWN);

        };
        });
    }

    private void setStatusSpinner() {
        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.array_status_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mStatusSpinner.setAdapter(statusSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection) && mPartner != null) {
                    if (selection.equals(getString(R.string.status_boyfriend))) {
                        mPartner.setStatusEnum(ContactEntry.STATUS_BOYFRIEND);
                    } else if (selection.equals(getString(R.string.status_girlfriend))) {
                        mPartner.setStatusEnum(ContactEntry.STATUS_GIRLFRIEND);
                    } else if (selection.equals(getString(R.string.status_husband))) {
                        mPartner.setStatusEnum(ContactEntry.STATUS_HUSBAND);
                    } else if (selection.equals(getString(R.string.status_wife))) {
                        mPartner.setStatusEnum(ContactEntry.STATUS_WIFE);
                    } else if (selection.equals(getString(R.string.status_complicated))) {
                        mPartner.setStatusEnum(ContactEntry.STATUS_COMPLICATED);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPartner.setStatusEnum(ContactEntry.STATUS_COMPLICATED);
            }
        });
    }

    /**
     * Helper method to get the ArrayAdapter for the gender spinner from the multiple genders.
     * @return
     */
    private ArrayAdapter getGenderSpinner() {
        ArrayAdapter genderSpinnerAdapter;
        if(showMultipleGenders()) {
            genderSpinnerAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                    R.array.array_gender_options, android.R.layout.simple_spinner_item);
        } else {
            genderSpinnerAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                    R.array.array_multiple_gender_options, android.R.layout.simple_spinner_item);
        }
        return genderSpinnerAdapter;
    }

    /**
     * Helper method to read from SharedPreferences to determine the set number of genders.
     * @return
     */
    private boolean showMultipleGenders() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());
        return sharedPreferences.getBoolean(getString(R.string.pref_show_genders_key)
                , getResources().getBoolean(R.bool.pref_genders_default));
    }


}
