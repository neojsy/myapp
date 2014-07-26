package com.neojsy.SimplePodcast;


public class ItemCast {
	String title;
	String subTitle;
	String desc;
	String author;
	String image;
	String view;
	String xmlUrl;
	
	final static int number = 7;
	
	
	ItemCast(){
		title = "no";
		subTitle = "no";
		desc = "no";
		author = "no";
		image = "no";
		view = " ";	
		xmlUrl = "no";
	}
	
	ItemCast(String a1,String a2,String a3,String a4,String a5,String a6,String a7){
		title = a1;
		subTitle = a2;
		desc = a3;
		author = a4;
		image = a5;
		view = a6;
		xmlUrl = a7;
	}
	
}
