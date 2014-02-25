package com.github.justin.cdjxjy.tbviewcounter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.github.justin.cdjxjy.sfexpress.bean.HotClickParams;
import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;
import com.github.justin.cdjxjy.sfexpress.utils.StringParserUtils;
import com.github.justin.cdjxjy.tbviewcounter.DateTimeUtils;
import com.github.justin.cdjxjy.tbviewcounter.utils.RandomUtils;

public class ShopItemService {
	private static final Logger log = Logger.getLogger(ShopItemService.class);

	private String pageUrl;

	public ShopItemService(String url) {
		this.pageUrl = url;
	}

	public void refeshViewCount(String url) {
		int i = 0;
		List<NameValuePair> referers = new ArrayList<NameValuePair>();
		referers.add(new BasicNameValuePair("Referer",
				"http://s.taobao.com/search?initiative_id=staobaoz_"
						+ DateTimeUtils.getRandomDateTimeStr() + "&jc=1&q="
						+ PropUtils.getValue("keyword")));
		String[] referArray = PropUtils.getValueArray("referer");
		for (String refer : referArray)
			referers.add(new BasicNameValuePair("Referer", refer));
		while (i < PropUtils.getValueInt("totalExecNum")) {
			log.info("seq no: " + i++);
			// log.info(target.toString());
			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(referers.get(RandomUtils.getRandomNumLong(0,
					referers.size() - 1)));
			log.info("referer is: " + headers.get(0).getValue());
			HttpClientUtils.doGet(url, headers);
		}
	}

	// counterApi:"http://count.tbcdn.cn/counter3?inc=ICVT_7_20505695336&sign=58784dc312f312a6bd612cfe3b3d95d592fa9&keys=DFX_200_1_20505695336,ICVT_7_20505695336,ICCP_1_20505695336,SCCP_2_103225282"
	// target.append(StringParserUtils.parseString(pageStr,
	// "(?:counterApi:\")(.*?)(?:\";"));
	public static String getViewCountUrl(String pageStr) {
		StringBuffer target = new StringBuffer();
		String append = "&callback=DT.mods.SKU.CountCenter.saveCounts";
		target.append(StringParserUtils.parseString(pageStr,
				"(?:counterApi:\")(.*?)(?:\")").get(0));
		target.append(append);
		return target.toString();
	}

	// http://hotclick.app.linezing.com/hotclick.gif?shopid=65104567&pagetype=item&itemid=18863444253&url=http%3A%2F%2Fitem.taobao.com%2Fitem.htm%3Fspm%3Da230r.1.14.59.eYY2KV%26id%3D18863444253&cl=2&ca=4&pageid=18863444253&linkid=&linkurl=&x=-550.5&y=352&ws=1241%3A170&el=html.ks-webkit537.ks-webkit.ks-chrome26.ks-chrome%3Ebody.tb-new.detached.active-tab-index-2%3E%23page%3E%23content.eshop.tb-content&rnd=9575&cna=fljZCWoFehoCAXBBfMt5hhR0
	public void hotClick() {

	}

	public String getHotClickUrl(HotClickParams hot) {
		StringBuffer url = new StringBuffer();

		url.append("http://hotclick.app.linezing.com/hotclick.gif?");
		url.append("shopid=" + hot.getShopId());
		url.append("&pagetype=" + hot.getPageType());
		url.append("&itemid=" + hot.getItemId());
		url.append("&url=" + hot.getUrl());
		url.append("&cl=" + hot.getCl());
		url.append("&ca=" + hot.getCa());
		url.append("&pageid=" + hot.getPageId());
		url.append("&linkid=" + hot.getLinkId());
		url.append("&linkurl=" + hot.getLinkUrl());
		url.append("&x=" + hot.getX());
		url.append("&y=" + hot.getY());
		url.append("&ws=" + hot.getWs());
		url.append("&el=" + hot.getEl());
		url.append("&rnd=" + hot.getRnd());
		url.append("&cna=" + hot.getCna());
		return url.toString();
	}

	public void execute() {

		HttpClientUtils.doGet(pageUrl);
		String pageStr = HttpClientUtils.getResponseAsString();

		refeshViewCount(getViewCountUrl(pageStr));

	}

	public String getShopId(String pageStr) {
		return StringParserUtils.parseString(pageStr,
				"(?:shopId:\")(.*?)(?:\")").get(0);
	}

	public String getItemId(String pageStr) {
		return StringParserUtils.parseString(pageStr,
				"(?:itemId:\")(.*?)(?:\")").get(0);
	}
}
