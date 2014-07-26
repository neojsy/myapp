package com.neojsy.SimplePodcast;

public class ItemEpisode {
	String title;
	String summary;
	String image;
	String mp3url;
	String pubDate;
	String read;
	String mp3PlayedTime;
	String mp3FullTime;
	String date;
	String newepi;
	
	
	final static int number = 10;
	
	ItemEpisode(String a1,String a2,String a3,String a4,String a5,String a6,String a7,String a8,String a9,String a10){
		title = a1;
		summary = a2;
		image = a3;
		mp3url = a4;
		pubDate = a5;
		read = a6;
		mp3PlayedTime = a7;
		mp3FullTime = a8;
		date = a9;
		newepi= a10;
	}
	
	ItemEpisode(){
		title = "no";
		summary = "no";
		image = "no";
		mp3url = "no";
		pubDate = "no";
		read = "no";
		mp3PlayedTime = "0";
		mp3FullTime = "0";
		date = "no";
		newepi = "no";
	}
	
	public int getDate(){
		return Util.Str2Int(date);
	}
}
