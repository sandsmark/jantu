/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package com.iskrembilen.jantu.modules;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

/**
 * 
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class StarcraftSensoryMemory extends SensoryMemoryImpl {

    /*
     * an array of rgb pixels 
     */
    private int[] imageLayer;
    private int START_X=0;
    private int START_Y=0;
    private static int X_SIZE = 100;
    private static int Y_SIZE = 100;
    private Map<String,Object> sensorParam = new HashMap<String, Object>();

    @Override
    public void init() {
        //TODO read START_X, ... from params
        imageLayer = new int[10000];
        sensorParam.put("x",START_X);
        sensorParam.put("y",START_Y);
        sensorParam.put("xsize",X_SIZE);
        sensorParam.put("ysize",Y_SIZE);
        sensorParam.put("imageLayer", imageLayer);
    }

    @Override
    public void runSensors() {
        imageLayer = (int[]) environment.getState(sensorParam);
    }

    @Override
    public Object getSensoryContent(String string, Map<String, Object> params) {
        String mode = (String)params.get("mode");
        if("color".equalsIgnoreCase(mode)){
            int x = (Integer) params.get("x");
            int y = (Integer) params.get("y");
            int position = (y-START_Y)*X_SIZE + (x-START_X);
            return imageLayer[position];
        }else if("all".equalsIgnoreCase(mode)){
            return imageLayer;
        }
        return null;
    }
    
    @Override
    public void decayModule(long l) {
        //NA
    }
    
    @Override
    public Object getModuleContent(Object... os) {
        return null;
    }

}
