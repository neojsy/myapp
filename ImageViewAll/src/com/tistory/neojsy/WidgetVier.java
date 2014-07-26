package com.tistory.neojsy;

import java.io.*;
import java.util.*;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.*;
import android.util.Log;
import android.widget.*;

public class WidgetVier extends AppWidgetProvider {

	private static String ACTION_WIDGET_IMAGECHANGE = "change";

	final static String folderListFileName = "wfoler.txt";
	final static String fileListFileName = "wfile.txt";
	static ArrayList<String> mDirList = new ArrayList<String>();
	static ArrayList<String> mFileList = new ArrayList<String>();
	static String seletedFileFullPath = null;
	final private static String WIDGETS = "widget1";

	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		// ���� ������Ʈ ����Ʈ�� �������� ��
		if (action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
			Log.d(null, "APPWIDGET_UPDATE");
		} else if (action.equals(ACTION_WIDGET_IMAGECHANGE)) {
			Log.d(null, "ACTION_WIDGET_IMAGECHANGE");
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			this.onUpdate(context, manager, manager
					.getAppWidgetIds(new ComponentName(context, getClass())));
		}
		// ���� ���� ����Ʈ�� �������� ��
		else if (action.equals("android.appwidget.action.APPWIDGET_DISABLED")) {
			Log.d(null, "APPWIDGET_DISABLED");
			UseDb db = new UseDb();
			db.setValue(context, WIDGETS, "NO");
		} else
			Log.d(null, "ETC");
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(null, "onUpdate");

		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateWidget(context, appWidgetManager, appWidgetId);
		}
	}

	public static void updateWidget(Context context,
			AppWidgetManager widgetMgr, int mAppWidgetId) {
		Log.d(null, "updateWidget");

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widgetviewer);

		UseDb db = new UseDb();

		if (db.getValue(context, WIDGETS, "NO").equals("ZERO")) {
			Log.d(null, "no image file");
			views.setTextViewText(R.id.wbutton1, "No Image File");
		} else if (db.getValue(context, WIDGETS, "NO").equals("NO")) {
			Log.d(null, "no widget configuration");
		} else {
			if (mDirList.size() == 0 || mFileList.size() == 0) {
				Log.d(null, "mdirlist was null. need reloadfile");
				setFileArray(context);
			}

			if (setFilePath()) {
				views.setTextViewText(R.id.wbutton1, " ");
				WidgetBmpMaker bmpmaker = new WidgetBmpMaker(
						seletedFileFullPath, 240, 300, false);
				Bitmap bitmap = bmpmaker.getBitmap();
				if (bitmap != null)
					views.setImageViewBitmap(R.id.widgetimage1, bitmap);
			} else {
				Log.d(null, "setFilePath() false. no image file");
				views.setTextViewText(R.id.wbutton1, "No Image File");
			}
		}
		PendingIntent testPendingIntent = PendingIntent.getBroadcast(context,
				0, new Intent(ACTION_WIDGET_IMAGECHANGE, Uri.EMPTY, context,
						WidgetVier.class), PendingIntent.FLAG_UPDATE_CURRENT);

		views.setOnClickPendingIntent(R.id.wbutton1, testPendingIntent);
		widgetMgr.updateAppWidget(mAppWidgetId, views);

	}

	public static void setList(Context context, String folder, String file,
			int fileNumber) {
		Log.d(null, "setList");

		if (fileNumber != 0) {
			UseDb db = new UseDb();
			db.setValue(context, WIDGETS, "YES");

			setFileArray(context);
		} else {
			UseDb db = new UseDb();
			db.setValue(context, WIDGETS, "ZERO");
		}
	}

	private static void setFileArray(Context context) {
		mDirList.clear();
		mFileList.clear();

		LoadFileToList lfile = new LoadFileToList(context);
		try {
			mDirList = lfile.getList(folderListFileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			mFileList = lfile.getList(fileListFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean setFilePath() {
		SelectRandomFile srf = new SelectRandomFile();
		String returnValue = srf.getFileName(mFileList, mDirList);
		if (returnValue.equals("NO")) {
			return false;
		} else {
			seletedFileFullPath = returnValue;
			return true;
		}
	}

}