package com.mmhocr.www.ocrcaller;

import com.mmhocr.www.camcaller.CameraManager;
import com.mmhocr.www.imagecaller.GalleryManager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private CameraManager mCamManager;
    private GalleryManager mGalleryManager;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                if(mCamManager == null ){
                    mCamManager = new CameraManager();
                    mCamManager.setContext(mContext);
                }
            case 1:
                if(mGalleryManager == null){
                    mGalleryManager = new GalleryManager();
                    mGalleryManager.setContext(mContext);
                }
        }

        return null;
    }

    public void startCamActivity(){
        if(mCamManager == null ) {
            mCamManager = new CameraManager();
            mCamManager.setContext(mContext);
            mCamManager.startActivity();
        }
        else{
            mCamManager.startActivity();
        }
    }

    public void stopCamActivity(){
        if(mCamManager != null) mCamManager.stopPreview();
    }

    public void startGalleryActivity(){
        if(mGalleryManager == null ) {
            mGalleryManager = new GalleryManager();
            mGalleryManager.setContext(mContext);
            mGalleryManager.startActivity();
        }
        else{
            mGalleryManager.startActivity();
        }
    }

    public void stopGalleryActivity(){
        if(mGalleryManager != null) mGalleryManager.stopActivity();
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}