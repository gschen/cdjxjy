package com.github.justin.cdjxjy.sfexpress.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.github.justin.cdjxjy.sfexpress.constants.SystemConstants;

public class PropUtils {
	private static final Logger log = Logger.getLogger(PropUtils.class);

	public static Properties props;

	public final static void init() {
		props = new Properties();
		try {
			log.info("loading file: " + SystemConstants.PROP_FILE_PATH);
			props.load(Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(SystemConstants.PROP_FILE_PATH));

//			props.load(new FileInputStream(SystemConstants.PROP_FILE_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("open " + SystemConstants.PROP_FILE_PATH + " file success!");
	}

	public static int getValueInt(String key) {

		return Integer.parseInt(getValue(key).trim());

	}

	public static String[] getValueArray(String key) {

		String value = getValue(key);
		return value.split("[;]");
	}

	public static String getValue(String key) {

		String value = null;
		try {

			value = new String(props.getProperty(key).getBytes("ISO-8859-1"),
					"utf-8");
			// System.out.println(key + " = " + value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
