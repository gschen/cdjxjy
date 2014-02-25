package com.github.justin.cdjxjy.tbviewcounter.thread;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.github.justin.cdjxjy.tbviewcounter.DateTimeUtils;

public class MultiGetThread {

	public final static void execute() {

		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));

		ClientConnectionManager cm = new PoolingClientConnectionManager(sr);
		HttpClient httpClient = new DefaultHttpClient(cm);
		int threadLength = 10;
		String url = "http://count.tbcdn.cn/counter3?inc=ICVT_7_13756426798&sign=9297648529dbe0497db33d23a08047925f04a&keys=DFX_200_1_13756426798,ICVT_7_13756426798,ICCP_1_13756426798,SCCP_2_65104567&callback=DT.mods.SKU.CountCenter.saveCounts";

		GetThread[] threads = new GetThread[threadLength];

		for (int i = 0; i < threads.length; ++i) {
			System.out.println("Starting thread - " + i);
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(
					"referer",
					"http://s.taobao.com/search?q=%B9%AB%CE%F1%D4%B1%BF%BC%CA%D4&searcy_type=item&s_from=newHeader&source=&ssid=s5-e&search=y&initiative_id=shopz_"
							+ DateTimeUtils.getCurrentDateTimeStr());
			threads[i] = new GetThread(httpClient, httpGet);
		}

		for (int i = 0; i < threads.length; ++i)
			threads[i].start();

		for (int i = 0; i < threads.length; ++i)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
