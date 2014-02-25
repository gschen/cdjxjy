package com.github.justin.cdjxjy.tbviewcounter;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;
import com.github.justin.cdjxjy.tbviewcounter.service.ShopItemListService;
import com.github.justin.cdjxjy.tbviewcounter.service.ShopItemService;

public class ViewCounterClient {
	private static final Logger log = Logger.getLogger(ViewCounterClient.class);

	public void init() {
		DOMConfigurator.configure(this.getClass().getClassLoader()
				.getResource("log4j.xml"));
		PropUtils.init();
		HttpClientUtils.init();
	}

	public void shutdown() {

		log.info("complete all tasks!");
	}

	public void single(String url) {

		ShopItemService shopItemService = new ShopItemService(url);
		shopItemService.execute();
	}

	public void single() {

		single(PropUtils.getValue("itemUrl"));
	}

	public void full() {

		ShopItemListService shopItemListService = new ShopItemListService(
				PropUtils.getValue("shopUrl"));
		shopItemListService.execute();
		List<String> urls = shopItemListService.getItemUrlList();

		for (String url : urls) {
			log.info("url: " + url);
			single(url);
		}
	}

	public void execute() {

		init();
		// full();
		single();
		// while (true)
		// MultiGetThread.execute();
		shutdown();
	}

	public static void main(String[] args) {
		new ViewCounterClient().execute();

	}

}
