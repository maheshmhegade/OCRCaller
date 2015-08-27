package com.mmhocr.www.imagecaller;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mmhocr.www.ocrcaller.R;


/**
 * Created by mmh on 8/26/15.
 */
public class GalleryManager extends Fragment {

    private final int RESULT_LOAD_IMG = 1;
    private Context mContext;
    private ImageScanner mImageScanner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gallery_layout, container, false);

        return rootView;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void startActivity(){
        ((Activity) mContext).setContentView(R.layout.gallery_layout);
        ImageButton imageButton = (ImageButton)((Activity)mContext).findViewById(R.id.selectImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageScanner == null) {
                    mImageScanner = new ImageScanner();
                    mImageScanner.setContext(mContext);
                }
                mImageScanner.startActivity();
            }
        });
        // Create intent to Open Image applications like Gallery, Google Photos
    }

    public void stopActivity(){
        LinearLayout camLO = (LinearLayout)((Activity)mContext).findViewById(R.id.camera_lo);
        if(camLO != null) camLO.removeAllViewsInLayout();
    }

}
