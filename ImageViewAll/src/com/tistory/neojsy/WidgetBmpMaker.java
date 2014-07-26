package com.tistory.neojsy;

import android.graphics.*;
import android.graphics.BitmapFactory.*;

public class WidgetBmpMaker {
	String filepath;
	int displayW;
	int displayH;
	boolean rotate;
	
	public WidgetBmpMaker(String _filePath, int _displayW, int _displayH, boolean _rotate){
		filepath = _filePath;
		displayW = _displayW;
		displayH = _displayH;
		rotate = _rotate;
	}
	
	public Bitmap getBitmap(){
		int imgWidth = getBitmapOfWidth(filepath);
		int imgHeight = getBitmapOfHeight(filepath);
		int lcdWidth = displayW;
		int lcdHeight = displayH;

		int dsWidth = 0;
		int dsHeight = 0;

		float imgB = (float) imgHeight / (float) imgWidth;
		float lcdB = (float) lcdHeight / (float) lcdWidth;

		// 비율이 LCD와 틀릴때 조정

		if (imgB == lcdB) {
			// 이미지와 LCD의 비율이 동일할 경우
			dsWidth = lcdWidth;
			dsHeight = lcdHeight;
		} else {
			if (imgB > lcdB) {
				// 높이를 기준으로
				dsHeight = lcdHeight;
				dsWidth = (int) ((float) imgWidth * ((float) lcdHeight / (float) imgHeight));
			} else {
				// 넓이를 기준으로
				dsWidth = lcdWidth;
				dsHeight = (int) ((float) imgHeight * ((float) lcdWidth / (float) imgWidth));
			}
		}
        
		Bitmap tempBitmap;
		
		if ((imgWidth + imgHeight) < (lcdWidth + lcdHeight * 3)) {
			// 2배 안으로 크기가 다를때
			tempBitmap = BitmapFactory.decodeFile(filepath);
//			bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth,	dsHeight, true);
		} else {
			// 2배보다 클때
			BitmapFactory.Options resizeOpts = new Options();
			resizeOpts.inSampleSize = 2;
			tempBitmap = BitmapFactory.decodeFile(filepath,	resizeOpts);
//			bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth,	dsHeight, true);
		}
		
		
		
//		BitmapFactory.Options resizeOpts = new Options();
//		resizeOpts.inSampleSize = 2;
//		Bitmap tempBitmap = BitmapFactory.decodeFile(filepath, resizeOpts);
		
		Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth, dsHeight, true);
		
		return bitmap;
	}
	
	/** Get Bitmap's Width **/
	public static int getBitmapOfWidth(String seletedFileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(seletedFileName, options);
			options.inJustDecodeBounds = false;
			return options.outWidth;
		} catch (Exception e) {
			return 0;
		}
	}

	/** Get Bitmap's height **/
	public static int getBitmapOfHeight(String seletedFileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(seletedFileName, options);
			options.inJustDecodeBounds = false;
			return options.outHeight;
		} catch (Exception e) {
			return 0;
		}
	}
}
