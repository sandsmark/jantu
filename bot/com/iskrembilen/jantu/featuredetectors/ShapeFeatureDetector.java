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

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

/**
 * Shape detector
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class ShapeFeatureDetector extends BasicDetectionAlgorithm {
	
    public static final int TOLERANCE = 5;
    
    /*
     * Size of the shape in pixels.  
     */
    private int soughtArea = 1000;
    /*
     * The white background color should not be counted in determined the number of pixels in the shape
     */
    private int backgroundColor = 0xFFFFFFFF;

    private Map<String, Object> smParams = new HashMap<String, Object>();
    
    /*
     * The square in the environment is 20x20 = 400 pixels.  Thus it takes up ~40% of the area.
     * The circle has radius = 10 so its area ~= 314 pixels.  Thus it takes up ~31% of the area.
     */
    @Override
    public void init() {
       super.init();
       smParams.put("mode","all");

       soughtArea = (Integer) getParam("area", 1000);
       backgroundColor = (Integer) getParam("backgroundColor", 0xFFFFFFFF);
    }

    @Override
    public double detect() {
        int[] layer = (int[]) sensoryMemory.getSensoryContent("visual",smParams);
        int area=0;
        for(int i=0;i<layer.length;i++){
            if(layer[i]!=backgroundColor){
                area++;
            }
        }
        area=(area*1000)/layer.length;
        if(Math.abs(area-soughtArea) < TOLERANCE){
            return 1.0;
        }
        return 0.0;
    }
}
