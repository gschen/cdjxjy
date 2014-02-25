package com.github.justin.cdjxjy.cdjxjy.utils;

import org.jsoup.nodes.Document;

public class PageParserUtils {

	public static String getViewState(Document doc) {
		return doc.select("input#__VIEWSTATE").first().attr("value");
	}

	public static String getEventValidation(Document doc) {
		return doc.select("input#__EVENTVALIDATION").first().attr("value");
	}
}
