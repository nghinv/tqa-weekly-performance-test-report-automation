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
	
	private String responeTimeResult;
	private String throughputResult;
	
	
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



	public String getResponeTimeResult() {
		return responeTimeResult;
	}
	public void setResponeTimeResult(String responeTimeResult) {
		this.responeTimeResult = responeTimeResult;
	}
	public String getThroughputResult() {
		return throughputResult;
	}
	public void setThroughputResult(String throughputResult) {
		this.throughputResult = throughputResult;
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
}
