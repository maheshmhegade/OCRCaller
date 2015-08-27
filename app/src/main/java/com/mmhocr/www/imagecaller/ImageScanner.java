package com.mmhocr.www.imagecaller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mmhocr.www.OcrUtil.OcrUtilFactory;
import com.mmhocr.www.camcaller.CallOcrHandler;

/**
 * Created by mmh on 8/26/15.
 */
public class ImageScanner extends Activity{

    private Context mContext;
    private final int RESULT_LOAD_IMG = 1;
    public void setContext(Context context){
        mContext = context;
    }

    public void startActivity(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        ((Activity)mContext).startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == ((Activity)mContext).RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = ((Activity)mContext).getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                CallOcrHandler ocrHandler = OcrUtilFactory.getOcrRecognizer();
                ocrHandler.scanImage(mContext, imgDecodableString);

            } else {
            }
        } catch (Exception e) {
        }

    }
}
