package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;

import com.iskrembilen.jantu.Supply;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class SupplyBlockFeatureDetector extends BasicDetectionAlgorithm {
	
private Map<String, Object> smParams = new HashMap<String, Object>();
    
    @Override
    public void init() {
       super.init();
       smParams.put("mode", "supply");
    }

	@Override
	public double detect() {
		Supply supply = (Supply) sensoryMemory.getSensoryContent("", smParams);
		if(supply.getCurSupply() >= supply.getSupplyCap()) {
			return 1;
		}
		return 0;
		
	}

}
