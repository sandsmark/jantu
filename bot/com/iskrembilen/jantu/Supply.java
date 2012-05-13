package com.iskrembilen.jantu;

public class Supply {
	
	private int curSupply;
	private int supplyCap;
	
	public Supply() {
	}

	public Supply(int curSupply, int supplyCap) {
		this.curSupply = curSupply;
		this.supplyCap = supplyCap;
	}
	

	public int getCurSupply() {
		return curSupply;
	}
	public void setCurSupply(int curSupply) {
		this.curSupply = curSupply;
	}
	public int getSupplyCap() {
		return supplyCap;
	}
	public void setSupplyCap(int supplyCap) {
		this.supplyCap = supplyCap;
	}

}
