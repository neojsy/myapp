package com.neojsy.SimplePodcast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;

public class XmlParser {
	static final String TAG = "SimplePodcast";
	
	ItemCast ic = new ItemCast();
	ArrayList<ItemEpisode> ie = new ArrayList<ItemEpisode>();
	
	private InputStream getInputStream(String para_url) throws Exception {

				Log.d(TAG, "getInputStream try");
				URL url = new URL(para_url);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(10000);
				con.setReadTimeout(10000);
				InputStream is = con.getInputStream();
				return is;

	}

	public ItemCast getCast(String parseURL) throws Exception{
		
		get(parseURL, true);
		return ic;
	}
	
	public ArrayList<ItemEpisode> getEpisode(String parseURL) throws Exception{
		ie.clear();
		temp.title = null;
		temp.summary = null;
		temp.mp3url = null;
		temp.pubDate = null;
		temp.mp3FullTime = null;
		
		get(parseURL, false);
		return ie;
	}
	
	boolean CastStart = false;
	
	private void get(String parseURL, boolean isCast) throws Exception {
		////Log.d(TAG, "parseURL : "+parseURL);
		

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();
			InputStream stream = getInputStream(parseURL);
			xpp.setInput(stream, null);
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {

				case XmlPullParser.START_TAG:
					////Log.d(TAG, "START_TAG");
					String name = xpp.getName();
					////Log.d(TAG, "name " + name);
					
					if(isCast && name.equals("item")){
						return;
					}

					if(!isCast && name.equals("item")){

						CastStart = true;
					}
						
					
					String text = " ";
					
					if (name.equals("itunes:image")) {
						//xpp.next();
						text = xpp.getAttributeValue(null, "href");
					}
					else if (name.equals("enclosure")) {
						//xpp.next();
						text = xpp.getAttributeValue(null, "url");
					}
					else{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT){
							text = xpp.getText();
						}	
					}
					
					if(!name.equals("null")){
						if(isCast)
							tagFilterCast(name, text);
						else
							tagFilterEpisode(name, text);
					}
					
					break;
				case XmlPullParser.START_DOCUMENT:
//					////Log.d(TAG, "START_DOCUMENT");
					break;					
				case XmlPullParser.END_TAG:
					//Log.d(TAG, "END_TAG");
					tagFilterEpisode("END", "END");
					break;

				}
				eventType = xpp.next();
			}

	}
	
	private void tagFilterCast(String name, String text){
		//Log.d(TAG, "tagFilterCast name : "+name+" text : "+text);
		
		text = changeColon(text);
		
		
		if(name.equals("title") && ic.title.equals("no")){
			ic.title = text;
		}
		if(name.equals("itunes:subtitle")){
			ic.subTitle = text;
		}
		if(name.equals("itunes:summary") || name.equals("description")){
			ic.desc = text;
		}
		if(name.equals("itunes:author")){
			ic.author = text;
		}
		if(name.equals("itunes:image")){
			ic.image = text;
		}
	}
	
	ItemEpisode temp = new ItemEpisode();
	
	private void tagFilterEpisode(String name, String text){
		
		if(!CastStart)
			return;
		
		if(!name.equals("END")){
		//Log.d(TAG, "tagFilterEpisode | name : "+name+" | text : "+text);
		}
		
		String convtext = changeColon(text);
		
		if(name.equals("title")){
			temp.title = convtext;
		}
		else if(name.equals("itunes:subtitle")){
			if(temp.title == null){
				temp.title = convtext;
			}
		}
		else if(name.equals("itunes:summary") || name.equals("description")){
			//Log.d(TAG, "original "+convtext);
			String con2 = convtext.replaceAll("\n", " ");
			String con3 = con2.replaceAll("<[^>]+>", "");
			String con4 = con3.replaceAll("</[^>]+>", " ");
			String con5 = con4.replaceAll("&nbsp;", " ");
			String con6 = changeColon(con5);
			
			//Log.d(TAG, "convert "+con4);
			temp.summary = con6;
		}
		else if(name.equals("enclosure")){
			Log.d(TAG, "mp3url "+convtext);
			temp.mp3url = convtext;
		}
		else if(name.equals("pubDate")){
			temp.pubDate = convtext;
		}
		else if(name.equals("itunes:duration")){
			temp.mp3FullTime = convtext;
		}
		else if(name.equals("END")){
			//Log.d(TAG, "One cycle end");
			
			if(temp.title != null &&
					temp.mp3url != null &&
					temp.pubDate != null){
				//Log.d(TAG, "One cycle end");
				ie.add(new ItemEpisode(
						temp.title,
						temp.summary,
						"no",
						temp.mp3url,
						temp.pubDate,
						"no",
						"0",
						temp.mp3FullTime,
						"s1",
						" "
						));
				temp.title = null;
				temp.summary = null;
				temp.mp3url = null;
				temp.pubDate = null;
				temp.mp3FullTime = null;
			}
			
		}
		
		

		/*
		String title;
		String summary;
		String image;
		String mp3url;
		String pubDate;
		String read;
		String mp3PlayedTime;
		String mp3FullTime;
		String s1;
		String s2;
		*/
	}
	
	public String changeColon(String args) {

		if(args == null)
			return " ";
		else
			return args.replaceAll(", ", ". ");

	}

}
