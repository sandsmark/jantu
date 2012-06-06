package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.iskrembilen.jantu.Resources;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.MultipleDetectionAlgorithm;

public class StructureFeatureDetector extends MultipleDetectionAlgorithm {
	private Map<String, Object> smParams = new HashMap<String, Object>();

	@Override
	public void init() {
		super.init();
		smParams.put("mode","buildings");
	}

	private void doExcitate(PamLinkable linkable, double value) {
		pam.receiveExcitation(linkable, value);
	}

	@Override
	public void detectLinkables() {
		Set<Unit> buildings = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
		for(Unit building : buildings) {
			int type = building.getTypeID();
			if(type == UnitTypes.Protoss_Nexus.ordinal()) 
				doExcitate(this.pamNodeMap.get("nexus"), 0.5);				
			else if(type == UnitTypes.Protoss_Gateway.ordinal())
				doExcitate(this.pamNodeMap.get("gateway"), 0.5);
			else if(type == UnitTypes.Protoss_Cybernetics_Core.ordinal())
				doExcitate(pamNodeMap.get("cybercore"), 0.5);
		}
	}
}
