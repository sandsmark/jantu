package com.iskrembilen.jantu.featuredetectors;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.MultipleDetectionAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iskrembilen.jantu.Resources;

/**
 *
 * @author sandsmark
 */
public class ResourceFeatureDetector extends BasicDetectionAlgorithm {
    private Map<String, Object> smParams = new HashMap<String, Object>();
    int resourceCost;
 
    @Override
    public void init() {
       super.init();
       smParams.put("mode","resources");
       resourceCost = (Integer) getParam("cost", Integer.MAX_VALUE);
    }

	@Override
	public double detect() {
		Resources resources = (Resources) sensoryMemory.getSensoryContent("", smParams);
		if(resources != null && resources.getMinerals() > resourceCost) {
			return 0.5;
		}
		return 0;
	}
}
