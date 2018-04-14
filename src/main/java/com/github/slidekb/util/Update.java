package com.github.slidekb.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Update {

	public String isAndGetUpdate(String currentVersion){
		try {
			Document doc = Jsoup.connect("https://slidekb.com/pages/software").get();
			String text = doc.body().text();
			int beginning = text.indexOf(" (Version ");
			text = text.substring(beginning + 10, beginning + 15);
			if (!currentVersion.equals(text)) {
				return text;
			} else {
				return "";
			}
		} catch (IOException e) {
			System.out.println("Update->isAndGetUpdate()-> Could not reach update site");
		}
		
		return "";
	}
}
