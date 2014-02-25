package com.github.justin.cdjxjy.cdjxjy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.github.justin.cdjxjy.cdjxjy.beans.Course;
import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;
import com.github.justin.cdjxjy.sfexpress.utils.StringParserUtils;

public class CourseUtils {
	private static final Logger log = Logger.getLogger(CourseUtils.class);
	public static List<Course> courseList = new ArrayList<Course>();

	public final static void study(String cid) {
		study(cid, 1);
	}

	public final static void study(Course course, int studyTimes) {
		log.info("Studying course: " + course.getcName());
		study(course.getcId(), studyTimes);
	}

	// 必须加上referer 每次学习5分钟
	public final static void study(String cid, int studyTimes) {
		double r = Math.random();
		String url = "http://www.cdjxjy.com/Course/Background/Studenting.aspx?type1=&cid="
				+ cid + "&r=" + r;
		HttpClientUtils
				.doGet("http://www.cdjxjy.com/Course/Background/StudentIndex.aspx?uid="
						+ cid + "&r=" + r);
		// http://www.cdjxjy.com/Course/Background/StudentIndex.aspx?uid=9172cc66-3f22-47ed-93fc-e3c586a62b2e&r=0.2468559870834554
		log.info("Post url is: " + url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("postdata", "1"));

		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("referer", url));

		int i = 0;
		while (i++ < studyTimes)
			HttpClientUtils.doPost(url, params, headers);
	}

	public final static int getPageNum(Document doc) {

		Elements eles = doc.select("a[href*=javascript]");
		int length = eles.size();
		if (length == 0)
			return 1;
		else
			return Integer.parseInt(eles.get(length - 1).attr("href")
					.split("[']")[3]);
	}

	public final static String getFirstPageUrl() {

		String url = "http://www.cdjxjy.com/Online/student/StudentCenter.aspx";
		HttpClientUtils.doGet(url);
		Document doc = HttpClientUtils.getResponseAsDocument();

		String firstPageUrl = "http://www.cdjxjy.com/Online/student/"
				+ doc.select("iframe#CourseInfo").first().attr("src");

		return firstPageUrl;
	}

	public final static void handleFirstPage() {

		String firstPageUrl = getFirstPageUrl();
		log.info("First page url is: " + firstPageUrl);
		HttpClientUtils.doGet(firstPageUrl);
		Document doc = null;
		doc = HttpClientUtils.getResponseAsDocument();

		boolean parsed = parseCourseListPage(doc);

		int pageNum = -1;
		if (parsed == false)
			pageNum = 0;
		else
			pageNum = getPageNum(doc);
		log.info("Total page num is: " + pageNum);
		String viewState = PageParserUtils.getViewState(doc);

		for (int i = 2; i <= pageNum; ++i) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("__VIEWSTATE", viewState));
			params.add(new BasicNameValuePair("__EVENTTARGET", "AspNetPager1"));
			params.add(new BasicNameValuePair("__EVENTARGUMENT", "" + i));
			params.add(new BasicNameValuePair("AspNetPager1_input", ""
					+ (i - 1)));

			HttpClientUtils.doPost(firstPageUrl, params);
			parseCourseListPage(HttpClientUtils.getResponseAsDocument());
		}

		printCourseList();

	}

	public final static boolean parseCourseListPage(Document doc) {
		Elements eles1 = doc.select("a[onclick^=tt]");
		String[] cids = new String[4];
		for (int i = 0; i < eles1.size(); ++i) {
			cids[i] = eles1.get(i).attr("onclick").split("[']")[1];
		}

		Elements eles = doc.select("table[width*=375px] table");
		if (eles.size() == 0) {

			log.info("There is no courses.");
			return false;
		}

		for (int i = 0; i < eles.size(); ++i) {
			Elements es = eles.get(i).select("tr > td");
			courseList
					.add(new Course(cids[i], es.get(0).text().split("[:]")[1],
							es.get(1).text().split("[:]")[1], es.get(2).text()
									.split("[:]")[1], Integer.parseInt(es
									.get(3).text().split("[:]")[1]), Integer
									.parseInt(getNumbers(es.get(4).text()
											.split("[:]")[1]))));
		}
		return true;
	}

	// 截取数字
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	public final static List<Course> getCourseList() {

		handleFirstPage();
		return null;
	}

	public final static void printCourseList() {
		for (Course c : courseList)
			log.info(c.toString());

	}

	public final static void selectCourse() {

	}

	public final static void onlineComment(String cid) {
		double r = Math.random();
		HttpClientUtils
				.doGet("http://www.cdjxjy.com/Course/Background/CourseComment.aspx?cid="
						+ cid + "&r=" + r);
		onlineComment(cid, HttpClientUtils.getResponseAsDocument(), r);
		log.info(cid + " - comment complete!");
	}

	public final static void onlineComment(String cid, Document doc, double r) {
		String url = "http://www.cdjxjy.com/Course/Background/CourseComment.aspx?cid="
				+ cid + "&r=" + r;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__VIEWSTATE", PageParserUtils
				.getViewState(doc)));
		params.add(new BasicNameValuePair("__EVENTVALIDATION", PageParserUtils
				.getEventValidation(doc)));
		params.add(new BasicNameValuePair("txtCommentContent", PropUtils
				.getValue("commentTemplate")));
		params.add(new BasicNameValuePair("imgBtnSave.x", "0"));
		params.add(new BasicNameValuePair("imgBtnSave.y", "0"));

		// HttpClientUtils.doGet(url);
		HttpClientUtils.doPost(url, params);
	}

	public static String getRowGuid(String cid) {
		HttpClientUtils
				.doGet("http://www.cdjxjy.com/Course/Template/TemplateItem.aspx?param=OnlineLearning&paramindex=&cid="
						+ cid);
		String rtn = HttpClientUtils.getResponseAsString();
		// $("#DetailFrame").attr("src",
		// "../../Course/Background/CreateOnlineLearning.aspx?o=130521145138");
		String o = StringParserUtils.parseString(rtn, "(?:o=)(.*?)(?:\")").get(
				0);

		HttpClientUtils
				.doGet("http://www.cdjxjy.com/Course/Background/CreateOnlineLearning.aspx?o="
						+ o);
		rtn = HttpClientUtils.getResponseAsString();
		// var id = "5228de27-95cb-45d7-b2e6-2ac7e317cd69";
		String rowGuid = StringParserUtils.parseString(rtn,
				"(?:id = \")(.*?)(?:\";)").get(0);

		log.info(rowGuid);
		// var userguid = "f7cf1a72-a042-4b13-bc3e-61715e1b19ff";

		String userGuid = StringParserUtils.parseString(rtn,
				"(?:userguid = \")(.*?)(?:\";)").get(0);
		log.info(userGuid);
		return rowGuid;
	}

	public final static void submitStudyRecord(String cid) {
		String content = PropUtils.getValue("record");
		String experience = PropUtils.getValue("experience");
		String url = "http://www.cdjxjy.com/Course/Template/TemplateItem.aspx?param=OnlineLearning&paramindex=&cid="
				+ cid;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(
				"postdata",
				"<Content><Item><opt><![CDATA[N]]></opt><CreateDate><![CDATA[2013-05-21 13:23:27]]></CreateDate><UserGuid><![CDATA[f7cf1a72-a042-4b13-bc3e-61715e1b19ff]]></UserGuid><RowGuid><![CDATA["
						+ getRowGuid(cid)
						+ "]]></RowGuid><MainContents><![CDATA["
						+ content
						+ "]]></MainContents><PerceptionExperience><![CDATA["
						+ experience
						+ "]]></PerceptionExperience><IsTarget><![CDATA[1]]></IsTarget><CoursesGuid><![CDATA["
						+ cid
						+ "]]></CoursesGuid><StartTime><![CDATA[2013-02-04 13:24]]></StartTime><EndTime><![CDATA[2013-05-01 13:24]]></EndTime></Item></Content>"));
		HttpClientUtils.doPost(url, params);
		log.info(HttpClientUtils.getResponseAsString());

		afterSubmit(cid);
		log.info("Submit study record success!");
	}

	public static void afterSubmit(String cid) {
		String url = "http://www.cdjxjy.com/Course/Background/Studenting.aspx?cid="
				+ cid + "&type1=";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("postdata", "2"));

		HttpClientUtils.doPost(url, params);
	}
}
