package com.mmhocr.www.OcrUtil;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.mmhocr.www.camcaller.CallOcrHandler;

/**
 * Created by mmh on 8/26/15.
 */
public class OcrUtilFactory {
    public static CallOcrHandler ocrHandler;

    public static CallOcrHandler getOcrRecognizer(){
        if(OcrUtilFactory.ocrHandler == null) ocrHandler = new CallOcrHandler();
        return ocrHandler;
    }
}
