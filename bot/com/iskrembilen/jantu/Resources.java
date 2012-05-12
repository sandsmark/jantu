package com.iskrembilen.jantu;

public class Resources {
	private int minerals;
	private int gas;
	
	public Resources() {
	}
	
	public Resources(int minerals, int gas) {
		this.minerals = minerals;;
		this.gas = gas;
	}
	
	public int getMinerals() {
		return minerals;
	}
	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}
	public int getGas() {
		return gas;
	}
	public void setGas(int gas) {
		this.gas = gas;
	}
	
}
