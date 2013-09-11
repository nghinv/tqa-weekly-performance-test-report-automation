package org.exoplatform.tqa.template;

import java.util.List;

public class Configurations {
	private String generatedPath;
	private String dataPath;
	private List scenariosList;
	private String wikiLink;
	private String webdavLogin;
	private String webdavPass;
	private String webdavPath;
	private String prefix;
	private String dataDelimiter;
	private String weekBase;
	private String weekPrevious;
	private String weekThis;
	
	private int responseColumn;
	private int throughputColumn;
	private int responseAvgColumn;
	private int errorColumn;
	
	private BoundaryObject boundaryObject;
	
	public String getGeneratedPath() {
		return generatedPath;
	}
	public void setGeneratedPath(String generatedPath) {
		this.generatedPath = generatedPath;
	}
	
	
	
	public String getDataPath() {
		return dataPath;
	}
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	public List getScenariosList() {
		return scenariosList;
	}
	public void setScenariosList(List scenariosList) {
		this.scenariosList = scenariosList;
	}
	public String getWikiLink() {
		return wikiLink;
	}
	public void setWikiLink(String wikiLink) {
		this.wikiLink = wikiLink;
	}
	public String getWebdavLogin() {
		return webdavLogin;
	}
	public void setWebdavLogin(String webdavLogin) {
		this.webdavLogin = webdavLogin;
	}
	public String getWebdavPass() {
		return webdavPass;
	}
	public void setWebdavPass(String webdavPass) {
		this.webdavPass = webdavPass;
	}
	public String getWebdavPath() {
		return webdavPath;
	}
	public void setWebdavPath(String webdavPath) {
		this.webdavPath = webdavPath;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getDataDelimiter() {
		return dataDelimiter;
	}
	public void setDataDelimiter(String dataDelimiter) {
		this.dataDelimiter = dataDelimiter;
	}
	public String getWeekBase() {
		return weekBase;
	}
	public void setWeekBase(String weekBase) {
		this.weekBase = weekBase;
	}
	public String getWeekPrevious() {
		return weekPrevious;
	}
	public void setWeekPrevious(String weekPrevious) {
		this.weekPrevious = weekPrevious;
	}
	public String getWeekThis() {
		return weekThis;
	}
	public void setWeekThis(String weekThis) {
		this.weekThis = weekThis;
	}
	public int getResponseColumn() {
		return responseColumn;
	}
	public void setResponseColumn(int responseColumn) {
		this.responseColumn = responseColumn;
	}
	public int getThroughputColumn() {
		return throughputColumn;
	}
	public void setThroughputColumn(int throughputColumn) {
		this.throughputColumn = throughputColumn;
	}
	public int getResponseAvgColumn() {
		return responseAvgColumn;
	}
	public void setResponseAvgColumn(int responseAvgColumn) {
		this.responseAvgColumn = responseAvgColumn;
	}
	public int getErrorColumn() {
		return errorColumn;
	}
	public void setErrorColumn(int errorColumn) {
		this.errorColumn = errorColumn;
	}
	public BoundaryObject getBoundaryObject() {
		return boundaryObject;
	}
	public void setBoundaryObject(BoundaryObject boundaryObject) {
		this.boundaryObject = boundaryObject;
	}
	
	
}
