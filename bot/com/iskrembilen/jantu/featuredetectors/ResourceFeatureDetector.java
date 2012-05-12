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
public class ResourceFeatureDetector extends MultipleDetectionAlgorithm {
    private Map<String, Object> smParams = new HashMap<String, Object>();
 
    @Override
    public void init() {
       super.init();
       smParams.put("mode","all");
    }
    
    private void doExcitate(PamLinkable linkable) {
    	pam.receiveExcitation(linkable, 1);
    }

	@Override
	public void detectLinkables() {
		Resources resources = (Resources) sensoryMemory.getSensoryContent("", smParams);
		if(resources.getMinerals() > 50) {
			doExcitate(this.pamNodeMap.get("affordWorker"));
		}
		
	}

}
