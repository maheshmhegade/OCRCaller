package com.mmhocr.www.camcaller;

import com.mmhocr.www.ocrcaller.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by mmh on 8/25/15.
 */
public class CallOcrHandler extends Activity{
    private Button callButton;
    private Camera mCamera;
    private Context mContext;

    public void performCrop(Context context) {
        mContext = context;
        try {
            Bitmap image = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() +"/image.jpg");
            DrawView selectionView = (DrawView)((Activity)context).findViewById(R.id.imageView);
            Rect frame = selectionView.getSelection();
            FrameLayout selectionLO = (FrameLayout)((Activity)context).findViewById(R.id.selection_lo);
            if(selectionLO != null) selectionLO.removeAllViewsInLayout();
            LinearLayout camLO = (LinearLayout)((Activity)context).findViewById(R.id.camera_lo);
            if(camLO != null) camLO.removeAllViewsInLayout();

            ((Activity)context).setContentView(R.layout.result_layout);


            //Bitmap resizedbitmap = Bitmap.createBitmap(image, frame.width()-frame.left, frame.height()-frame.top, frame.right-frame.left, frame.bottom-frame.top);
            Bitmap resizedbitmap =  Bitmap.createBitmap(image,100 + frame.left*2, 350 + frame.top*2,(frame.right-frame.left)*2, 2*(frame.bottom-frame.top));


            String[] phoneNumbers = RecognizeText(resizedbitmap);
            final Spinner spinner = (Spinner)((Activity)context).findViewById(R.id.phone_number_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, phoneNumbers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            ImageButton button = (ImageButton) ((Activity)context).findViewById(R.id.callNumber);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String phoneNumber = (String)spinner.getSelectedItem();
                    callTheNumber(phoneNumber);
                }
            });
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
        }
    }

    public void scanImage(final Context context, String filePath){
        if(filePath == null) return;
        mContext = context;
        Bitmap image = BitmapFactory.decodeFile(filePath);
        ImageView customView = (ImageView)((Activity)context).findViewById(R.id.selectImage);

        String[] phoneNumbers = RecognizeText(image);
        final Spinner spinner = (Spinner)((Activity)context).findViewById(R.id.phone_numbers_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, phoneNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageButton button = (ImageButton) ((Activity)context).findViewById(R.id.callButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String phoneNumber = (String)spinner.getSelectedItem();
                callTheNumber(phoneNumber);
            }
        });
    }

    public String[] RecognizeText(Bitmap bitmap) {
        TessBaseAPI baseAPI = new TessBaseAPI();
        String characterWhitelist = "1234567890";
        TessDataManager.initTessTrainedData(mContext);
        baseAPI.init(TessDataManager.getTesseractFolder()+"/", "eng", TessBaseAPI.OEM_TESSERACT_ONLY);
        baseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
//        baseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, characterBlacklist);
        baseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, characterWhitelist);
        baseAPI.setImage(ReadFile.readBitmap(bitmap));
        String textResult = baseAPI.getUTF8Text();
        String[] phoneNumbers = textResult.split(" ");
//        phoneNumbers[0] = "9964810346";
//        Log.d("Recognized as", textResult);
        return phoneNumbers;
    }

    private void callTheNumber(String phoneNumber){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +phoneNumber));// + phoneNumber));
        ((Activity)mContext).startActivity(callIntent);
    }
}
