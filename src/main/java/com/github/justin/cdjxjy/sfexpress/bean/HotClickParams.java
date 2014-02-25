package com.github.justin.cdjxjy.sfexpress.bean;

/**
 * 
 * 1. shopid: 65104567 2. pagetype: item 3. itemid: 18863444253 4. url:
 * http://item.taobao.com/item.htm?spm=a230r.1.14.59.XpTeTr&id=${itemID} 5. cl:
 * 0 6. ca: 1 7. pageid: ${itemID} 8. linkid: 9. linkurl: 10. x: -520.5 11. y:
 * 300 12. ws: 1241:193 13. el:
 * html.ks-webkit537.ks-webkit.ks-chrome26.ks-chrome
 * >body.tb-new.active-tab-index-0.detached>#page>#content.eshop.tb-content 14.
 * rnd: 35220 15. cna: fljZCWoFehoCAXBBfMt5hhR0
 * 
 * @author gschen
 * 
 */
public class HotClickParams {
	public static int i = 0;

	public HotClickParams(String pageUrl, String shopId, String itemId) {
		this.shopId = shopId;
		this.itemId = itemId;
		this.cna = "fljZCWoFehoCAXBBfMt5hhR0";
		this.pageId = itemId;
		this.url = pageUrl;
		this.pageType = "item";
		this.cl = "1";
		this.ca = "" + i++;
	}

	public void setX() {

	}

	public void setY() {

	}

	public void setWS() {

	}

	private String shopId;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCl() {
		return cl;
	}

	public void setCl(String cl) {
		this.cl = cl;
	}

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getWs() {
		return ws;
	}

	public void setWs(String ws) {
		this.ws = ws;
	}

	public String getEl() {
		return el;
	}

	public void setEl(String el) {
		this.el = el;
	}

	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public String getCna() {
		return cna;
	}

	public void setCna(String cna) {
		this.cna = cna;
	}

	private String pageType;
	private String itemId;
	private String url;
	private String cl;
	private String ca;
	private String pageId;
	private String linkId;
	private String linkUrl;
	private String x;
	private String y;
	private String ws;
	private String el;
	private String rnd;
	private String cna;
}
