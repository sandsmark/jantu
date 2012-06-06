package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.iskrembilen.jantu.Supply;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class SupplyBlockFeatureDetector extends BasicDetectionAlgorithm {
	
private Map<String, Object> smParams = new HashMap<String, Object>();
    
    @Override
    public void init() {
       super.init();
    }

	@Override
	public double detect() {
		smParams.put("mode", "supply");
		Supply supply = (Supply) sensoryMemory.getSensoryContent("", smParams);
		smParams.put("mode","buildings");
		Set<Unit> buildings = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
		int supplyInConstruction = 0;
		for(Unit building : buildings) {
			if(building.getTypeID() == UnitTypes.Protoss_Pylon.ordinal() && building.isBeingConstructed()) {
				supplyInConstruction += 8;
			}
		}
		if(supply != null && supply.getCurSupply() >= (int)((supply.getSupplyCap()+supplyInConstruction)*0.9)) {
			return 1;
		}
		return 0;
		
	}

}
