/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

/**
 * Shape detector
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class UnsaturatedResourcesFeatureDetector extends BasicDetectionAlgorithm {

    private Map<String, Object> smParams = new HashMap<String, Object>();
    
    @Override
    public void init() {
       super.init();
       smParams.put("mode", "minerals");
    }

    @Override
    public double detect() {
    	//TODO: only works for 1 base for now
    	Set<Unit> minerals = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
    	smParams.put("mode", "buildings");
    	Unit hive = null;
    	Set<Unit> buildings = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
    	for(Unit building : buildings) {
    		if(building.getTypeID() == UnitTypes.Zerg_Hive.ordinal() || building.getTypeID() == UnitTypes.Zerg_Lair.ordinal()) {
    			hive = building;
    		}
    			
    	}
    	if(hive != null) {
    		int mineralCount = 0;
    		for(Unit mineral : minerals) {
    			double distance = Math.sqrt(Math.pow(mineral.getX() - hive.getX(), 2) + Math.pow(mineral.getY() - hive.getY(), 2));
    			if (distance < 300) {
    				mineralCount += 1;
    			}
    		}
    		smParams.put("mode", "units");
    		Set<Unit> units = (Set<Unit>) sensoryMemory.getSensoryContent("", smParams);
    		int workerCount = 0;
    		for(Unit unit : units) {
    			if(unit.getTypeID() == UnitTypes.Zerg_Drone.ordinal()) {
    				workerCount += 1;
    			}
    		}
    		if(workerCount > (mineralCount *3)) return 1;
    	}
    	
		return 0;
    }
}
