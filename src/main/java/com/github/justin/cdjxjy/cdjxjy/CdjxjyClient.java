package com.github.justin.cdjxjy.cdjxjy;

import org.apache.log4j.Logger;

import com.github.justin.cdjxjy.cdjxjy.beans.Course;
import com.github.justin.cdjxjy.cdjxjy.utils.CourseUtils;
import com.github.justin.cdjxjy.cdjxjy.utils.LoginUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;

public class CdjxjyClient extends AbstractClient {
    private static final Logger log = Logger.getLogger(CdjxjyClient.class);

    public static void main(String[] args) {

        new CdjxjyClient().execute();
    }

    /**
     * how to simulate?
     * 1. login;
     * 2. get course list;
     * 3. elect courses;
     * 4. get elected courses list;
     * 5. study elected courses;
     * 6. complete all;
     */
    @Override
    public void task() {
        LoginUtils.login();

		CourseUtils.getCourseList();
		for (Course c : CourseUtils.courseList) {
			if (c.getcStudyTime() < PropUtils.getValueInt("totalStudyTime")) {
				CourseUtils.study(
						c,
						(PropUtils.getValueInt("totalStudyTime") - c
								.getcStudyTime())
								/ PropUtils.getValueInt("minPerPost") + 1);

			}
			CourseUtils.onlineComment(c.getcId());
			CourseUtils.submitStudyRecord(c.getcId());
		}

    }
}
