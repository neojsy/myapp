package com.neojsy.digitalframe;

import java.io.*;
import java.util.*;

public class SelectRandomFile {
	static String selectedfile = "NO";
	
	SelectRandomFile(){	
	}
	
	public String getFileName(ArrayList<String> mFileList, ArrayList<String> mFolderList){
		selectedfile = "NO";
		
		if (mFileList.size() == 0)
			return selectedfile;
		else {
			Random oRandom = new Random();
			int selectedFileIndex;
			boolean searchResult;

			do {
				selectedFileIndex = oRandom.nextInt(mFileList.size());
				searchResult = selectRandomImageFile(selectedFileIndex, mFileList, mFolderList);
			} while (!searchResult);

			return selectedfile;
		}
	}
	
	private static boolean selectRandomImageFile(int fileIndex, ArrayList<String> mFileList, ArrayList<String> mFolderList) {

		String fileString = mFileList.get(fileIndex);
		String[] temps = fileString.split(":::");

		int folderIndex = Integer.parseInt(temps[0]);

		String seletedFileName = temps[1];
		String seletedFilePath = mFolderList.get(folderIndex);
		selectedfile = seletedFilePath + "/" + seletedFileName;

		File file = new File(selectedfile);

		if (file.length() == 0) {
			return false;
		} else {
			return true;
		}

	}
}
