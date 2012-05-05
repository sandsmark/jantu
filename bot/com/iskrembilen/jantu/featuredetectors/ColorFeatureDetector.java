/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iskrembilen.jantu.featuredetectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

/**
 * Color detector
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class ColorFeatureDetector extends BasicDetectionAlgorithm {
	
	/*
	 * Red rgb value
	 */
    private int soughtColor = 0xFFFF0000;
    private Map<String, Object> smParams = new HashMap<String, Object>();
    
    @Override
    public void init() {
       super.init();
       smParams.put("mode","color");
       smParams.put("x",50);
       smParams.put("y",50);
       soughtColor = (Integer) getParam("color", 0xFFFF0000);
    }

    @Override
    public double detect() {
        int color = (Integer) sensoryMemory.getSensoryContent("visual",smParams);
        //cosine is a better comparison
        if(soughtColor == color){
            return 1.0;
        }
       return 0.0;
    }
}
