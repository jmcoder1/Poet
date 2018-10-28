package com.example.android.poet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;
import com.example.android.poet.data.PersonContract.ContactEntry;


import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Partner {

    private String mName;
    private String mGender;
    private String mStatus;
    private String mNotes;
    private int mGenderEnum = ContactEntry.GENDER_UNKNOWN;
    private int mStatusEnum = ContactEntry.STATUS_BOYFRIEND;
    private boolean mHasImg = false;
    private boolean mPartnerHasChanged = false;
    private byte[] mImgByte;
    private Bitmap mImgBitmap;

    public Partner(String name) {
        mName = name;
    }

    public Partner() {}

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getGender() { return mGender; }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getNotes() { return mNotes; }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getStatus() { return mStatus; }

    public void setStatus(String status) {
        mStatus = status;
    }

    public int getGenderEnum() { return mGenderEnum; }

    public int getStatusEnum() { return mStatusEnum; }

    public void setGenderEnum(int gender) { mGenderEnum = gender; }

    public void setStatusEnum(int status) { mStatusEnum = status; }

    public void setHasImg(boolean hasImg) {
        mHasImg = hasImg;
    }

    public void setImgByte(byte[] imgByte) {
        mImgByte = imgByte;
        mImgBitmap = getBitmapFromByte(imgByte);
    }

    public void setImgByte(Bitmap imgBitmap) {
        mImgBitmap = imgBitmap;
        mImgByte = getByteFromBitmap(mImgBitmap);
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        mImgBitmap = imgBitmap;
        mImgByte = getByteFromBitmap(mImgBitmap);
    }

    public void setHasChanged(boolean changed) {
        mPartnerHasChanged = changed;
    }

    public boolean getHasChanged() {
        return mPartnerHasChanged;
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
        if (!mHasImg && mName == null) {
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
