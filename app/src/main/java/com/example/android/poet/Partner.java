package com.example.android.poet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Partner {

    private String mName;
    private boolean mHasImg = false;
    private byte[] mImgByte;
    private Bitmap mImgBitmap;

    public Partner(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setHasImg(boolean hasImg) {
        mHasImg = hasImg;
    }

    public void setImgByte(byte[] imgByte) {
        mImgByte = imgByte;
    }

    public void setImgByte(Bitmap imgBitmap) {
        mImgByte = getByteFromBitmap(imgBitmap);
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        mImgBitmap = imgBitmap;
    }

    public Bitmap getImgBitmap() {
        return mImgBitmap;
    }

    public byte[] getImgByte() {
        return mImgByte;
    }

    /** This helper method gets the partner profile image based on whether a profile picture has been
     * chosen.
     *
     * @param partnerTV
     * @param partnerImg
     * @return
     */
    public View getPartnerImg(TextView partnerTV, CircleImageView partnerImg, CircleImageView defaultImg) {
        View rootView;

        // There are only 3 options - no name or profile picture, no profile picture, and no name.
        if (!mHasImg && mName == null) { // TODO:
            rootView = defaultImg;
        } else if(mHasImg) {
            mImgBitmap = getBitmapFromByte(mImgByte);
            partnerImg.setImageBitmap(mImgBitmap);
            rootView =  partnerImg;
        } else { //TODO: Null check for empty "" equals string
            String firstLetter = mName.substring(0, 1);
            partnerTV.setText(firstLetter);
            rootView = partnerTV;
        }
        return rootView;
    }

    private Bitmap getBitmapFromByte(byte[] byteImg){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImg , 0, byteImg .length);
        return bitmap;
    }

    private byte[] getByteFromBitmap(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

}
