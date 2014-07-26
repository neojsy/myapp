package com.neojsy.smartoilmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.util.Log;

public class FileRW {
	static final String logTag = "OIL";
	String oldfileName = "/mnt/sdcard/od.ser";
	String oldfileNamec = "/mnt/sdcard/cd.ser";
	String oldfileNamer = "/mnt/sdcard/rd.ser";
	String oldfileNamef = "/mnt/sdcard/fd.ser";

	String fileName = "/mnt/sdcard/smartoilmanager/od.ser";
	String fileNamec = "/mnt/sdcard/smartoilmanager/cd.ser";
	String fileNamer = "/mnt/sdcard/smartoilmanager/rd.ser";
	String fileNamef = "/mnt/sdcard/smartoilmanager/fd.ser";

	String folder = "/mnt/sdcard/smartoilmanager";

	FileRW() {

	}

	public void fileMoveToNewPath() {
		File old1 = new File(oldfileName);
		File old2 = new File(oldfileNamec);
		File old3 = new File(oldfileNamer);
		File old4 = new File(oldfileNamef);
		File new1 = new File(fileName);
		File new2 = new File(fileNamec);
		File new3 = new File(fileNamer);
		File new4 = new File(fileNamef);

		File dir = new File(folder);
		if (!dir.exists()) {

			dir.mkdir();
		}

		if (old1.exists()) {
			old1.renameTo(new1);
		}

		if (old2.exists()) {
			old2.renameTo(new2);
		}

		if (old3.exists()) {
			old3.renameTo(new3);
		}

		if (old4.exists()) {
			old4.renameTo(new4);
		}

	}

	private void printLog(ArrayList<CarData> od) {
		for (int i = 0; i < od.size(); i++) {
			Log.d(logTag,
					"FileRW SMS:" + od.get(i).distance + " BRAND:"
							+ od.get(i).literwon);
		}
	}

	public void writeFile_OilData(ArrayList<OilData> od) {
		Log.d(logTag, "FileRW writeFile");
		// printLog(od);
		File file = new File(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);

			out.writeObject(od);
			out.close();
			fos.close();

		} catch (IOException e) {
		}
	}

	public ArrayList<OilData> readFile_OilData() {
		Log.d(logTag, "FileRW readFile");
		ArrayList<OilData> od = new ArrayList<OilData>();
		File file = new File(fileName);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis);

			od = (ArrayList<OilData>) in.readObject();

			in.close();
			fis.close();
		} catch (Exception e) {
		}
		// printLog(od);
		return od;
	}

	public void writeFile_CarData(ArrayList<CarData> od) {
		Log.d(logTag, "FileRW writeFile_CarData");
		printLog(od);
		File file = new File(fileNamec);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);

			out.writeObject(od);
			out.close();
			fos.close();

		} catch (IOException e) {
			Util.print("writeFile_CarData IOException " + e.toString());
		}
	}

	public ArrayList<CarData> readFile_CarData() {
		Log.d(logTag, "FileRW readFile_CarData");
		ArrayList<CarData> od = new ArrayList<CarData>();
		File file = new File(fileNamec);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis);

			od = (ArrayList<CarData>) in.readObject();

			in.close();
			fis.close();
		} catch (Exception e) {
			Util.print("readFile_CarData IOException " + e.toString());

		}
		printLog(od);
		return od;
	}

	public void writeFile_RepairData(ArrayList<RepairData> od) {
		Log.d(logTag, "FileRW writeFile_RepairData");
		// printLog(od);
		File file = new File(fileNamer);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);

			out.writeObject(od);
			out.close();
			fos.close();

		} catch (IOException e) {
			Util.print("writeFile_RepairData IOException " + e.toString());
		}
	}

	public ArrayList<RepairData> readFile_RepairData() {
		Log.d(logTag, "FileRW readFile_RepairData");
		ArrayList<RepairData> od = new ArrayList<RepairData>();
		File file = new File(fileNamer);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis);

			od = (ArrayList<RepairData>) in.readObject();

			in.close();
			fis.close();
		} catch (Exception e) {
			Util.print("readFile_RepairData IOException " + e.toString());

		}
		// printLog(od);
		return od;
	}

	public void writeFile_FilterData(ArrayList<String> od) {
		Log.d(logTag, "FileRW writeFile_FilterData");
		// printLog(od);
		File file = new File(fileNamef);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);

			out.writeObject(od);
			out.close();
			fos.close();

		} catch (IOException e) {
			Util.print("writeFile_RepairData IOException " + e.toString());
		}
	}

	public ArrayList<String> readFile_FilterData() {
		Log.d(logTag, "FileRW readFile_FilterData");
		ArrayList<String> od = new ArrayList<String>();
		File file = new File(fileNamef);

		if (!file.exists()) {
			ArrayList<String> cc = new ArrayList<String>();
			cc.add("주유소");
			cc.add("주유");
			cc.add("오일뱅크");
			cc.add("석유");
			cc.add("정유");
			cc.add("오일");
			cc.add("충전소");
			writeFile_FilterData(cc);
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis);

			od = (ArrayList<String>) in.readObject();

			in.close();
			fis.close();
		} catch (Exception e) {
			Util.print("readFile_RepairData IOException " + e.toString());

		}
		// printLog(od);
		return od;
	}
}
