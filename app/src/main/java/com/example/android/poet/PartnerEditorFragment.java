package com.example.android.poet;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALLERY_CODE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_CODE) {
            Uri uri = data.getData();
            partnerEditorImageView.setImageURI(uri);
        }
    }
}
