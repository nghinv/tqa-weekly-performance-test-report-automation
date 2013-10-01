package org.exoplatform.tqa.template;

import java.util.List;

public class ScenarioObject {
	
	private String scenarioName;
	private String responseLabel;
	private String throughputLabel;
	private String responseLabelId;
	private String responseLabelAlias;
	private String scenarioAlias;
	private String scenarioUrl;
		
	private int baseResponseTime;
	private int preResponseTime;
	private int currentResponseTime;
	
	private int thisWeekResponseTime1;
	private int thisWeekResponseTime2;
	private int thisWeekResponseTime3;
	
	private int base1h;
	private int base1hStability;
	
	private int weekStability;
	private int baseStability;
	
	private String weekStabilityDisplay;
	private String baseStabilityDisplay;
	
	private String weekStabilityColor;
	private String baseStabilityColor;
	
	private String weekStabilityIndi;
	private String baseStabilityIndi;
	
	private int baseThroughput;
	private int preThroughput;
	private int currentThroughput;
	
	private double preResponseDiff;
	private double baseResponseDiff;
	
	private double preThroughputDiff;
	private double baseThroughputDiff;	
		
	private String baseResponseDiffColor;
	private String preResponseDiffColor;

	private String baseResponseChangeStatus;
	private String preResponseChangeStatus;
	
	private String baseResponseDiffIndicator;
	private String preResponseDiffIndicator;
	
	
	private String baseThruDiffColor;
	private String preThruDiffColor;
	
	private String baseThruDiffIndicator;
	private String preThruDiffIndicator;	
	
	private String enable;
	
	private BoundaryObject boundaryObject;
	
	private List dataLink;
	
	public int getBaseResponseTime() {
		return baseResponseTime;
	}



	public int getPreResponseTime() {
		return preResponseTime;
	}



	public void setPreResponseTime(int preResponseTime) {
		this.preResponseTime = preResponseTime;
	}



	public int getCurrentResponseTime() {
		return currentResponseTime;
	}



	public void setCurrentResponseTime(int currentResponseTime) {
		this.currentResponseTime = currentResponseTime;
	}



	public int getBaseThroughput() {
		return baseThroughput;
	}



	public void setBaseThroughput(int baseThroughput) {
		this.baseThroughput = baseThroughput;
	}



	public int getPreThroughput() {
		return preThroughput;
	}



	public void setPreThroughput(int preThroughput) {
		this.preThroughput = preThroughput;
	}



	public int getCurrentThroughput() {
		return currentThroughput;
	}



	public void setCurrentThroughput(int currentThroughput) {
		this.currentThroughput = currentThroughput;
	}



	public void setBaseResponseTime(int baseResponseTime) {
		this.baseResponseTime = baseResponseTime;
	}

	public double getPreResponseDiff() {
		return preResponseDiff;
	}
	public void setPreResponseDiff(double preResponseDiff) {
		this.preResponseDiff = preResponseDiff;
	}
	public double getBaseResponseDiff() {
		return baseResponseDiff;
	}
	public void setBaseResponseDiff(double baseResponseDiff) {
		this.baseResponseDiff = baseResponseDiff;
	}
	public double getPreThroughputDiff() {
		return preThroughputDiff;
	}
	public void setPreThroughputDiff(double preThroughputDiff) {
		this.preThroughputDiff = preThroughputDiff;
	}
	public double getBaseThroughputDiff() {
		return baseThroughputDiff;
	}
	public void setBaseThroughputDiff(double baseThroughputDiff) {
		this.baseThroughputDiff = baseThroughputDiff;
	}



	public String getBaseResponseDiffColor() {
		return baseResponseDiffColor;
	}



	public void setBaseResponseDiffColor(String baseResponseDiffColor) {
		this.baseResponseDiffColor = baseResponseDiffColor;
	}



	public String getPreResponseDiffColor() {
		return preResponseDiffColor;
	}



	public void setPreResponseDiffColor(String preResponseDiffColor) {
		this.preResponseDiffColor = preResponseDiffColor;
	}



	public String getBaseResponseDiffIndicator() {
		return baseResponseDiffIndicator;
	}



	public void setBaseResponseDiffIndicator(String baseResponseDiffIndicator) {
		this.baseResponseDiffIndicator = baseResponseDiffIndicator;
	}



	public String getPreResponseDiffIndicator() {
		return preResponseDiffIndicator;
	}



	public void setPreResponseDiffIndicator(String preResponseDiffIndicator) {
		this.preResponseDiffIndicator = preResponseDiffIndicator;
	}



	public String getBaseThruDiffColor() {
		return baseThruDiffColor;
	}



	public void setBaseThruDiffColor(String baseThruDiffColor) {
		this.baseThruDiffColor = baseThruDiffColor;
	}



	public String getPreThruDiffColor() {
		return preThruDiffColor;
	}



	public void setPreThruDiffColor(String preThruDiffColor) {
		this.preThruDiffColor = preThruDiffColor;
	}



	public String getBaseThruDiffIndicator() {
		return baseThruDiffIndicator;
	}



	public void setBaseThruDiffIndicator(String baseThruDiffIndicator) {
		this.baseThruDiffIndicator = baseThruDiffIndicator;
	}



	public String getPreThruDiffIndicator() {
		return preThruDiffIndicator;
	}



	public void setPreThruDiffIndicator(String preThruDiffIndicator) {
		this.preThruDiffIndicator = preThruDiffIndicator;
	}



	public String getScenarioName() {
		return scenarioName;
	}



	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}



	public String getResponseLabel() {
		return responseLabel;
	}



	public void setResponseLabel(String responseLabel) {
		this.responseLabel = responseLabel;
	}



	public String getThroughputLabel() {
		return throughputLabel;
	}



	public void setThroughputLabel(String throughputLabel) {
		this.throughputLabel = throughputLabel;
	}



	public String getResponseLabelId() {
		return responseLabelId;
	}



	public void setResponseLabelId(String responseLabelId) {
		this.responseLabelId = responseLabelId;
	}



	public BoundaryObject getBoundaryObject() {
		return boundaryObject;
	}



	public void setBoundaryObject(BoundaryObject boundaryObject) {
		this.boundaryObject = boundaryObject;
	}



	public String getBaseResponseChangeStatus() {
		return baseResponseChangeStatus;
	}



	public void setBaseResponseChangeStatus(String baseResponseChangeStatus) {
		this.baseResponseChangeStatus = baseResponseChangeStatus;
	}



	public String getPreResponseChangeStatus() {
		return preResponseChangeStatus;
	}



	public void setPreResponseChangeStatus(String preResponseChangeStatus) {
		this.preResponseChangeStatus = preResponseChangeStatus;
	}



	public String getResponseLabelAlias() {
		return responseLabelAlias;
	}



	public void setResponseLabelAlias(String responseLabelAlias) {
		this.responseLabelAlias = responseLabelAlias;
	}



	public String getScenarioAlias() {
		return scenarioAlias;
	}



	public void setScenarioAlias(String scenarioAlias) {
		this.scenarioAlias = scenarioAlias;
	}



	public String getScenarioUrl() {
		return scenarioUrl;
	}



	public void setScenarioUrl(String scenarioUrl) {
		this.scenarioUrl = scenarioUrl;
	}



	public String getEnable() {
		return enable;
	}



	public void setEnable(String enable) {
		this.enable = enable;
	}



	public List getDataLink() {
		return dataLink;
	}



	public void setDataLink(List dataLink) {
		this.dataLink = dataLink;
	}



	public int getThisWeekResponseTime1() {
		return thisWeekResponseTime1;
	}



	public void setThisWeekResponseTime1(int thisWeekResponseTime1) {
		this.thisWeekResponseTime1 = thisWeekResponseTime1;
	}



	public int getThisWeekResponseTime2() {
		return thisWeekResponseTime2;
	}



	public void setThisWeekResponseTime2(int thisWeekResponseTime2) {
		this.thisWeekResponseTime2 = thisWeekResponseTime2;
	}



	public int getThisWeekResponseTime3() {
		return thisWeekResponseTime3;
	}



	public void setThisWeekResponseTime3(int thisWeekResponseTime3) {
		this.thisWeekResponseTime3 = thisWeekResponseTime3;
	}



	public int getBase1h() {
		return base1h;
	}



	public void setBase1h(int base1h) {
		this.base1h = base1h;
	}



	public int getBase1hStability() {
		return base1hStability;
	}



	public void setBase1hStability(int base1hStability) {
		this.base1hStability = base1hStability;
	}



	public int getWeekStability() {
		return weekStability;
	}



	public void setWeekStability(int weekStability) {
		this.weekStability = weekStability;
	}



	public int getBaseStability() {
		return baseStability;
	}



	public void setBaseStability(int baseStability) {
		this.baseStability = baseStability;
	}



	public String getWeekStabilityDisplay() {
		return weekStabilityDisplay;
	}



	public void setWeekStabilityDisplay(String weekStabilityDisplay) {
		this.weekStabilityDisplay = weekStabilityDisplay;
	}



	public String getBaseStabilityDisplay() {
		return baseStabilityDisplay;
	}



	public void setBaseStabilityDisplay(String baseStabilityDisplay) {
		this.baseStabilityDisplay = baseStabilityDisplay;
	}



	public String getWeekStabilityColor() {
		return weekStabilityColor;
	}



	public void setWeekStabilityColor(String weekStabilityColor) {
		this.weekStabilityColor = weekStabilityColor;
	}



	public String getBaseStabilityColor() {
		return baseStabilityColor;
	}



	public void setBaseStabilityColor(String baseStabilityColor) {
		this.baseStabilityColor = baseStabilityColor;
	}



	public String getWeekStabilityIndi() {
		return weekStabilityIndi;
	}



	public void setWeekStabilityIndi(String weekStabilityIndi) {
		this.weekStabilityIndi = weekStabilityIndi;
	}



	public String getBaseStabilityIndi() {
		return baseStabilityIndi;
	}



	public void setBaseStabilityIndi(String baseStabilityIndi) {
		this.baseStabilityIndi = baseStabilityIndi;
	}

	
}
