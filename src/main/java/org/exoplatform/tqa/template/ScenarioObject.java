package org.exoplatform.tqa.template;

public class ScenarioObject {
	private double baseResponseTime;
	private double preResponseTime;
	private double currentResponseTime;
	
	private double baseThroughput;
	private double preThroughput;
	private double currentThroughput;
	
	private double preResponseDiff;
	private double baseResponseDiff;

	private double preThroughputDiff;
	private double baseThroughputDiff;	
	
	private String responeTimeResult;
	private String throughputResult;
	
	
	public double getBaseResponseTime() {
		return baseResponseTime;
	}
	public void setBaseResponseTime(double baseResponseTime) {
		this.baseResponseTime = baseResponseTime;
	}
	public double getPreResponseTime() {
		return preResponseTime;
	}
	public void setPreResponseTime(double preResponseTime) {
		this.preResponseTime = preResponseTime;
	}
	public double getCurrentResponseTime() {
		return currentResponseTime;
	}
	public void setCurrentResponseTime(double currentResponseTime) {
		this.currentResponseTime = currentResponseTime;
	}
	public double getBaseThroughput() {
		return baseThroughput;
	}
	public void setBaseThroughput(double baseThroughput) {
		this.baseThroughput = baseThroughput;
	}
	public double getPreThroughput() {
		return preThroughput;
	}
	public void setPreThroughput(double preThroughput) {
		this.preThroughput = preThroughput;
	}
	public double getCurrentThroughput() {
		return currentThroughput;
	}
	public void setCurrentThroughput(double currentThroughput) {
		this.currentThroughput = currentThroughput;
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
