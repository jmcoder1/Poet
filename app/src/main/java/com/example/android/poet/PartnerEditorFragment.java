package com.example.android.poet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PartnerEditorFragment extends Fragment{

    private static final int GALLERY_CODE = 1;
    private static final String LOG_TAG = "PartnerEditorFragment".getClass().getSimpleName();

    private Bitmap mBitmapImg;
    private byte[] mByteImg;
    private String mName;

    private CircleImageView partnerEditorImageView;

    public PartnerEditorFragment() { }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            openGallery();
            return false;
        }
    };

    private OnDataPass dataPasser;
    public interface OnDataPass {
        public void onDataPass(byte[]  imgData);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partner_img, container, false);

        partnerEditorImageView = (CircleImageView) rootView.findViewById(R.id.partner_img);
        partnerEditorImageView.setImageBitmap(mBitmapImg);
        partnerEditorImageView.setOnTouchListener(mTouchListener);

        return rootView;
    }

    private void openGallery() {
        Log.v(LOG_TAG, "openGallery: called");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALLERY_CODE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.v(LOG_TAG, "onActivityResult: called");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_CODE) {
            Uri imgUri = data.getData();
            if(imgUri != null) {
                mBitmapImg = decodeUri(imgUri, 400);
                partnerEditorImageView.setImageBitmap(mBitmapImg);
            }
            mByteImg = getByteFromBitmap(mBitmapImg);
            passData(mByteImg);
        }
    }

    /**
     * Helper method to convert image Uri to image Bitmap.
     */
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        Log.v(LOG_TAG, "decodeUri: called");
        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setByteImg(byte[] img) {
        Log.v(LOG_TAG, "setByteImg: called");
        if(img != null) {
            mByteImg = img;
            mBitmapImg = BitmapFactory.decodeByteArray(mByteImg , 0, mByteImg .length);
        }
    }

    private byte[] getByteFromBitmap(Bitmap b){
        Log.v(LOG_TAG, "getBitmapFromByte: called");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public void passData(byte[]  imgData) {
        Log.v(LOG_TAG, "passData: called \nData: " + imgData);
        dataPasser.onDataPass(imgData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "onAttach: called");
        dataPasser = (OnDataPass) context;
    }
}
