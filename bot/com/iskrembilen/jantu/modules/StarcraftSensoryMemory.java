package com.iskrembilen.jantu.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.iskrembilen.jantu.Resources;
import com.iskrembilen.jantu.Supply;
import com.iskrembilen.jantu.model.Unit;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

public class StarcraftSensoryMemory extends SensoryMemoryImpl {

	private Set<Unit> unitObjects = new ConcurrentHashSet<Unit>();
	private Set<Unit> buildingObjects = new ConcurrentHashSet<Unit>();
	private Set<Unit> mineralObjects = new ConcurrentHashSet<Unit>();
	private Set<Unit> geyserObjects = new ConcurrentHashSet<Unit>();
	Resources resources;
	private Supply supply;	
 
    private Map<String,Object> sensorParam = new HashMap<String, Object>();

    @Override
    public void init() {
    }

    @Override
    public void runSensors() {
    	sensorParam.put("mode", "playing");
    	if(!((Boolean)environment.getState(sensorParam)).booleanValue()) {
    		return;
    	} else {
    		supply = new Supply();
    		resources = new Resources();
    	}
    	sensorParam.put("mode","units");
    	unitObjects.clear();
    	unitObjects.addAll((Set<Unit>) environment.getState(sensorParam));
    	
    	sensorParam.put("mode","buildings");
    	buildingObjects.clear();
    	buildingObjects.addAll((Set<Unit>) environment.getState(sensorParam));
    	
    	sensorParam.put("mode","minerals");
    	mineralObjects.clear();
    	mineralObjects.addAll((Set<Unit>) environment.getState(sensorParam));
    	
    	sensorParam.put("mode","geysers");
    	geyserObjects.clear();
    	geyserObjects.addAll((Set<Unit>) environment.getState(sensorParam));
    	
    	sensorParam.put("mode", "resources");
    	Resources tempRes = (Resources) environment.getState(sensorParam);
    	resources.setMinerals(tempRes.getMinerals());
    	resources.setGas(tempRes.getGas());
    	
    	sensorParam.put("mode", "supply");
    	Supply tempSupply = (Supply) environment.getState(sensorParam);
    	supply.setCurSupply(tempSupply.getCurSupply());
    	supply.setSupplyCap(tempSupply.getSupplyCap());
    }

    @Override
    public Object getSensoryContent(String string, Map<String, Object> params) {
        String mode = (String)params.get("mode");
        if("units".equals(mode)) {
            return unitObjects;
        } else if("buildings".equals(mode)){
            return buildingObjects;
        } else if("minerals".equals(mode)){
            return mineralObjects;
        } else if("geysers".equals(mode)){
            return geyserObjects;
        } else if("resources".equals(mode)) {
        	return resources;
        } else if("supply".equals(mode)) {
        	return supply;
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
