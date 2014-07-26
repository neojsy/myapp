package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FileRW f = new FileRW();
		f.fileMoveToNewPath();
		
		//eventPopup();
		// saveTestData();
		// testSMS("삼성카드승인 09/25 23:28 128,000원 일시불 (주)동원석유주유 *누적 6,250,167원");
		// testSMS("기업BC(9*3*)조**님. 09/30 08:45.일시불.39,300원.32TOP적립예정.현대오일뱅크(주)경산현");
		// testSMS("씨티카드(6*7*)정상영님. 09/30 08:45 일시불 39,300원 누계1,234,234원 금천주유소");
	}

	private void eventPopup() {
		
		Random oRandom = new Random();
		int rand = oRandom.nextInt(7);

		int res = R.drawable.t01;
		switch(rand){
		case 0:
			res = R.drawable.t01;
			break;
		case 1:
			res = R.drawable.t02;
			break;
		case 2:
			res = R.drawable.t03;
			break;
		case 3:
			res = R.drawable.t04;
			break;
		case 4:
			res = R.drawable.t05;
			break;
		case 5:
			res = R.drawable.t06;
			break;
		case 6:
			res = R.drawable.t07;
			break;
		}
		
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(res));
		dialog.show();
	}

	private void testSMS(String msg) {

		CheckOilMsgStr cos = new CheckOilMsgStr(msg);
		if (cos.isOilMsg()) {

			String won = cos.getWon();
			String brand = cos.getBrand();

			Intent intent = new Intent(MainActivity.this, SaveData.class);
			intent.putExtra("SMS", " ");
			intent.putExtra("WON", won);
			intent.putExtra("BRAND", brand);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

		}
	}

	private void saveTestData() {

		ArrayList<OilData> od = new ArrayList<OilData>();
		FileRW f = new FileRW();
		od = f.readFile_OilData();

		GregorianCalendar test = new GregorianCalendar();
		od.add(0, new OilData(" ", "30000", "한화", test));
		od.add(0, new OilData(" ", "30000", "한화", test));
		od.add(0, new OilData(" ", "30000", "한화", test));
		od.add(0, new OilData(" ", "30000", "한화", test));
		od.add(0, new OilData(" ", "30000", "한화", test));

		f.writeFile_OilData(od);
	}

	public void oilinginfo(View v) {
		Intent intent = new Intent(MainActivity.this, OilingInfo.class);
		startActivity(intent);
	}

	public void yunbiinfo(View v) {
		Intent intent = new Intent(MainActivity.this, YunbiInfo.class);
		startActivity(intent);
	}

	public void repairinfo(View v) {
		Intent intent = new Intent(MainActivity.this, RepairInfo.class);
		startActivity(intent);
	}

	public void inputnow(View v) {
		Intent intent = new Intent(MainActivity.this, InputData.class);
		startActivity(intent);
	}

	public void appinfo(View v) {
		Intent intent = new Intent(MainActivity.this, RepairInfo.class);
		startActivity(intent);
	}

	private void DrawinfoPopup() {

		String dev = getResources().getString(R.string.main_info_name) + " : "
				+ getResources().getString(R.string.main_info_maker) + "\n"
				+ getString(R.string.main_info_email) + " : "
				+ getResources().getString(R.string.main_info_email_url) + "\n"
				+ getResources().getString(R.string.main_info_blog) + " : "
				+ getResources().getString(R.string.main_info_blog_url) + "\n"
				+ "\n" + "\n" + "\n"
				+ getResources().getString(R.string.main_info_my);

		new AlertDialog.Builder(MainActivity.this)
				.setTitle(getResources().getString(R.string.main_info_tk))
				.setMessage(dev)
				.setIcon(R.drawable.tk)

				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						})
				.setNeutralButton(
						getResources().getString(R.string.main_updateinfo),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								drawDetailPopup();
							}
						}).show();
		/*
		 * .setNegativeButton( getResources().getString(R.string.mnu_jeonghwa),
		 * new DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int whichButton) { Intent intent =
		 * new Intent( MainActivity.this, Teatime2.class);
		 * startActivity(intent); } }).show();
		 */
	}

	public void drawDetailPopup() {
		ScrollView scv = new ScrollView(this);
		scv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		scv.setMinimumHeight(100);
		LinearLayout parentLinear = new LinearLayout(this);
		parentLinear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		parentLinear.setOrientation(LinearLayout.VERTICAL);
		scv.addView(parentLinear);

		LinearLayout linear;
		TextView tv;

		linear = new LinearLayout(this);
		linear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linear.setOrientation(LinearLayout.HORIZONTAL);
		parentLinear.addView(linear);
		// property name
		tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		// tv.setWidth(80);
		tv.setPadding(5, 5, 5, 5);
		// tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.setText(getResources().getString(R.string.update0001));
		tv.setTextColor(Color.CYAN);
		linear.addView(tv);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.main_updateinfo));
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setView(scv);
		builder.setPositiveButton("OK", null);
		builder.show();
	}

	public void mOnClickinfo(View v) {
		DrawinfoPopup();
	}

}
