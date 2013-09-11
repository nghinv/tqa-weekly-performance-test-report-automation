package org.exoplatform.tqa.template;

public class ScenarioObject {
	private int baseResponseTime;
	private int preResponseTime;
	private int currentResponseTime;
	
	private int baseThroughput;
	private int preThroughput;
	private int currentThroughput;
	
	private double preResponseDiff;
	private double baseResponseDiff;

	private double preThroughputDiff;
	private double baseThroughputDiff;	
		
	private String baseResponseDiffColor;
	private String preResponseDiffColor;
	private String baseResponseDiffIndicator;
	private String preResponseDiffIndicator;
	
	private String baseThruDiffColor;
	private String preThruDiffColor;
	private String baseThruDiffIndicator;
	private String preThruDiffIndicator;	
	
	
	
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
	
	
}
