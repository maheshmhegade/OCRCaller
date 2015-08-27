package com.mmhocr.www.camcaller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by mmh on 8/24/15.
 */
public class TextRegionSelectActivity {

    File image;
    public TextRegionSelectActivity( ) {

    }

    public File createImageFile() throws IOException {

        if(image == null) {
            String imageFileName = "image";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }

        return image;
    }

    public void setImage(final DrawView imageview) {
        Bitmap photo = BitmapFactory.decodeFile("/sdcard/Pictures/image.jpg");
        imageview.setImageBitmap(photo);
    }


}
