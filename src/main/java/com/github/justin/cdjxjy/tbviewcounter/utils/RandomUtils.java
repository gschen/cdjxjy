package com.github.justin.cdjxjy.tbviewcounter.utils;

public class RandomUtils {

	public static int getRandomNumLong(int start, int end) {
		Long num;

		num = Math.round(Math.random() * (end - start) + start);
		return num.intValue();

	}
}
