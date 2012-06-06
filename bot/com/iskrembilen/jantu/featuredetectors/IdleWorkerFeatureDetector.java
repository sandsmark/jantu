package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class IdleWorkerFeatureDetector extends BasicDetectionAlgorithm {
	
	 private Map<String, Object> smParams = new HashMap<String, Object>();
	 
	 @Override
	public void init() {
		super.init();
		smParams.put("mode", "units");
	}

	@Override
	public double detect() {
		Set<Unit> units = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
		for(Unit unit : units) {
			if (unit.getTypeID() == UnitTypes.Protoss_Probe.ordinal()) {
				if(unit.getOrderID() == 3) {
					return 1;
				}
			}
		}
		return 0;
	}

}
