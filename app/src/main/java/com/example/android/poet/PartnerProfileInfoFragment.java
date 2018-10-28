package com.example.android.poet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.poet.ui.EditorActivity;


public class PartnerProfileInfoFragment extends Fragment {

    private Uri mCurrentPartnerUri;
    private Partner mPartner;

    private TextView.OnTouchListener mTouchListener = new TextView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onOpenEdit();
            return false;
        }
    };


    public PartnerProfileInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_partner_profile_info, container, false);

        TextView genderTextView = (TextView) rootView.findViewById(R.id.partner_profile_gender_entry);
        TextView notesTextView = (TextView) rootView.findViewById(R.id.partner_profile_notes_entry);
        TextView statusTextView = (TextView) rootView.findViewById(R.id.partner_profile_status_entry);

        genderTextView.setText(mPartner.getGender());
        notesTextView.setText(mPartner.getNotes());
        statusTextView.setText(mPartner.getStatus());

        genderTextView.setOnTouchListener(mTouchListener);
        notesTextView.setOnTouchListener(mTouchListener);
        statusTextView.setOnTouchListener(mTouchListener);

        return rootView;
    }

    private void onOpenEdit() {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.setData(mCurrentPartnerUri);
        startActivity(intent);
    }

    public void setPartner(Partner partner) {
        mPartner = partner;
    }

    public void setCurrentPartnerUri(Uri partnerUri) {
        mCurrentPartnerUri = partnerUri;
    }

}
