package com.neojsy.SimplePodcast;

public class ItemPopup {
	int imgID;
	String text;
	ItemPopup(int id, String t){
		imgID = id;
		text = t;
	}
	public int getImgID(){
		return imgID;
	}
	public String getText(){
		return text;
	}
}
