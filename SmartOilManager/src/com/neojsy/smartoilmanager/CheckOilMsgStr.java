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
		if((mText.matches(".*" + "원" + ".*")) && matched){
			
			Log.d(logTag, "카드 메세지 맞음");
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
			Util.print("parceSMS 날짜제거 : "+mText);
			
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
			Util.print("parceSMS 시간제거 : "+mText);
			
			//remove 누계
			int sum;
			sum = mText.indexOf("누계");
			if(sum != -1){
				mText = mText.substring(0, sum);
			}
			Util.print("parceSMS 누계제거 : "+mText);
			
			//remove 누적
			int sum1;
			sum1 = mText.indexOf("누적");
			if(sum1 != -1){
				mText = mText.substring(0, sum1);
			}
			Util.print("parceSMS 누적제거 : "+mText);
			
			// brand parce
			int brand;
			brand = mText.indexOf("카드");
			if(brand != -1){
				mBrand = mText.substring(brand - 2, brand);
			}
			else{
				mBrand = "알수없는카드";
			}
			Log.d(logTag, "카드사 : "+mBrand);
			
			//name remove
			int nim;
			nim = mText.indexOf("님");
			if(nim != -1){
				mText = mText.substring(nim, mText.length());
			}
			Util.print("parceSMS 사용자정보제거 : "+mText);
			
			//trash remove
			int won;
			won = mText.indexOf("원");
			if(won != -1){
				mText = mText.substring(0, won);
			}
			Util.print("parceSMS 원나머지제거 : "+mText);			
			
			// won parce
			mWon = extract_numeral(mText);
			Log.d(logTag, "금액 : "+mWon);

		} else {
			Log.d(logTag, "카드 메세지 아님");
			isOilMsg = false;
		}

	}

	public String extract_numeral(String str) {

		String numeral = "";
		if (str == null) {
			numeral = null;
		} else {
			String patternStr = "\\d"; // 숫자를 패턴으로 지정
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(str);

			while (matcher.find()) {
				numeral += matcher.group(0); // 지정된 패턴과 매칭되면 numeral 변수에 넣는다.
												// 여기서는 숫자!!
			}
		}

		return numeral;
	}
}
