package com.github.justin.cdjxjy.sfexpress.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpClientUtils {
	private static final Logger log = Logger.getLogger(HttpClientUtils.class);

	public static DefaultHttpClient httpClient;

	public static HttpPost httpPost;

	public static HttpGet httpGet;

	public static HttpResponse httpResponse;
	public static int failedPostCnt = 0;
	public static int failedGetCnt = 0;

	public static int totalPostCnt = 0;
	public static int totalGetCnt = 0;

	public final static void init() {
		if (null == httpClient) {
			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					PropUtils.getValueInt("connectionTimeout"));
			HttpConnectionParams.setSoTimeout(httpParams,
					PropUtils.getValueInt("socketTimeout"));
			HttpConnectionParams.setTcpNoDelay(httpParams, true);
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
			// httpParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS,
			// false); //禁止自动重定向

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
					.getSocketFactory()));
			schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
					.getSocketFactory()));
			PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
					schemeRegistry);
			cm.setMaxTotal(2000);
			cm.setDefaultMaxPerRoute(200);
			httpClient = new DefaultHttpClient(cm, httpParams);
		}
	}

	public final static void shutdown() {

		httpClient.getConnectionManager().shutdown();
		httpClient = null;
	}

	public static void doPost(String url, List<NameValuePair> params) {
		doPost(url, params, null);
	}

	public static void doPost(String url, List<NameValuePair> params,
			List<NameValuePair> headers) {

		httpPost = new HttpPost(url);

		if (headers != null) {
			for (NameValuePair nvp : headers) {
				httpPost.setHeader(nvp.getName(), nvp.getValue());
			}
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e);
			log.error(e.getStackTrace());
		}

		int cnt = 0;
		boolean result = false;
		do {
			if (cnt > 0) {
				failedPostCnt++;
				try {
					log.info("httppost request no: " + cnt + 1);
					int time = PropUtils.getValueInt("sleepInterval") * cnt;
					Thread.sleep(time);
					log.info("Sleep " + time + " ms because of blocking!");
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			}
			result = tryToDoPost();
			totalPostCnt++;
		} while (!result && cnt++ < PropUtils.getValueInt("requestRetryCount"));
	}

	// httppost执行成功时返回true，捕获到异常时返回false
	public static boolean tryToDoPost() {
		try {
			httpResponse = httpClient.execute(httpPost);
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
		return false;

	}

	public static void reset() {
		// shutdown();
		// init();
	}

	public static boolean tryToDoGet() {
		try {

			httpResponse = httpClient.execute(httpGet);
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
			reset();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
			reset();
		}
		return false;

	}

	public static void doGet(String url) {

		doGet(url, null);
	}

	public static void doGet(String url, List<NameValuePair> headers) {

		httpGet = new HttpGet(url);

		if (headers != null) {
			for (NameValuePair nvp : headers) {
				httpGet.setHeader(nvp.getName(), nvp.getValue());
			}
		}
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// httpGet.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		// httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		// httpGet.setHeader("Accept-Language",
		// "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		// httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		// httpGet.setHeader(
		// "Cookie",
		// "cna=fljZCWoFehoCAXBBfMt5hhR0; miid=1668940266467109747; tg=0; _cc_=U%2BGCWk%2F7og%3D%3D; tracknick=gschen163; l=gschen163::1368359654957::11; mt=ci=0_0; v=0; cookie2=df0ac536dba941926faf2f79cd9084bd; t=7ef564d0b245be34a9123b1100a73a49; swfstore=105773; _tb_token_=Zt9Mjq1j9a6j; uc1=cookie14=UoLa9Hhj6fZmSw%3D%3D; sec=Detail|101.245.136.57|1368957454|7896232fca0b52ab0c38f7179f0216b5; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0");
		// httpGet.setHeader("Host", "");
		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 ");
		// log.info("do get request： " + url);
		int cnt = 0;
		boolean result = false;
		do {
			if (cnt > 0) {
				failedGetCnt++;
				try {
					log.info("httpget request no: " + cnt + 1);
					int time = PropUtils.getValueInt("sleepInterval") * cnt;
					Thread.sleep(time);
					log.info("Sleep " + time + " ms because of blocking!");
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			}
			result = tryToDoGet();
			totalGetCnt++;
		} while (!result && cnt++ < PropUtils.getValueInt("requestRetryCount"));

	}

	public static String getResponseAsString() {

		String str = null;
		try {
			str = EntityUtils.toString(httpResponse.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
		}

		return str;
	}

	public static Document getResponseAsDocument() {

		Document doc = null;

		doc = Jsoup.parse(getResponseAsString());

		return doc;
	}

	public static void printMetrics() {
		// log.info("---------------------------------------------------------");
		log.info("All of httpPOST request num is: " + totalPostCnt);
		log.info("All of httpGET request num is: " + totalGetCnt);
		log.info("All of failed httpPOST request num is: " + failedPostCnt);
		log.info("All of failed httpGET request num is: " + failedGetCnt);
	}

	public static void printCookies() {
		List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		log.info("Cookies is as following: ");
		for (Cookie c : cookies) {
			log.info(c.getName() + " - " + c.getValue());
		}
	}
}
