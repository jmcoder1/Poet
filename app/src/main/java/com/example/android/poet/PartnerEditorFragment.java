package com.example.android.poet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PartnerEditorFragment extends Fragment{

    private static final int GALLERY_CODE = 1;

    private Uri mImageUri;
    private CircleImageView partnerEditorImageView;

    public PartnerEditorFragment() {

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            openGallery();
            return false;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partner_editor_img, container, false);

        partnerEditorImageView = (CircleImageView) rootView.findViewById(R.id.partner_editor_img);
        partnerEditorImageView.setOnTouchListener(mTouchListener);

        return rootView;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    startActivityForResult(gallery, GALLERY_CODE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_CODE) {
            mImageUri = data.getData();
            partnerEditorImageView.setImageURI(mImageUri);
        }
    }
}
