package com.github.justin.cdjxjy.tbviewcounter.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;

public class ShopItemListService {

	private String pageUrl;

	private List<String> itemUrlList = new ArrayList<String>();

	public List<String> getItemUrlList() {
		return itemUrlList;
	}

	public ShopItemListService(String url) {
		this.pageUrl = url.trim() + "/category.htm?search=y";
	}

	public void execute() {

		HttpClientUtils.doGet(pageUrl);
		Document doc = HttpClientUtils.getResponseAsDocument();

		// model 1
		Elements eles = doc.select("a.item-name");
		for (Element e : eles) {
			System.out.println(e.attr("href"));

			itemUrlList.add(e.attr("href"));
		}

		// model 2
		Elements urls = doc.select("div.pic > a");
		for (Element e : urls) {
			System.out.println(e.attr("href"));

			itemUrlList.add(e.attr("href"));
		}

	}
}
