package com.example.android.poet;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PartnerProfileFragment extends Fragment {

    private String mPartnerName;

    public PartnerProfileFragment() {

    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the partner profile
     * ImageView or TextView to display.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partner_profile_img, container, false);

        TextView partnerProfileTextView = (TextView) rootView.findViewById(R.id.partner_profile_text_view);
        String firstLetter = mPartnerName.substring(0, 1);

        if(hasProfilePicture()) {
            // TODO: Skip this away
            partnerProfileTextView.setVisibility(TextView.INVISIBLE);
        } else {
            partnerProfileTextView.setVisibility(TextView.VISIBLE);
            //partnerProfileImageView.setVisibility(View.INVISIBLE);
            partnerProfileTextView.setText(firstLetter);
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

    public void setPartnerName(String name) {
        mPartnerName = name;
    }

}
