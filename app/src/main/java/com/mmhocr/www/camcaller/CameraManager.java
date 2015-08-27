package com.mmhocr.www.camcaller;

import com.mmhocr.www.OcrUtil.OcrUtilFactory;
import com.mmhocr.www.ocrcaller.R;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mmh on 8/24/15.
 */
public class CameraManager extends Fragment {
    private TextRegionSelectActivity txtSelectActivity;
    private TextRegionSelectActivity selectRegion;
    private Display display;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private WindowManager manager;
    private LayoutInflater controlInflater;
    private LayoutParams layoutParamsControl;
    private View viewControl;
    private ImageButton callButton;
    private DrawView image;
    private CallOcrHandler ocrHandler;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_ocr, container, false);

        return rootView;
    }

    public void stopPreview(){
        if(context != null){
            FrameLayout selectionLO = (FrameLayout)((Activity)context).findViewById(R.id.selection_lo);
            if(selectionLO != null) selectionLO.removeAllViewsInLayout();
            LinearLayout camLO = (LinearLayout)((Activity)context).findViewById(R.id.camera_lo);
            if(camLO != null) camLO.removeAllViewsInLayout();
        }
        if(mPreview != null) mPreview.stopPreview();
//        if(image != null) image.removeAllViews();
        //        mPreview.stopPreview();
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() +"/image.jpg");

            if (pictureFile == null){
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                performCrop();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    };

    public void setContext(Context mContext){
        context = mContext;
    }

    private void performCrop(){
        if(ocrHandler == null) ocrHandler = OcrUtilFactory.getOcrRecognizer();
        ocrHandler.performCrop((Activity) context);
    }

    public void startActivity() {
        ((Activity)context).setContentView(R.layout.activity_ocr);

        init();
        startPreview();
        startResumePreview();
    }

    private void restartPreview(){
        image = (DrawView) ((Activity) context).findViewById(R.id.imageView);
        image.setRegion(getFramingRect());
        image.setScreenResolution(new Point(display.getWidth(), display.getHeight()));

        selectRegion.setImage(image);
        image.setAlpha(0x00);

    }

    private void startPreview() {
        // Create an instance of Camera
        if(mCamera == null) {
            mCamera = getCameraInstance();
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
                if ((((Activity) context)).getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(params);
            }
            // Create our Preview view and set it as the content of our activity.
        }
        mPreview = new CameraPreview(((Activity) context), mCamera);
        preview = (FrameLayout) ((Activity) context).findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        restartPreview();

    }

    private void startResumePreview(){

    }

    private void init() {
        if(selectRegion == null) {
            selectRegion = new TextRegionSelectActivity();
            manager = (WindowManager) ((Activity) context).getSystemService(Context.WINDOW_SERVICE);
            display = manager.getDefaultDisplay();
        }
        controlInflater = LayoutInflater.from(((Activity) context));
        layoutParamsControl
                = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        viewControl = controlInflater.inflate(R.layout.selection_layout, null);
        ((Activity) context).addContentView(viewControl, layoutParamsControl);

        if(callButton == null) {
            callButton = (ImageButton) ((Activity) context).findViewById(R.id.button_take_piture);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Camera.Parameters params = mCamera.getParameters();
//                if((getApplication()).getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
//                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

//                if((getApplication()).getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//                    params.remove(Camera.Parameters.FLASH_MODE_TORCH);
//                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                }
                    if (mCamera != null) {
                        mCamera.takePicture(null, null, mPicture);
//                    mCamera.release();
                    }
//                mCamera.setParameters(params);
//                mPreview.takePicture();
                }
            });
        }

    }

    public synchronized Rect getFramingRect() {
        Point screenResolution = new Point(display.getWidth(), display.getHeight());
        if (screenResolution == null) {
            // Called early, before init even finished
            return null;
        }
        int width = screenResolution.x * 3/5;
        int height = screenResolution.y * 1/5;
        int leftOffset = (screenResolution.x - width) / 2;
        int topOffset = (screenResolution.y - height) / 2;
        Rect framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        return framingRect;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
