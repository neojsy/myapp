package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class CheckOilMsgStr {
	String mText = null;
	String mWon = null;
	String mBrand = null;
	Boolean isOilMsg = false;
	static final String logTag = "OIL";

	CheckOilMsgStr(String text) {
		mText = text;
	}

	public boolean isOilMsg() {
		parceSMS();
		return isOilMsg;
	}

	public String getWon() {
		return mWon;
	}

	public String getBrand() {
		return mBrand;
	}

	private void parceSMS() {

		Util.print("parceSMS SMS original : "+mText);
		
		ArrayList<String> fa = new ArrayList<String>();
		FileRW f = new FileRW();
		fa = f.readFile_FilterData();
		
		boolean matched = false;
		
		mText = mText.replaceAll("\n", " ");
		
		for(int i=0;i<fa.size();i++){
			Util.print("SMS : "+mText);
			Util.print("Filter #"+i+" : "+fa.get(i));
			
			if((mText.matches(".*" + fa.get(i) + ".*"))){
				Util.print("Match OK!");
				matched = true;
			}
		}
		
		// check card
		if((mText.matches(".*" + "��" + ".*")) && matched){
			
			Log.d(logTag, "ī�� �޼��� ����");
			isOilMsg = true;
			
			//date remove
			int date;
			date = mText.indexOf("/");
			if(date != -1){
				String preS = mText.substring(0, date - 2);
				String subS = mText.substring(date + 3, mText.length());
				//Log.d(logTag, "preS : "+ preS);
				//Log.d(logTag, "subS : "+ subS);
				mText = preS + subS;
			}
			Util.print("parceSMS ��¥���� : "+mText);
			
			//time remove
			int time;
			time = mText.indexOf(":");
			if(time != -1){
				String preS = mText.substring(0, time - 2);
				String subS = mText.substring(time + 3, mText.length());
				//Log.d(logTag, "preS : "+ preS);
				//Log.d(logTag, "subS : "+ subS);
				mText = preS + subS;
			}
			Util.print("parceSMS �ð����� : "+mText);
			
			//remove ����
			int sum;
			sum = mText.indexOf("����");
			if(sum != -1){
				mText = mText.substring(0, sum);
			}
			Util.print("parceSMS �������� : "+mText);
			
			//remove ����
			int sum1;
			sum1 = mText.indexOf("����");
			if(sum1 != -1){
				mText = mText.substring(0, sum1);
			}
			Util.print("parceSMS �������� : "+mText);
			
			// brand parce
			int brand;
			brand = mText.indexOf("ī��");
			if(brand != -1){
				mBrand = mText.substring(brand - 2, brand);
			}
			else{
				mBrand = "�˼�����ī��";
			}
			Log.d(logTag, "ī��� : "+mBrand);
			
			//name remove
			int nim;
			nim = mText.indexOf("��");
			if(nim != -1){
				mText = mText.substring(nim, mText.length());
			}
			Util.print("parceSMS ������������� : "+mText);
			
			//trash remove
			int won;
			won = mText.indexOf("��");
			if(won != -1){
				mText = mText.substring(0, won);
			}
			Util.print("parceSMS ������������ : "+mText);			
			
			// won parce
			mWon = extract_numeral(mText);
			Log.d(logTag, "�ݾ� : "+mWon);

		} else {
			Log.d(logTag, "ī�� �޼��� �ƴ�");
			isOilMsg = false;
		}

	}

	public String extract_numeral(String str) {

		String numeral = "";
		if (str == null) {
			numeral = null;
		} else {
			String patternStr = "\\d"; // ���ڸ� �������� ����
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(str);

			while (matcher.find()) {
				numeral += matcher.group(0); // ������ ���ϰ� ��Ī�Ǹ� numeral ������ �ִ´�.
												// ���⼭�� ����!!
			}
		}

		return numeral;
	}
}
