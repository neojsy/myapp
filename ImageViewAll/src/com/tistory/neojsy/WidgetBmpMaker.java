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

		// ������ LCD�� Ʋ���� ����

		if (imgB == lcdB) {
			// �̹����� LCD�� ������ ������ ���
			dsWidth = lcdWidth;
			dsHeight = lcdHeight;
		} else {
			if (imgB > lcdB) {
				// ���̸� ��������
				dsHeight = lcdHeight;
				dsWidth = (int) ((float) imgWidth * ((float) lcdHeight / (float) imgHeight));
			} else {
				// ���̸� ��������
				dsWidth = lcdWidth;
				dsHeight = (int) ((float) imgHeight * ((float) lcdWidth / (float) imgWidth));
			}
		}
        
		Bitmap tempBitmap;
		
		if ((imgWidth + imgHeight) < (lcdWidth + lcdHeight * 3)) {
			// 2�� ������ ũ�Ⱑ �ٸ���
			tempBitmap = BitmapFactory.decodeFile(filepath);
//			bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth,	dsHeight, true);
		} else {
			// 2�躸�� Ŭ��
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
