package com.tistory.neojsy;

import java.io.*;
import java.nio.channels.*;
import java.util.*;

import com.tistory.neojsy.*;
import com.tistory.neojsy.R.drawable;

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

public class ImageViewer<GifView> extends Activity {
	ArrayList<String> mDirList = new ArrayList<String>();
	ArrayList<String> mFileList = new ArrayList<String>();
	// File extStore = Environment.getExternalStorageDirectory();
	String seletedFileFullPath;
	String seletedFileName;
	String seletedFilePath;
	long selectedFileSize;
	int seletedFileIndex;
	boolean fileInfo = false;
	private static final int SSoff = 1;
	private static final int SS2s = 2;
	private static final int SS3s = 4;
	private static final int SS4s = 6;
	private static final int SS5s = 8;
	int SlideShowMode = SSoff;
	String folderListFileName = null;
	String fileListFileName = null;
	private SettingInfo SettingInfo;
	private boolean settingDeleteOn;
	private boolean settingInfoOn;
	private boolean settingDelPopupOn;
	private boolean settingSsOn;
	private boolean settingMov1On;
	String mov1Path;

	String YES = "yes";
	String NO = "no";
	String DEL = "del";
	String INFO = "info";
	String DELPOP = "delpop";
	String SLIDE = "slide";
	String MOVE = "move";

	boolean isAniGif = false;
	GifDecoder gifD;
	int gifFrameMax = 0;
	int gifIndex;
	int gifNextDelay;
	Timer timer = null;
	boolean decording = false;
	int gifError = 0;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent gintent = getIntent();

		settingDeleteOn = getValue(this, DEL);
		settingInfoOn = getValue(this, INFO);
		settingDelPopupOn = getValue(this, DELPOP);
		settingSsOn = getValue(this, SLIDE);
		settingMov1On = getValue(this, MOVE);

		mov1Path = getMovePath(ImageViewer.this);

		folderListFileName = gintent.getStringExtra("folderListfileName");
		fileListFileName = gintent.getStringExtra("fileListfileName");

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
			MyView vw = new MyView(this);
			setContentView(vw);

			gifD = null;
			String lowName = seletedFileName.toLowerCase();
			if (lowName.endsWith(".gif")) {
				vw.drawNext();
			}

			Toast.makeText(
					ImageViewer.this,
					mFileList.size()
							+ getResources().getString(
									R.string.view_msg_folderloaded),
					Toast.LENGTH_SHORT).show();
			showHelp(this);
		} else {
			Toast.makeText(ImageViewer.this,
					getResources().getString(R.string.view_msg_noimagefile),
					Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	public void onResume() {
		super.onResume();
		mov1Path = getMovePath(ImageViewer.this);
	}

	public void showHelp(Context context) {
		String nowVersion = "3.1";
		SharedPreferences pref = context.getSharedPreferences("neojsy",
				Activity.MODE_PRIVATE);
		String oldVersion = pref.getString("viewver", "0");

		try {
			if (oldVersion.equals(nowVersion) == false) {
				showDialog(DIALOG_HELP);
				SharedPreferences.Editor ed = pref.edit();
				ed.putString("viewver", nowVersion);
				ed.commit();
			}
		} catch (Exception e) {
		}
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
			Toast.makeText(
					ImageViewer.this,
					seletedFileFullPath
							+ getResources().getString(R.string.view_msg_clash),
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}

	public boolean delFile() {
		File file = new File(getFilePath());

		if (file.exists() == false) {
			Toast.makeText(ImageViewer.this,
					getFilePath() + " file not exixts!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			if (file.delete()) {
				Toast.makeText(
						ImageViewer.this,
						getFilePath()
								+ " "
								+ getResources().getString(
										R.string.view_msg_deleted),
						Toast.LENGTH_SHORT).show();
				return true;
			} else {
				Toast.makeText(ImageViewer.this,
						getFilePath() + " is not deleted!", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
	}

	public boolean moveFile(String fileName) throws IOException {

		File apkFile = new File(getFilePath());
		File decFile = new File(mov1Path + "/" + fileName);

		FileInputStream inputStream = new FileInputStream(apkFile);
		FileOutputStream outputStream = new FileOutputStream(decFile);

		FileChannel fcin = inputStream.getChannel();
		FileChannel fcout = outputStream.getChannel();

		long size = fcin.size();

		fcin.transferTo(0, size, fcout);
		fcout.close();
		fcin.close();
		outputStream.close();
		inputStream.close();
		apkFile.delete();

		Toast.makeText(
				ImageViewer.this,
				getFilePath() + " "
						+ getResources().getString(R.string.view_msg_moved),
				Toast.LENGTH_SHORT).show();

		return true;
	}

	protected class MyView extends View {
		// 버튼 로드
		Resources res = getResources();
		BitmapDrawable bD = (BitmapDrawable) res.getDrawable(R.drawable.delete);
		Bitmap bDel = bD.getBitmap();
		BitmapDrawable bI = (BitmapDrawable) res.getDrawable(R.drawable.info);
		Bitmap bInfo = bI.getBitmap();
		BitmapDrawable bP = (BitmapDrawable) res.getDrawable(R.drawable.play);
		Bitmap bPlay = bP.getBitmap();
		BitmapDrawable bSS = (BitmapDrawable) res.getDrawable(R.drawable.ss);
		Bitmap bSs = bSS.getBitmap();
		BitmapDrawable bM1 = (BitmapDrawable) res.getDrawable(R.drawable.mov1);
		Bitmap bMov1 = bM1.getBitmap();

		float butGap = 20;
		float butWidth = bD.getIntrinsicWidth();
		float butHeight = bD.getIntrinsicHeight();
		float butDelSx = butGap;
		float butDelSy = getLCDheight() - butGap - butHeight;
		float butInfoSx = getLCDwidth() - butGap - butWidth;
		float butInfoSy = getLCDheight() - butGap - butHeight;
		float butPlaySx = (getLCDwidth() / 2) - (bPlay.getWidth() / 2);
		float butPlaySy = getLCDheight() - butGap - butHeight;
		float butSsSx = (getLCDwidth() / 2) - (bPlay.getWidth() / 2);
		float butSsSy = getLCDheight() - butGap - butHeight;
		float butMov1Sx = butGap;
		float butMov1Sy = butGap;

		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 1초간의 지연 시간을 두어 1초후에 자기자신이 호출 되도록 한다.
				if (SlideShowMode != SSoff) {
					drawNext();
					handler.removeMessages(0);
					handler.sendEmptyMessageDelayed(0, SlideShowMode * 1000);
				}
			}
		};

		public MyView(Context context) {
			super(context);
		}

		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (SlideShowMode == SSoff) {
					if (settingDeleteOn
							&& (event.getRawX() > butDelSx
									&& event.getRawX() < butDelSx + butWidth
									&& event.getRawY() > butDelSy && event
									.getRawY() < butDelSy + butWidth)) {
						// file delete
						hadleDel();
					} else if (settingSsOn
							&& (event.getRawX() > butPlaySx
									&& event.getRawX() < butPlaySx + butWidth
									&& event.getRawY() > butPlaySy && event
									.getRawY() < butPlaySy + butWidth)) {
						// slide show on
						SlideShowMode = SS3s;
						invalidate();
						handler.sendEmptyMessageDelayed(0, 1000);
						Toast.makeText(
								ImageViewer.this,
								getResources().getString(
										R.string.view_msg_ssstart),
								Toast.LENGTH_LONG).show();
					} else if (settingInfoOn
							&& (event.getRawX() > butInfoSx
									&& event.getRawX() < butInfoSx + butWidth
									&& event.getRawY() > butInfoSy && event
									.getRawY() < butInfoSy + butWidth)) {
						// file info
						if (fileInfo)
							fileInfo = false;
						else
							fileInfo = true;
						invalidate();
					} else if (settingMov1On
							&& (event.getRawX() > butMov1Sx
									&& event.getRawX() < butMov1Sx + butWidth
									&& event.getRawY() > butMov1Sy && event
									.getRawY() < butMov1Sy + butWidth)) {
						// file move 1
						handleMove(1);
					} else {
						// next file view
						drawNext();
					}

				} else {
					if (event.getRawX() > butSsSx
							&& event.getRawX() < butSsSx + butWidth
							&& event.getRawY() > butSsSy
							&& event.getRawY() < butSsSy + butWidth) {
						// slide show time change
						if (SlideShowMode == SS2s)
							SlideShowMode = SS3s;
						else if (SlideShowMode == SS3s)
							SlideShowMode = SS4s;
						else if (SlideShowMode == SS4s)
							SlideShowMode = SS5s;
						else if (SlideShowMode == SS5s)
							SlideShowMode = SS2s;
						Toast.makeText(
								ImageViewer.this,
								getResources().getString(
										R.string.view_msg_sstimechanged)
										+ " "
										+ SlideShowMode
										+ getResources().getString(
												R.string.view_msg_sec),
								Toast.LENGTH_SHORT).show();
					} else {
						// slide show end
						SlideShowMode = SSoff;
						invalidate();
						Toast.makeText(
								ImageViewer.this,
								getResources().getString(
										R.string.view_msg_ssfinish),
								Toast.LENGTH_SHORT).show();
					}
				}
				return true;
			}
			return false;
		}

		public void hadleDel() {
			if (settingDelPopupOn) {
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						ImageViewer.this);
				alt_bld.setMessage(
						seletedFileName
								+ " "
								+ getResources().getString(
										R.string.view_msg_delete))
						.setCancelable(false)
						.setIcon(R.drawable.delete)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'Yes' Button
										if (delFile()) {
											mFileList.remove(seletedFileIndex);

											try {
												FileOutputStream fos = openFileOutput(
														fileListFileName,
														Context.MODE_WORLD_READABLE);
												fos.write(mFileList.toString()
														.getBytes());
												fos.close();
											} catch (Exception e) {
											}

											drawNext();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'NO' Button
										dialog.cancel();
									}
								});
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				// alert.setTitle("Title");
				// Icon for AlertDialog
				alert.show();

			} else {
				if (delFile()) {
					mFileList.remove(seletedFileIndex);

					try {
						FileOutputStream fos = openFileOutput(fileListFileName,
								Context.MODE_WORLD_READABLE);
						fos.write(mFileList.toString().getBytes());
						fos.close();
					} catch (Exception e) {
					}

					drawNext();
				}
			}

		}

		public void handleMove(int movNum) {
			// file move 1
			if (settingMov1On) {
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						ImageViewer.this);
				alt_bld.setTitle(getResources().getString(
						R.string.view_msg_move));
				alt_bld.setMessage(
						getResources().getString(R.string.view_msg_moveq)
								+ "\n" + mov1Path)
						.setCancelable(false)
						.setIcon(R.drawable.mov1)
						.setPositiveButton(
								getResources().getString(
										R.string.view_msg_moveok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for '1' Button
										try {
											if (moveFile(seletedFileName)) {
												mFileList
														.remove(seletedFileIndex);

												drawNext();

												try {
													FileOutputStream fos = openFileOutput(
															fileListFileName,
															Context.MODE_WORLD_READABLE);
													fos.write(mFileList
															.toString()
															.getBytes());
													fos.close();
												} catch (Exception e) {
												}

											}
										} catch (IOException e) {
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}
									}
								})
						.setNeutralButton(
								getResources().getString(
										R.string.view_msg_movefolder),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for '2' Button
										Intent intent = new Intent(
												ImageViewer.this, MoveEx.class);
										intent.putExtra("path", mov1Path);
										startActivity(intent);
									}
								})
						.setNegativeButton(
								getResources().getString(
										R.string.view_msg_moveback),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for '3' Button
										dialog.cancel();
									}
								});
				AlertDialog alert = alt_bld.create();
				alert.show();

			} else {

				try {
					if (moveFile(seletedFileName)) {

						mFileList.remove(seletedFileIndex);

						drawNext();

						try {
							FileOutputStream fos = openFileOutput(
									fileListFileName,
									Context.MODE_WORLD_READABLE);
							fos.write(mFileList.toString().getBytes());
							fos.close();
						} catch (Exception e) {
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				invalidate();
			} else {
				isAniGif = false;
				invalidate();
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
						invalidate();
					} else {
						isAniGif = false;
						decording = false;
						invalidate();
					}
				} else if (msg.what == 1) {
					if (!decording)
						invalidate();
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

		public void onDraw(Canvas canvas) {

			int imgWidth = getBitmapOfWidth(getFilePath());
			int imgHeight = getBitmapOfHeight(getFilePath());
			int lcdWidth = getLCDwidth();
			int lcdHeight = getLCDheight();
			int dsWidth = 0;
			int dsHeight = 0;
			int dsStartX = 0;
			int dsStartY = 0;

			if (decording) {
				int tsize = 30;
				String string = "Loading animated GIF...";
				Paint Pnt = new Paint();
				Pnt.setTextSize(tsize);

				float textWidth = Pnt.measureText(string);
				drawOutlineText(canvas, string, (int) ((lcdWidth - textWidth) / 2),
						(lcdHeight - 26) / 2, tsize, Color.WHITE);
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
				/*
				 * int shortK; int longK;
				 * 
				 * if (imgWidth > imgHeight) { longK = imgWidth; shortK =
				 * imgHeight; } else { longK = imgHeight; shortK = imgWidth; }
				 * 
				 * float imgB = (float) longK / (float) shortK; float lcdB =
				 * (float) lcdHeight / (float) lcdWidth;
				 * 
				 * // 비율이 LCD와 틀릴때 조정
				 * 
				 * if (imgB == lcdB) { // 이미지와 LCD의 비율이 동일할 경우 dsWidth =
				 * lcdWidth; dsHeight = lcdHeight; dsStartX = 0; dsStartY = 0; }
				 * else { if (imgB > lcdB) { // 높이를 기준으로 dsHeight = lcdHeight;
				 * dsWidth = (int) ((float) shortK * ((float) lcdHeight /
				 * (float) longK)); dsStartY = 0; dsStartX = (lcdWidth -
				 * dsWidth) / 2; } else { // 넓이를 기준으로 dsWidth = lcdWidth;
				 * dsHeight = (int) ((float) longK * ((float) lcdWidth / (float)
				 * shortK)); dsStartX = 0; dsStartY = (lcdHeight - dsHeight) /
				 * 2; } }
				 * 
				 * //draw i frame Bitmap bitmap = gifD.getFrame(gifIndex);
				 * bitmap = imgRotate(bitmap, imgWidth, imgHeight);
				 * canvas.drawBitmap(bitmap, dsStartX, dsStartY, null);
				 */

				float imgB = (float) imgHeight / (float) imgWidth;
				float lcdB = (float) lcdHeight / (float) lcdWidth;

				// 비율이 LCD와 틀릴때 조정

				if (imgB == lcdB) {
					// 이미지와 LCD의 비율이 동일할 경우
					dsWidth = lcdWidth;
					dsHeight = lcdHeight;
					dsStartX = 0;
					dsStartY = 0;
				} else {
					if (imgB > lcdB) {
						// 높이를 기준으로
						dsHeight = lcdHeight;
						dsWidth = (int) ((float) imgWidth * ((float) lcdHeight / (float) imgHeight));
						dsStartY = 0;
						dsStartX = (lcdWidth - dsWidth) / 2;
					} else {
						// 넓이를 기준으로
						dsWidth = lcdWidth;
						dsHeight = (int) ((float) imgHeight * ((float) lcdWidth / (float) imgWidth));
						dsStartX = 0;
						dsStartY = (lcdHeight - dsHeight) / 2;
					}
				}

				// draw i frame
				Bitmap bitmap = gifD.getFrame(gifIndex);
				// M.l("dsStartX "+ dsStartX+ " dsStartY "+ dsStartY+ " dsWidth"
				// + dsWidth+ " dsHeight "+ dsHeight);

				try {
					Bitmap logo = Bitmap.createScaledBitmap(bitmap, dsWidth,
							dsHeight, true);
					canvas.drawBitmap(logo, dsStartX, dsStartY, null);
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

				int shortK;
				int longK;

				if (imgWidth > imgHeight) {
					longK = imgWidth;
					shortK = imgHeight;
				} else {
					longK = imgHeight;
					shortK = imgWidth;
				}

				float imgB = (float) longK / (float) shortK;
				float lcdB = (float) lcdHeight / (float) lcdWidth;

				// 비율이 LCD와 틀릴때 조정

				if (imgB == lcdB) {
					// 이미지와 LCD의 비율이 동일할 경우
					dsWidth = lcdWidth;
					dsHeight = lcdHeight;
					dsStartX = 0;
					dsStartY = 0;
				} else {
					if (imgB > lcdB) {
						// 높이를 기준으로
						dsHeight = lcdHeight;
						dsWidth = (int) ((float) shortK * ((float) lcdHeight / (float) longK));
						dsStartY = 0;
						dsStartX = (lcdWidth - dsWidth) / 2;
					} else {
						// 넓이를 기준으로
						dsWidth = lcdWidth;
						dsHeight = (int) ((float) longK * ((float) lcdWidth / (float) shortK));
						dsStartX = 0;
						dsStartY = (lcdHeight - dsHeight) / 2;
					}
				}

				

				try {
					Bitmap bitmap;

					if (imgWidth == lcdWidth && imgHeight == lcdHeight) {
						// LCD와이미지 크기가 같을때
						bitmap = BitmapFactory.decodeFile(getFilePath());
						bitmap = imgRotate(bitmap, imgWidth, imgHeight);
					} else if ((imgWidth + imgHeight) < (lcdWidth + lcdHeight * 2)) {
						// 2배 안으로 크기가 다를때
						Bitmap tempBitmap = BitmapFactory
								.decodeFile(getFilePath());
						tempBitmap = imgRotate(tempBitmap, imgWidth, imgHeight);
						bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth,
								dsHeight, true);
					} else {
						// 2배보다 클때
						BitmapFactory.Options resizeOpts = new Options();
						resizeOpts.inSampleSize = 2;
						Bitmap tempBitmap = BitmapFactory.decodeFile(
								getFilePath(), resizeOpts);
						tempBitmap = imgRotate(tempBitmap, imgWidth, imgHeight);
						bitmap = Bitmap.createScaledBitmap(tempBitmap, dsWidth,
								dsHeight, true);
					}

					canvas.drawBitmap(bitmap, dsStartX, dsStartY, null);

				} catch (Error e) {
					System.gc();
				}
			}

			if (fileInfo) {
				int textY = 150;
				int textX = 10;
				int textSize = 20;
				int gap = 8;
				int color1 = Color.LTGRAY;
				int color2 = Color.WHITE;

				drawOutlineText(canvas, "Original Image Size", textX, textY,
						textSize, color1);
				textY = textY + textSize + gap;
				drawOutlineText(canvas, "Width : " + imgWidth + " Height : "
						+ imgHeight, textX, textY, textSize, color2);
				textY = textY + textSize + gap;

				drawOutlineText(canvas, "Phone Display Size", textX, textY,
						textSize, color1);
				textY = textY + textSize + gap;
				drawOutlineText(canvas, "Width : " + lcdWidth + " Height : "
						+ lcdHeight, textX, textY, textSize, color2);
				textY = textY + textSize + gap;

				drawOutlineText(canvas, "Scaled Image Size", textX, textY,
						textSize, color1);
				textY = textY + textSize + gap;
				drawOutlineText(canvas, "Width : " + dsWidth + " Height : "
						+ dsHeight, textX, textY, textSize, color2);
				textY = textY + textSize + gap;

				drawOutlineText(canvas, "File Size", textX, textY, textSize,
						color1);
				textY = textY + textSize + gap;
				drawOutlineText(canvas, selectedFileSize + " Byte", textX,
						textY, textSize, color2);
				textY = textY + textSize + gap;

				drawOutlineText(canvas, "File Name", textX, textY, textSize,
						color1);
				textY = textY + textSize + gap;
				drawOutlineText(canvas, seletedFileName, textX, textY,
						textSize, color2);
				textY = textY + textSize + gap;

				drawOutlineText(canvas, "File Path", textX, textY, textSize,
						color1);
				textY = textY + textSize + gap;
				String newPath = seletedFilePath.substring(5,
						seletedFilePath.length());
				drawOutlineText(canvas, newPath, textX, textY, textSize, color2);

			}

			// 버튼 출력
			if (SlideShowMode == SSoff) {
				if (settingDeleteOn)
					canvas.drawBitmap(bDel, butDelSx, butDelSy, null);
				if (settingSsOn)
					canvas.drawBitmap(bPlay, butPlaySx, butPlaySy, null);
				if (settingInfoOn)
					canvas.drawBitmap(bInfo, butInfoSx, butInfoSy, null);
				if (settingMov1On)
					canvas.drawBitmap(bMov1, butMov1Sx, butMov1Sy, null);
			} else {
				canvas.drawBitmap(bSs, butSsSx, butSsSy, null);
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

		}
	}

	public void drawOutlineText(Canvas canvas, String string, int sx, int sy,
			int tsize, int color) {

		Paint Pnt = new Paint();
		Pnt.setTextSize(tsize);
		Pnt.setTypeface(Typeface.SERIF);
		Pnt.setAntiAlias(true);

		float textWidth = Pnt.measureText(string);
		int viewWidth = getLCDwidth() - sx;

		if (viewWidth < textWidth) {
			do {
				tsize = tsize - 1;
				Pnt.setTextSize(tsize);
				textWidth = Pnt.measureText(string);
			} while ((viewWidth < textWidth) && (tsize > 5));
		}

		Pnt.setTextAlign(Paint.Align.LEFT);
		Pnt.setColor(Color.BLACK);
		canvas.drawText(string, sx, sy + 1, Pnt);
		canvas.drawText(string, sx, sy - 1, Pnt);
		canvas.drawText(string, sx + 1, sy, Pnt);
		canvas.drawText(string, sx - 1, sy, Pnt);
		Pnt.setColor(color);
		canvas.drawText(string, sx, sy, Pnt);
	}

	private Bitmap imgRotate(Bitmap bmp, int w, int h) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		if (w > h) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width,
					height, matrix, true);
			bmp.recycle();

			return resizedBitmap;
		} else
			return bmp;

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
		// onCreate 에서 showDialog 메소드에서 넘긴 id 값에 따라서 다이얼로그를 띄웁니다.
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
			// builder.setTitle("패턴정보");
			builder.setView(scv);
			builder.setPositiveButton("OK", null);
			builder.show();
		}
		return null;
	}

	public boolean getValue(Context context, String item) {
		SharedPreferences pref = context.getSharedPreferences("neojsy",
				Activity.MODE_PRIVATE);
		String value = pref.getString(item, YES);
		if (value.equals(YES)) {
			return true;
		} else {
			return false;
		}
	}

	public String getMovePath(Context context) {
		SharedPreferences pref = context.getSharedPreferences("neojsy",
				Activity.MODE_PRIVATE);
		String value = pref.getString("movepath", Environment
				.getExternalStorageDirectory().toString());
		return value;
	}
}
