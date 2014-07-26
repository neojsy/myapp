package com.neojsy.SimplePodcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddInputUrl extends Activity {
	String getPath;
	EditText editset;
	Editable data;
	ProgressDialog progress;
	ItemCast ic;
	String xmlURL;
	boolean addresult = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
		editset = new EditText(AddInputUrl.this);
		data = editset.getText();
		data.insert(0, "http://");
		drawPopup();
	}

	public void drawPopup() {

		new AlertDialog.Builder(AddInputUrl.this)
				.setTitle(getResources().getString(R.string.addurl_inputurl))
				.setMessage(
						"ex) http://old.ddanzi.com/appstream/ddradio.xml")
				.setIcon(android.R.drawable.ic_menu_edit).setView(editset)
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'NO' Button
						finish();
					}
				})
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						addCast();
					}
				}).show();

	}

	public void addCast() {
		Editable data = editset.getText();
		xmlURL = data.toString();

		if (!Util.isConnected(AddInputUrl.this)) {
			Toast.makeText(AddInputUrl.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT)
					.show();
		} else {
			progress = ProgressDialog.show(AddInputUrl.this, "", getResources()
					.getString(R.string.msg_loadingCastAddInput), true);

			Thread backGround = new Thread() {
				public void run() {
					ic = new ItemCast();
					XmlParser xp = new XmlParser();
					try {
						ic = xp.getCast(xmlURL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					ControlCastListFile cc = new ControlCastListFile(
							AddInputUrl.this);
					addresult = cc.Add2CastFile(ic, xmlURL);

					mCompleteHandler.sendEmptyMessage(0);
				}
			};
			backGround.start();
		}
	}

	public Handler mCompleteHandler = new Handler() {
		public void handleMessage(Message msg) {
			progress.dismiss();
			finishParse();
		}
	};

	private void finishParse() {
		if (addresult) {

		} else {
			Toast.makeText(this,
					getResources().getString(R.string.msg_duplicateCast),
					Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}
