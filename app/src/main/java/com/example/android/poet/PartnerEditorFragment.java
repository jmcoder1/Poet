package com.example.android.poet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartnerEditorFragment extends Fragment{

    public PartnerEditorFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partner_profile_img, container, false);

        CircleImageView partnerEditorImageView = (CircleImageView) rootView.findViewById(R.id.partner_editor_img);

        if(hasProfilePicture()) {
            // TODO: Skip this away
            //partnerProfileTextView.setVisibility(TextView.INVISIBLE);
        } else {
            //partnerProfileTextView.setVisibility(TextView.VISIBLE);
            //partnerProfileImageView.setVisibility(View.INVISIBLE);
            //partnerProfileTextView.setText(firstLetter);
        }
        // Return the rootView
        return rootView;
    }

    /**
     * Helper method to check if the user has added a profile picture
     *
     */
    private boolean hasProfilePicture() {
        // TODO: If the user has added a new profile picture then return true
        return false;
    }

}
