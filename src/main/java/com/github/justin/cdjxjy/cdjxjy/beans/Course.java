package com.github.justin.cdjxjy.cdjxjy.beans;

public class Course {

	private String cId;

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getcNature() {
		return cNature;
	}

	public void setcNature(String cNature) {
		this.cNature = cNature;
	}

	public String getcDiscipline() {
		return cDiscipline;
	}

	public void setcDiscipline(String cDiscipline) {
		this.cDiscipline = cDiscipline;
	}

	public int getcPeriod() {
		return cPeriod;
	}

	public void setcPeriod(int cPeriod) {
		this.cPeriod = cPeriod;
	}

	public int getcStudyTime() {
		return cStudyTime;
	}

	public void setcStudyTime(int cStudyTime) {
		this.cStudyTime = cStudyTime;
	}

	private String cName;
	private String cNature;// 课程性质
	private String cDiscipline;// 课程学科
	private int cPeriod;// 学时
	private int cStudyTime;// 学习累计时间

	public Course(String cId, String cName, String cNature, String cDiscipline,
			int cPeriod, int cStudyTime) {
		this.cId = cId;
		this.cName = cName;
		this.cNature = cNature;
		this.cDiscipline = cDiscipline;
		this.cPeriod = cPeriod;
		this.cStudyTime = cStudyTime;
	}

	@Override
	public String toString() {

		return "\n	cid: " + cId + "	cName: " + cName + "\n	cNature: " + cNature
				+ "	cDiscipline: " + cDiscipline + "	cPeriod: " + cPeriod
				+ "	cStudyTime: " + cStudyTime;
	}

}
