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

    private String mName;
    private boolean mHasImg;
    private byte[] mImgByte;

    public PartnerProfileFragment() {    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the partner profile
     * ImageView or TextView to display.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewTextView = inflater.inflate(R.layout.fragment_partner_img_tv, container, false);
        View rootViewCircleImageView = inflater.inflate(R.layout.fragment_partner_img, container, false);

        TextView partnerProfileTextView = (TextView) rootViewTextView.findViewById(R.id.partner_img_text_view);
        CircleImageView partnerProfileCircleImageView = (CircleImageView) rootViewCircleImageView.findViewById(R.id.partner_img);

        if(mHasImg) {
            partnerProfileCircleImageView.setVisibility(View.VISIBLE);
            Bitmap bitmapImg = getBitmapFromByte(mImgByte);
            partnerProfileCircleImageView.setImageBitmap(bitmapImg);

            partnerProfileTextView.setVisibility(TextView.INVISIBLE);
            return rootViewCircleImageView;
        } else {
            partnerProfileTextView.setVisibility(TextView.VISIBLE);
            String firstLetter = mName.substring(0, 1);
            partnerProfileTextView.setText(firstLetter);

            partnerProfileCircleImageView.setVisibility(View.INVISIBLE);
            return  rootViewTextView;
        }
    }

    public void setHasPartnerProfileImg(boolean hasProfilePicture) {
        mHasImg = hasProfilePicture;
    }

    public void setPartnerProfileImg(byte[] imgByte) {
        mImgByte = imgByte;
    }

    public void setPartnerName(String name) {
        mName = name;
    }

    private Bitmap getBitmapFromByte(byte[] byteImg){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImg , 0, byteImg .length);
        return bitmap;
    }
}
