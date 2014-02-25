package com.github.justin.cdjxjy.cdjxjy;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;

public abstract class AbstractClient {
	private static final Logger log = Logger.getLogger(AbstractClient.class);

	public void init() {
		DOMConfigurator.configure(this.getClass().getClassLoader()
				.getResource("log4j.xml"));
		PropUtils.init();
		HttpClientUtils.init();
	}

	public void execute() {
		init();
		task();
		shutdown();
	}

	public abstract void task();

	public void shutdown() {
		log.info("complete all tasks!");
	}

}
