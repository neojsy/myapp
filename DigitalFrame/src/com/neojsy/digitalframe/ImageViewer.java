package com.neojsy.digitalframe;

import java.io.*;
import java.nio.channels.*;
import java.util.*;

import com.neojsy.digitalframe.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.ImageView.ScaleType;

public class ImageViewer<GifView> extends Activity {
	ArrayList<String> mDirList = new ArrayList<String>();
	ArrayList<String> mFileList = new ArrayList<String>();
	// File extStore = Environment.getExternalStorageDirectory();
	String seletedFileFullPath;
	String seletedFileName;
	String seletedFilePath;
	long selectedFileSize;
	int seletedFileIndex;
	boolean mTimeDisplay = false;
	boolean mDateDisplay = false;
	int mTextDisplayPos = 0;
	int mTextDisplaySize = 0;
	int mTextDisplayColor = 0;
	int SlideShowTime = 5;
	boolean ImageFull = true;
	boolean mWakelock = false;
	String folderListFileName = null;
	String fileListFileName = null;
	String mov1Path;


	boolean isAniGif = false;
	GifDecoder gifD;
	int gifFrameMax = 0;
	int gifIndex;
	int gifNextDelay;
	Timer timer = null;
	boolean decording = false;
	int gifError = 0;
	boolean autoStart = false;
	
	String fontPath = "digital.ttf";
	

    private BroadcastReceiver chargingReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(autoStart){
				finish();
			}
		}
    };
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview);
		
		Intent gintent = getIntent();

		String startType = gintent.getStringExtra("type");
		
		if(startType.equals("auto")){
			autoStart = true;
		}

		folderListFileName = Util.getFolderlistFileName();
		fileListFileName = Util.getFileListFileName();

		loadSetting();
	
		try {
			loadFolderListFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			loadfileListFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (setFilePath()) {
			drawNext();
			

			gifD = null;
			String lowName = seletedFileName.toLowerCase();
			if (lowName.endsWith(".gif")) {
				drawNext();
			}


		} else {
			Toast.makeText(ImageViewer.this,
					getResources().getString(R.string.view_msg_noimagefile),
					Toast.LENGTH_SHORT).show();
			finish();
		}

	}
	
	PowerManager.WakeLock wakeLock = null;
	
	public void onResume() {
		super.onResume();
		
		Util.print("ImageViewer onResume");
		
		if(mWakelock){
			if (wakeLock == null) {
				Util.print("ImageViewer Wakelock acquire");
				PowerManager powerManager = (PowerManager) getSystemService(ImageViewer.POWER_SERVICE);
				wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "digitalframe");
				wakeLock.acquire();
			}	
		}
		
		IntentFilter playFilter = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
		registerReceiver(chargingReceiver, playFilter); 
	}

	public void onPause(){
		super.onPause();
		
		Util.print("ImageViewer onPause");
		
		if (wakeLock != null) {
			Util.print("ImageViewer Wakelock release");
			wakeLock.release();
			wakeLock = null;
		}
		
		unregisterReceiver(chargingReceiver);
	}
	
	private void loadSetting(){
		
		SetData sData = new SetData(ImageViewer.this);
		int intervalvalue = sData.getInterval();
		
		switch (intervalvalue){
		case 0:
			SlideShowTime = 3;
			break;
		case 1:
			SlideShowTime = 5;
			break;
		case 2:
			SlideShowTime = 10;
			break;
		case 3:
			SlideShowTime = 15;
			break;
		case 4:
			SlideShowTime = 30;
			break;
		case 5:
			SlideShowTime = 60;
			break;			
		}		
		
		if(sData.getScreenSize() == 0){
			ImageFull = true;
		}
		else{
			ImageFull = false;
		}
		
		if(sData.getScreenOff()){
			mWakelock = true;
		}
		else{
			mWakelock = false;
		}
		
		if(sData.getClock()){
			mTimeDisplay = true;
			mDateDisplay = true;
		}
		else{
			mTimeDisplay = false;
			mDateDisplay = false;
		}
		
		mTextDisplayPos = sData.getCPos();
		
		mTextDisplaySize = sData.getCSize();
		
		int color = sData.getCColor();
		if(color == 0)
			mTextDisplayColor = Color.BLACK;
		else if(color == 1)
			mTextDisplayColor = Color.DKGRAY;
		else if(color == 2)
			mTextDisplayColor = Color.GRAY;
		else if(color == 3)
			mTextDisplayColor = Color.LTGRAY;
		else if(color == 4)
			mTextDisplayColor = Color.WHITE;
		else if(color == 5)
			mTextDisplayColor = Color.RED;
		else if(color == 6)
			mTextDisplayColor = Color.GREEN;
		else if(color == 7)
			mTextDisplayColor = Color.BLUE;
		else if(color == 8)
			mTextDisplayColor = Color.YELLOW;
		else if(color == 9)
			mTextDisplayColor = Color.CYAN;
		else if(color == 10)
			mTextDisplayColor = Color.MAGENTA;
		else
			mTextDisplayColor = Color.WHITE;
		
	}

	public void loadFolderListFromFile() throws IOException {
		FileInputStream fis = openFileInput(folderListFileName);
		byte[] data = new byte[fis.available()];
		while (fis.read(data) != -1) {
			;
		}
		fis.close();

		mDirList.clear();
		// byte -> str
		String str = new String(data, "UTF-8");

		// str -> str array
		String[] temps = str.split(", ");
		// [ , ] remove
		temps[0] = temps[0].replace("[", "");
		temps[temps.length - 1] = temps[temps.length - 1].replace("]", "");

		int i = 0;
		// str add to mDirList
		for (i = 0; i < temps.length; i++) {
			mDirList.add(temps[i]);
		}
	}

	public void loadfileListFromFile() throws IOException {
		FileInputStream fis = openFileInput(fileListFileName);
		byte[] data = new byte[fis.available()];
		while (fis.read(data) != -1) {
			;
		}
		fis.close();

		mFileList.clear();
		// byte -> str
		String str = new String(data, "UTF-8");

		if (str.equals("[]"))
			return;

		// str -> str array
		String[] temps = str.split(", ");
		// [ , ] remove
		temps[0] = temps[0].replace("[", "");
		temps[temps.length - 1] = temps[temps.length - 1].replace("]", "");

		int i = 0;
		// str add to mDirList
		for (i = 0; i < temps.length; i++) {
			mFileList.add(temps[i]);
		}
	}

	public String getFilePath() {
		return seletedFileFullPath;
	}

	public boolean setFilePath() {

		if (mFileList.size() == 0)
			return false;
		else {
			Random oRandom = new Random();
			int selectedFileIndex;
			boolean searchResult;

			do {
				selectedFileIndex = oRandom.nextInt(mFileList.size());
				searchResult = selectRandomImageFile(selectedFileIndex);
			} while (!searchResult);

			return true;
		}
	}

	public boolean selectRandomImageFile(int fileIndex) {

		String fileString = mFileList.get(fileIndex);
		String[] temps = fileString.split(":::");

		int folderIndex = Integer.parseInt(temps[0]);

		seletedFileName = temps[1];
		seletedFilePath = mDirList.get(folderIndex);
		seletedFileFullPath = seletedFilePath + "/" + seletedFileName;
		seletedFileIndex = fileIndex;

		File file = new File(seletedFileFullPath);

		selectedFileSize = file.length();

		if (file.length() == 0) {
			File filex = new File(seletedFileFullPath);
			filex.exists();
			return false;
		} else {
			return true;
		}

	}

	public void drawNext() {
		if (timer != null)
			timer.cancel();
		gifError = 0;
		setFilePath();
		gifD = null;
		System.gc();
		String lowName = seletedFileName.toLowerCase();
		if (lowName.endsWith(".gif")) {
			startGifDecordingThread();
			drawScreen();
		} else {
			isAniGif = false;
			drawScreen();
		}
	}


	public void startGifDecordingThread() {
		Thread backGround = new Thread() {
			public void run() {
				try {
					decording = true;
					// M.l(getFilePath());
					// M.l("GIF file decoding now");
					GifDecoder decoder = new GifDecoder();
					gifError = decoder.read(new FileInputStream(
							getFilePath()));
					if (gifError == 0) {
						ImageViewer.this.gifD = decoder;
						gifFrameMax = gifD.getFrameCount();
					} else {
						gifFrameMax = 1;
					}
					mCompleteHandler.sendEmptyMessage(0);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		backGround.start();
	}
	

	public Handler mCompleteHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (gifFrameMax > 1) {
					isAniGif = true;
					gifIndex = 0;
					decording = false;
					drawScreen();
				} else {
					isAniGif = false;
					decording = false;
					drawScreen();
				}
			} else if (msg.what == 1) {
				if (!decording)
					drawScreen();
			}
		}
	};
	

	class MyTask extends TimerTask {
		public void run() {

			if (gifD == null)
				return;
			mCompleteHandler.sendEmptyMessage(1);

		}
	}
	
	private void drawScreen(){

		int imgWidth = getBitmapOfWidth(getFilePath());
		int imgHeight = getBitmapOfHeight(getFilePath());
		int lcdWidth = getLCDwidth();
		int lcdHeight = getLCDheight();
		ImageView pic = (ImageView) findViewById(R.id.picture);
		
		if(ImageFull){
			pic.setScaleType(ScaleType.FIT_CENTER);
		}
		else{
			pic.setScaleType(ScaleType.FIT_XY);
		}
		
		
		
		if (decording) {
			int tsize = 30;
			String string = "Loading animated GIF...";
			
			TextView infot = (TextView) findViewById(R.id.ctime);
			infot.setText(string);
			
			return;
		}
		
		if (isAniGif) {
			if (gifD == null || decording) {
				// M.l("gifD "+gifD+" decording "+decording );
				return;
			}

			if (gifIndex >= gifFrameMax) {
				// M.l("over index");
				gifIndex = 0;
			}


			// draw i frame
			Bitmap bitmap = gifD.getFrame(gifIndex);
			// M.l("dsStartX "+ dsStartX+ " dsStartY "+ dsStartY+ " dsWidth"
			// + dsWidth+ " dsHeight "+ dsHeight);

			try {
				//Bitmap logo = Bitmap.createScaledBitmap(bitmap, dsWidth, dsHeight, true);
				//canvas.drawBitmap(logo, dsStartX, dsStartY, null);
				pic.setImageBitmap(bitmap);
			} catch (Error e) {
				System.gc();
			}

			// Bitmap logo = Bitmap.createScaledBitmap(bitmap, dsWidth,
			// dsHeight, true);

			// canvas.drawBitmap(logo, dsStartX, dsStartY, null);

			gifNextDelay = gifD.getDelay(gifIndex);

			if (gifIndex + 1 < gifFrameMax)
				gifIndex++;
			else
				gifIndex = 0;

			timer = new Timer();
			MyTask myTask = new MyTask();
			timer.schedule(myTask, gifNextDelay);
		} else {

			try {
				Bitmap bitmap;
				
				bitmap = BitmapFactory.decodeFile(getFilePath());
				pic.setImageBitmap(bitmap);

			} catch (Error e) {
				System.gc();
			}
		}


		if (mTimeDisplay) {

			String date = Util.getDateStr();
			String time = Util.getTimeStr();
			String ampm = Util.getAmPm();
			
			TextView stime = (TextView) findViewById(R.id.ctime);
			TextView sapm = (TextView) findViewById(R.id.campm);
			TextView sdate = (TextView) findViewById(R.id.cdate);
			
			switch (mTextDisplayPos){
			case 0: //center
				stime = (TextView) findViewById(R.id.ctime);
				sapm = (TextView) findViewById(R.id.campm);
				sdate = (TextView) findViewById(R.id.cdate);
				break;
			case 1: //left top
				stime = (TextView) findViewById(R.id.lttime);
				sapm = (TextView) findViewById(R.id.ltapmp);
				sdate = (TextView) findViewById(R.id.ltdate);
				break;
			case 2: //right top
				stime = (TextView) findViewById(R.id.rttime);
				sapm = (TextView) findViewById(R.id.rtampm);
				sdate = (TextView) findViewById(R.id.rtdate);
				break;
			case 3: //left bottom
				stime = (TextView) findViewById(R.id.lbtime);
				sapm = (TextView) findViewById(R.id.lbampm);
				sdate = (TextView) findViewById(R.id.lbdate);
				break;
			case 4: //right bottom
				stime = (TextView) findViewById(R.id.rbtime);
				sapm = (TextView) findViewById(R.id.rbampm);
				sdate = (TextView) findViewById(R.id.rbdate);
				break;
			}
			
			stime.setText(time);
			sdate.setText(date);
			sapm.setText(ampm);
			
			Typeface font = Typeface.createFromAsset(ImageViewer.this.getAssets(), fontPath);
			stime.setTypeface(font);
			sdate.setTypeface(font);
			sapm.setTypeface(font);
			
			stime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
			sdate.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize()/4);
			sapm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize()/5);
			
			stime.setTextColor(mTextDisplayColor);
			sdate.setTextColor(mTextDisplayColor);
			sapm.setTextColor(mTextDisplayColor);
			
			
		}
		

		String lowName = seletedFileName.toLowerCase();
		if (lowName.endsWith(".gif")) {
			if (gifError != 0) {
				switch (gifError) {
				case 1:
					Toast.makeText(ImageViewer.this, "GIF FILE FORMAT ERROR",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(ImageViewer.this, "GIF FILE OPEN ERROR",
							Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(ImageViewer.this, "OUT OF SYSTEM MEMORY",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, SlideShowTime * 1000);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			drawNext();
		}
	};
	
	private int getTextSize(){
		int endSize;
		int lcdw = getLCDwidth();
		//mTextDisplaySize

		
		float size = (float) (lcdw/3.0);
		
		if(mTextDisplaySize == 0){
			size = (float) (size*0.38);
		}
		else if(mTextDisplaySize == 1){
			size = (float) (size*0.51);
		}
		else if(mTextDisplaySize == 2){
			size = (float) (size*0.64);
		}
		else if(mTextDisplaySize == 3){
			size = (float) (size*0.77);
		}
		else
			size = (float) (size*0.9);
		
		endSize = (int) size;
		
		return endSize;
	}



	public int getLCDwidth() {
		Display dp = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		return dp.getWidth();
	}

	public int getLCDheight() {
		Display dp = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		return dp.getHeight();
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

	private static final int DIALOG_HELP = 1;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_HELP:
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

			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_text_entry, null);
			parentLinear.addView(textEntryView);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("?�턴?�보");
			builder.setView(scv);
			builder.setPositiveButton("OK", null);
			builder.show();
		}
		return null;
	}


	public String getMovePath(Context context) {
		SharedPreferences pref = context.getSharedPreferences("neojsy",
				Activity.MODE_PRIVATE);
		String value = pref.getString("movepath", Environment
				.getExternalStorageDirectory().toString());
		return value;
	}
}
