package com.example.android.poet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.poet.ui.EditorActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class PartnerProfileFragment extends Fragment {

    private Partner mPartner;
    private Uri mCurrentPartnerUri;

    private TextView.OnTouchListener mTouchListener = new TextView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onOpenEdit();
            return false;
        }
    };

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

        View finalView = mPartner.getPartnerImg(partnerTextView, partnerCircleImageView, partnerCircleImageViewDefault);
        finalView.setOnTouchListener(mTouchListener);
        return finalView;

    }

    public void setPartner(Partner partner) {
        mPartner = partner;
    }

    public void setCurrentPartnerUri(Uri partnerUri) {
        mCurrentPartnerUri = partnerUri;
    }

    private void onOpenEdit() {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.setData(mCurrentPartnerUri);
        startActivity(intent);
    }


}
