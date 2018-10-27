package com.example.android.poet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class PartnerProfileFragment extends Fragment {

    private Partner mPartner;

    public PartnerProfileFragment() {    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the partner profile
     * ImageView or TextView to display.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewTextView = inflater.inflate(R.layout.fragment_partner_img_tv, container, false);
        View rootViewCircleImageView = inflater.inflate(R.layout.fragment_partner_img, container, false);
        View rootViewCircleImageViewDefault = inflater.inflate(R.layout.fragment_partner_img_default, container, false);;

        TextView partnerTextView = (TextView) rootViewTextView.findViewById(R.id.partner_img_text_view);
        CircleImageView partnerCircleImageView = (CircleImageView) rootViewCircleImageView.findViewById(R.id.partner_img);
        CircleImageView partnerCircleImageViewDefault = (CircleImageView) rootViewCircleImageViewDefault.findViewById(R.id.partner_img_default);

        return mPartner.getPartnerImg(partnerTextView, partnerCircleImageView, partnerCircleImageViewDefault);

    }

    public void setPartner(Partner partner) {
        mPartner = partner;
    }
}
