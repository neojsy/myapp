package com.tistory.neojsy;

import java.io.*;
import java.util.*;

import android.content.*;

public class LoadFileToList {
	Context context;

	LoadFileToList(Context con) {
		context = con;
	}

	public ArrayList<String> getList(String fileName) throws IOException {
		ArrayList<String> mList = new ArrayList<String>();

		FileInputStream fis = context.openFileInput(fileName);
		byte[] data = new byte[fis.available()];
		while (fis.read(data) != -1) {
			;
		}
		fis.close();

		mList.clear();
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
			mList.add(temps[i]);
		}

		return mList;
	}

}
