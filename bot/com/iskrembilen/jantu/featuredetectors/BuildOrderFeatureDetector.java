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

public class BuildOrderFeatureDetector extends MultipleDetectionAlgorithm {
	private static final int NR_WANTED_GATEWAYS = 3;
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
		int gatewayCount = 0;
		for(Unit building : buildings) {
			int type = building.getTypeID();
			if(type == UnitTypes.Protoss_Gateway.ordinal())
				gatewayCount += 1;
		}
		if(gatewayCount < NR_WANTED_GATEWAYS)
			doExcitate(this.pamNodeMap.get("needGateway"), 0.5);
	}
}
