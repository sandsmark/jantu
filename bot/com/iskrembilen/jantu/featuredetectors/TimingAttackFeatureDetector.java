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
public class TimingAttackFeatureDetector extends BasicDetectionAlgorithm {

    private Map<String, Object> smParams = new HashMap<String, Object>();
    
    @Override
    public void init() {
       super.init();
       smParams.put("mode", "units");
    }

    @Override
    public double detect() {
    	int zealotCount = 0;
    	for(Unit unit : (Set<Unit>) sensoryMemory.getSensoryContent("", smParams))  {
    		if(unit.getTypeID() == UnitTypes.Protoss_Zealot.ordinal()) {
    			zealotCount += 1;
    		}
    	}
    	if(zealotCount > 4) {
    		return 1;
    	}
		return 0;
    }
}
