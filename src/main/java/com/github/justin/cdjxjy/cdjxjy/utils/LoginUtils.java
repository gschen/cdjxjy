package com.github.justin.cdjxjy.cdjxjy.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;

public class LoginUtils {
	private static final Logger log = Logger.getLogger(LoginUtils.class);

	public static String getHideTxt(Document doc) {
		return doc.select("input#hidetxt").first().attr("value");
	}

	public static void login() {
		String pageUrl = "http://www.cdjxjy.com/Portal/index.aspx";
		HttpClientUtils.doGet(pageUrl);
		Document doc = HttpClientUtils.getResponseAsDocument();

		String viewState = PageParserUtils.getViewState(doc);
		String eventValidation = PageParserUtils.getEventValidation(doc);
		String hideTxt = getHideTxt(doc);
		// log.info(viewState);
		// log.info(eventValidation);
		// log.info(hideTxt);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		params.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation));
		params.add(new BasicNameValuePair("TxtUsr", "******"));
		params.add(new BasicNameValuePair("TxtPwd", "******"));
		params.add(new BasicNameValuePair("btnlog.x", "32"));
		params.add(new BasicNameValuePair("btnlog.y", "5"));
		params.add(new BasicNameValuePair("hidetxt", hideTxt));

		HttpClientUtils.doPost(pageUrl, params);

		// HttpClientUtils.printCookies();
		log.info("Login success!");

	}
}
