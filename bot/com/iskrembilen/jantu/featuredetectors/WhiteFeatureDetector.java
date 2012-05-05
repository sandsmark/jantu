/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iskrembilen.jantu.featuredetectors;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sandsmark
 */
public class WhiteFeatureDetector extends BasicDetectionAlgorithm {
    private static final Logger logger = Logger.getLogger(WhiteFeatureDetector.class.getCanonicalName());
    public static final int TOLERANCE = 5;
    private Map<String, Object> smParams = new HashMap<String, Object>();
    
    private int backgroundColor = 0xFFFFFFFF;
    @Override
    public void init() {
       super.init();
       smParams.put("mode","all");

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
        if(area < TOLERANCE){
            return 1.0;
        }
        return 0.0;
    }
}
