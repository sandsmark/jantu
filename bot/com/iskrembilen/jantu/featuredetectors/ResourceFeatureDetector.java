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
       smParams.put("mode","resources");
    }
    
    private void doExcitate(PamLinkable linkable, double value) {
    	pam.receiveExcitation(linkable, value);
    }

	@Override
	public void detectLinkables() {
		Resources resources = (Resources) sensoryMemory.getSensoryContent("", smParams);
		if(resources == null) return;
		if(resources.getMinerals() > 50) {
			doExcitate(this.pamNodeMap.get("affordWorker"), 0.5);
		}
		if(resources.getMinerals() > 100) {
			doExcitate(this.pamNodeMap.get("affordSupply"), 0.5);
		}
		if(resources.getMinerals() > 150) {
			doExcitate(pamNodeMap.get("affordGateway"), 0.5);
		}
	}

}
