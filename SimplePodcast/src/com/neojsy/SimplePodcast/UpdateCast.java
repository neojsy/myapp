package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.util.Log;

public class UpdateCast {
	static final String TAG = "SimplePodcast";
	
	String XML_URL;
	String fileName;
	Context context;
	int startIndexOfYear;

	UpdateCast(Context con) {
		context = con;
	}
	
	ArrayList<ItemEpisode> ie = new ArrayList<ItemEpisode>();
	ArrayList<ItemEpisode> neo = new ArrayList<ItemEpisode>();
	
	public String Update(String url, String file) throws Exception{
		int readedNum = 0;
		int newEpiNum = 0;
		
		XML_URL = url;
		fileName = file;
		
		neo.clear();
		XmlParser xp = new XmlParser();
		neo = xp.getEpisode(XML_URL);
		//Log.d(TAG, "Cast updated : "+XML_URL+" size : "+ie.size());
		
    	/*
		for(int k=0;k<neo.size();k++){
			Log.d(TAG, "------------Parce result---- array : "+k);
			Log.d(TAG, "title : "+neo.get(k).title);
			Log.d(TAG, "summary : "+neo.get(k).summary);
			Log.d(TAG, "image : "+neo.get(k).image);
			Log.d(TAG, "mp3url : "+neo.get(k).mp3url);
			Log.d(TAG, "pubDate : "+neo.get(k).pubDate);
			Log.d(TAG, "read : "+neo.get(k).read);
			Log.d(TAG, "mp3PlayedTime : "+neo.get(k).mp3PlayedTime);
			Log.d(TAG, "mp3FullTime : "+neo.get(k).mp3FullTime);
			Log.d(TAG, "s1 : "+neo.get(k).date);
			Log.d(TAG, "s2 : "+neo.get(k).newepi);
		}
    	*/
		
		//thu. 06 may 2012 10:00:00 +0900
		for(int g=0;g<neo.size();g++){
			Log.d(TAG, "pubDate : "+neo.get(g).pubDate);
			
			String pubString = neo.get(g).pubDate.replace(" ", "");
			int middle = findMonth(pubString);
			String pre = pubString.substring(0, middle);
			String after = pubString.substring(middle, pubString.length());
			//Log.d(TAG, " pre : "+pre+"| after : "+after+"| pubString : "+pubString);
			int startOfSemicolon = after.indexOf(":")-2;
			int dday = findDay(pre);
			int dyear = findYear(after);
			String sMonth = after.substring(0, startIndexOfYear);
			String sTime = after.substring(startOfSemicolon, startOfSemicolon+8); 
			
			Log.d(TAG, " dyear : "+dyear+"| sMonth : "+sMonth+"| dday : "+dday+"| sTime : "+sTime);
			
			String year = Util.Int2Str(dyear);
			int mon = Util.Str2Int(Util.Month2Num(sMonth));
			String smon = String.format( "%02d", mon );
			String sday = String.format( "%02d", dday );
			
			String[] time = sTime.split(":");
			
			String hour = "0";
			String min = "0"; 
			if(time[0] != null && time[1] != null){
				hour = time[0];
				min = time[1];
			}
			
			year = year.substring(2);
			
			
			//pubdate > date (yymmdd)
			String src = year + smon + sday + hour + min;
			String con2 = src.replaceAll(" ", "");
			String con3 = con2.replaceAll("[+]", "");
			if(con3.length() > 10){
				neo.get(g).date = con3.substring(0, 10);
			}
			else{
				neo.get(g).date = con3;
			}
			//pubdate conv yyyy.mm.dd 00:00
			neo.get(g).pubDate = year+"."+ smon +"."+ sday + " " + hour + ":" + min;
			
			Log.d(TAG, "neo.get(g).date : "+neo.get(g).date);
			Log.d(TAG, "neo.get(g).pubDate : "+neo.get(g).pubDate);
		}
		
		
		ie.clear();
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
    	LoadFileToList lf = new LoadFileToList(context);
    	File originfile = new File("/data/data/com.neojsy.SimplePodcast/files/"+fileName);
		if (originfile.exists()){
			//Log.d(TAG, "Exist Cast. Update start!");
			try {
				mList = lf.getList(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ie = Str2Epi(mList);
			
			//Log.d(TAG, "ie.size() : "+ie.size());
			//Log.d(TAG, "neo.size() : "+neo.size());
			
			//new string remove
			for(int y=0;y<ie.size();y++){
				ie.get(y).newepi = " ";
			}
			
			if(ie.size() != neo.size()){

				
				int neoDate;
				int ieDate = Util.Str2Int(ie.get(0).date);
				for(int x=0;x<neo.size();x++){
					neoDate = Util.Str2Int(neo.get(x).date);
					//Log.d(TAG, "neoDate : "+neoDate);
					//Log.d(TAG, "ieDate : "+ieDate);
					if(neoDate > ieDate){
						//Log.d(TAG, "New episode finded. will add! | new : "+neo.get(x).date);
						neo.get(x).newepi = "new";
						ie.add(neo.get(x));
						newEpiNum++;	
					}
				}
				
				/*
				boolean Notfind = true;

				for(int x=0;x<neo.size();x++){
					Notfind = true;
					
					for(int y=0;y<ie.size();y++){
						if(neo.get(x).date.equals(ie.get(y).date)){
							Notfind = false;
						}
						
						if(Notfind){
							Log.d(TAG, "New episode finded. will add! | new : "+neo.get(x).date);
							neo.get(x).newepi = "new";
							ie.add(neo.get(x));
							newEpiNum++;	
						}
					}
				}
				*/
			}
		}
		else{
			//Log.d(TAG, "New Cast. Create start!");
			ie = neo;
		}

		//cast sort using date
		//Log.d(TAG, "sorting start!");
		Comparator<ItemEpisode> myComparator = new Comparator<ItemEpisode>() {
			public int compare(ItemEpisode obj1, ItemEpisode obj2) {
			return obj1.getDate() < obj2.getDate() ? 1:(obj1.getDate()==obj2.getDate()?0:-1);
			}
		};
		
		Collections.sort(ie , myComparator);
		
		/*
		for(int k=0;k<ie.size();k++){
			Log.d(TAG, "----------------------------- array : "+k);
			Log.d(TAG, "ie.get(k).title : "+ie.get(k).title);
			Log.d(TAG, "ie.get(k).summary : "+ie.get(k).summary);
			Log.d(TAG, "ie.get(k).image : "+ie.get(k).image);
			Log.d(TAG, "ie.get(k).mp3url : "+ie.get(k).mp3url);
			Log.d(TAG, "ie.get(k).pubDate : "+ie.get(k).pubDate);
			Log.d(TAG, "ie.get(k).read : "+ie.get(k).read);
			Log.d(TAG, "ie.get(k).mp3PlayedTime : "+ie.get(k).mp3PlayedTime);
			Log.d(TAG, "ie.get(k).mp3FullTime : "+ie.get(k).mp3FullTime);
			//Log.d(TAG, "ie.get(k).s1 : "+ie.get(k).s1);
			//Log.d(TAG, "ie.get(k).s2 : "+ie.get(k).s2);
		}
		*/
		Log.d(TAG, "updating write to file!");
		try {
			writeToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int z=0;z<ie.size();z++){
			if(ie.get(z).read.equals("no")){
				readedNum++;
			}
		}
		
//		return Integer.toString(readedNum)+"/"+ie.size();
		
		String result = " ";
		if(newEpiNum > 0){
			result = "new "+ newEpiNum;
		}
		return result;
	}
	
    public void writeToFile() throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
    	LoadFileToList lf = new LoadFileToList(context);
    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/"+fileName);
		if (file.exists()){
//			mList = lf.getList(fileName);
		}    	
		
		mList = Epi2Str(ie);
    	lf.saveFileListToFile(fileName, mList);
    }
    
    public ArrayList<ItemEpisode> Str2Epi(ArrayList<String> src){
    	
    	ArrayList<ItemEpisode> rs = new ArrayList<ItemEpisode>();
    	ItemEpisode temp = new ItemEpisode();
    	int num = temp.number;
    	int epinum = src.size()/num;
    	//Log.d(TAG, "Episode item : "+num+" number :" + epinum);
    	for(int k=0;k<epinum;k++){
    		rs.add(new ItemEpisode(
    				src.get(k*num + 0),
    				src.get(k*num + 1),
    				src.get(k*num + 2),
    				src.get(k*num + 3),
    				src.get(k*num + 4),
    				src.get(k*num + 5),
    				src.get(k*num + 6),
    				src.get(k*num + 7),
    				src.get(k*num + 8),
    				src.get(k*num + 9)
					));
    	}
    	/*
		for(int k=0;k<rs.size();k++){
			Log.d(TAG, "------------String to Episode---- array : "+k);
			Log.d(TAG, "ie.get(k).title : "+rs.get(k).title);
			Log.d(TAG, "ie.get(k).summary : "+rs.get(k).summary);
			Log.d(TAG, "ie.get(k).image : "+rs.get(k).image);
			Log.d(TAG, "ie.get(k).mp3url : "+rs.get(k).mp3url);
			Log.d(TAG, "ie.get(k).pubDate : "+rs.get(k).pubDate);
			Log.d(TAG, "ie.get(k).read : "+rs.get(k).read);
			Log.d(TAG, "ie.get(k).mp3PlayedTime : "+rs.get(k).mp3PlayedTime);
			Log.d(TAG, "ie.get(k).mp3FullTime : "+rs.get(k).mp3FullTime);
			Log.d(TAG, "ie.get(k).s1 : "+rs.get(k).s1);
			Log.d(TAG, "ie.get(k).s2 : "+rs.get(k).s2);
		}
    	*/
    	return rs;
    }
    
    public ArrayList<String> Epi2Str(ArrayList<ItemEpisode> src){
    	//Log.d(TAG, "Epi2Str");
    	
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
		for(int k=0;k<src.size();k++){
			mList.add(src.get(k).title);
			mList.add(src.get(k).summary);
			mList.add(src.get(k).image);
			mList.add(src.get(k).mp3url);
			mList.add(src.get(k).pubDate);
			mList.add(src.get(k).read);
			mList.add(src.get(k).mp3PlayedTime);
			mList.add(src.get(k).mp3FullTime);
			mList.add(src.get(k).date);
			mList.add(src.get(k).newepi);
		}
	
		//Log.d(TAG, "Epi2Str "+mList.size());
		/*
		for(int k=0;k<mList.size();k++){
			Log.d(TAG, "------------Episode to String----------- array : "+k);
			Log.d(TAG, "mList.get(k).title : "+mList.get(k));

		}
		*/
		return mList;
    }
	
	public int findYear(String w){
		startIndexOfYear = -1;
		
		startIndexOfYear = w.indexOf("2024");
		if(startIndexOfYear != -1){ return 2024;}
		startIndexOfYear = w.indexOf("2023");
		if(startIndexOfYear != -1){ return 2023;}
		startIndexOfYear = w.indexOf("2022");
		if(startIndexOfYear != -1){ return 2022;}
		startIndexOfYear = w.indexOf("2021");
		if(startIndexOfYear != -1){ return 2021;}
		startIndexOfYear = w.indexOf("2020");
		if(startIndexOfYear != -1){ return 2020;}
		startIndexOfYear = w.indexOf("2019");
		if(startIndexOfYear != -1){ return 2019;}
		startIndexOfYear = w.indexOf("2018");
		if(startIndexOfYear != -1){ return 2018;}
		startIndexOfYear = w.indexOf("2017");
		if(startIndexOfYear != -1){ return 2017;}
		startIndexOfYear = w.indexOf("2016");
		if(startIndexOfYear != -1){ return 2016;}
		startIndexOfYear = w.indexOf("2015");
		if(startIndexOfYear != -1){ return 2015;}
		startIndexOfYear = w.indexOf("2014");
		if(startIndexOfYear != -1){ return 2014;}
		startIndexOfYear = w.indexOf("2013");
		if(startIndexOfYear != -1){ return 2013;}
		startIndexOfYear = w.indexOf("2012");
		if(startIndexOfYear != -1){ return 2012;}
		startIndexOfYear = w.indexOf("2011");
		if(startIndexOfYear != -1){ return 2011;}
		startIndexOfYear = w.indexOf("2010");
		if(startIndexOfYear != -1){ return 2010;}
		startIndexOfYear = w.indexOf("2009");
		if(startIndexOfYear != -1){ return 2009;}
		startIndexOfYear = w.indexOf("2008");
		if(startIndexOfYear != -1){ return 2008;}
		startIndexOfYear = w.indexOf("2007");
		if(startIndexOfYear != -1){ return 2007;}
		startIndexOfYear = w.indexOf("2006");
		if(startIndexOfYear != -1){ return 2006;}
		startIndexOfYear = w.indexOf("2005");
		if(startIndexOfYear != -1){ return 2005;}
		startIndexOfYear = w.indexOf("2004");
		if(startIndexOfYear != -1){ return 2004;}
		startIndexOfYear = w.indexOf("2003");
		if(startIndexOfYear != -1){ return 2003;}
		startIndexOfYear = w.indexOf("2002");
		if(startIndexOfYear != -1){ return 2002;}
		startIndexOfYear = w.indexOf("2001");
		if(startIndexOfYear != -1){ return 2001;}
		
		return 2000;
	}

	public int findDay(String w){
		int res = -1;
		
		res = w.indexOf("31");
		if(res != -1){ return 31;}res = w.indexOf("30");
		if(res != -1){ return 30;}res = w.indexOf("29");
		if(res != -1){ return 29;}res = w.indexOf("28");
		if(res != -1){ return 28;}res = w.indexOf("27");
		if(res != -1){ return 27;}res = w.indexOf("26");
		if(res != -1){ return 26;}res = w.indexOf("25");
		if(res != -1){ return 25;}res = w.indexOf("24");
		if(res != -1){ return 24;}res = w.indexOf("23");
		if(res != -1){ return 23;}res = w.indexOf("22");
		if(res != -1){ return 22;}res = w.indexOf("21");
		if(res != -1){ return 21;}res = w.indexOf("20");
		if(res != -1){ return 20;}res = w.indexOf("19");
		if(res != -1){ return 19;}res = w.indexOf("18");
		if(res != -1){ return 18;}res = w.indexOf("17");
		if(res != -1){ return 17;}res = w.indexOf("16");
		if(res != -1){ return 16;}res = w.indexOf("15");
		if(res != -1){ return 15;}res = w.indexOf("14");
		if(res != -1){ return 14;}res = w.indexOf("13");
		if(res != -1){ return 13;}res = w.indexOf("12");
		if(res != -1){ return 12;}res = w.indexOf("11");
		if(res != -1){ return 11;}res = w.indexOf("10");
		if(res != -1){ return 10;}res = w.indexOf("9");
		if(res != -1){ return 9;}res = w.indexOf("8");
		if(res != -1){ return 8;}res = w.indexOf("7");
		if(res != -1){ return 7;}res = w.indexOf("6");
		if(res != -1){ return 6;}res = w.indexOf("5");
		if(res != -1){ return 5;}res = w.indexOf("4");
		if(res != -1){ return 4;}res = w.indexOf("3");
		if(res != -1){ return 3;}res = w.indexOf("2");
		if(res != -1){ return 2;}res = w.indexOf("1");
		if(res != -1){ return 1;}
		
		return 1;
	}
    
	public int findMonth(String word){
		int res = -1;
		
		String w = word.toLowerCase();
		
		res = w.indexOf("jan");
		if(res == -1)res = w.indexOf("january");
		if(res == -1)res = w.indexOf("feb");
		if(res == -1)res = w.indexOf("febuary");
		if(res == -1)res = w.indexOf("mar");
		if(res == -1)res = w.indexOf("march");
		if(res == -1)res = w.indexOf("apr");
		if(res == -1)res = w.indexOf("april");
		if(res == -1)res = w.indexOf("may");
		if(res == -1)res = w.indexOf("jun");
		if(res == -1)res = w.indexOf("june");
		if(res == -1)res = w.indexOf("jul");
		if(res == -1)res = w.indexOf("july");
		if(res == -1)res = w.indexOf("aug");
		if(res == -1)res = w.indexOf("august");
		if(res == -1)res = w.indexOf("sep");
		if(res == -1)res = w.indexOf("september");
		if(res == -1)res = w.indexOf("oct");
		if(res == -1)res = w.indexOf("october");
		if(res == -1)res = w.indexOf("nov");
		if(res == -1)res = w.indexOf("november");
		if(res == -1)res = w.indexOf("dec");
		if(res == -1)res = w.indexOf("december");

		return res;
	}
}
