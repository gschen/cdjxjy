package com.github.justin.cdjxjy.tbviewcounter.thread;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class GetThread extends Thread {

	@Override
	public void run() {
		try {
			HttpResponse response = httpClient.execute(httpGet);
			EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			this.httpGet.abort();
		} catch (IOException e) {
			e.printStackTrace();
			this.httpGet.abort();
		}
	}

	private HttpClient httpClient;
	private HttpContext httpContext;
	private HttpGet httpGet;

	public GetThread(HttpClient httpClient, HttpGet httpGet) {
		this.httpClient = httpClient;
		this.httpGet = httpGet;
		this.httpContext = new BasicHttpContext();

	}
}
