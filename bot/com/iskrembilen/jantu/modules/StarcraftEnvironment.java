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
package com.iskrembilen.jantu.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iskrembilen.jantu.BWAPIEventListener;
import com.iskrembilen.jantu.JNIBWAPI;
import com.iskrembilen.jantu.Resources;
import com.iskrembilen.jantu.Supply;
import com.iskrembilen.jantu.model.Action;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 *
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class StarcraftEnvironment extends EnvironmentImpl implements BWAPIEventListener {

    private static final Logger logger = Logger.getLogger(StarcraftEnvironment.class.getCanonicalName());
    
    private final int DEFAULT_TICKS_PER_RUN = 100;
    private int ticksPerRun = DEFAULT_TICKS_PER_RUN;

    private boolean matchRunning = false;

    private JNIBWAPI bwapi;
    
    private HashMap<Integer, Object> buildingTypes;
    
    private class BWAPIThread extends Thread {
    	public void run() {
    		bwapi = new JNIBWAPI(StarcraftEnvironment.this);
    		bwapi.start();
    	}
    }
    
    /**
     * Called by LIDA, start the BWAPI thread.
     */
    @Override
    public void init() {
    	BWAPIThread thread = new BWAPIThread();
    	thread.start();
    	
    	buildingTypes = new HashMap<Integer, Object>();
    	
    	// Terran buildings
    	buildingTypes.put(UnitTypes.Terran_Academy.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Armory.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Barracks.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Bunker.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Command_Center.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Comsat_Station.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Control_Tower.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Engineering_Bay.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Factory.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Machine_Shop.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Missile_Turret.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Nuclear_Silo.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Physics_Lab.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Refinery.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Science_Facility.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Starport.ordinal(), null);
    	buildingTypes.put(UnitTypes.Terran_Supply_Depot.ordinal(), null);
    	
    	// Protoss buildings
    	buildingTypes.put(UnitTypes.Protoss_Arbiter_Tribunal.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Assimilator.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Citadel_of_Adun.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Cybernetics_Core.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Fleet_Beacon.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Forge.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Gateway.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Nexus.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Observatory.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Photon_Cannon.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Pylon.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Robotics_Facility.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Robotics_Support_Bay.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Shield_Battery.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Stargate.ordinal(), null);
    	buildingTypes.put(UnitTypes.Protoss_Templar_Archives.ordinal(), null);
    	
    	// Zerg buildings
    	buildingTypes.put(UnitTypes.Zerg_Creep_Colony.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Defiler_Mound.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Evolution_Chamber.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Extractor.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Greater_Spire.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Hatchery.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Hive.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Hydralisk_Den.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Infested_Command_Center.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Lair.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Nydus_Canal.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Queens_Nest.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Spawning_Pool.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Spire.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Spore_Colony.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Sunken_Colony.ordinal(), null);
    	buildingTypes.put(UnitTypes.Zerg_Ultralisk_Cavern.ordinal(), null);
    }
    

    /**
     * BWAPI has a connection to starcraft
     */
	@Override
	public void connected() {
		bwapi.loadTypeData();
	}

	/**
	 * Read data about environment
	 * Called from LIDA
	 */
	@Override
	public Object getState(Map<String, ?> parameters) {
		String what = (String)parameters.get("mode");
		if(what.equals("playing")) {
			return matchRunning;
		}
		if (what.equals("units")) {
			Collection<Unit> units = new ArrayList<Unit>();
			for(Unit u: bwapi.getMyUnits()) {
				if (!buildingTypes.containsKey(u.getTypeID())) {
					units.add(u);
				}
			}
		} else if (what.equals("buildings")) {
			Collection<Unit> buildings = new ArrayList<Unit>();
			for(Unit u: bwapi.getMyUnits()) {
				if (buildingTypes.containsKey(u.getTypeID())) {
					buildings.add(u);
				}
			}
		} else if (what.equals("minerals")) {
			Collection<Unit> minerals = new ArrayList<Unit>();
			for(Unit u: bwapi.getNeutralUnits()) {
				if (u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
					minerals.add(u);
				}
			}
			return minerals;
		} else if (what.equals("geysers")) {
			Collection<Unit> geysers = new ArrayList<Unit>();
			for(Unit u: bwapi.getNeutralUnits()) {
				if (u.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) {
					geysers.add(u);
				}
			}
			return geysers;
		} else if (what.equals("resources")) {
			Resources resources = new Resources(bwapi.getSelf().getMinerals(), bwapi.getSelf().getGas());
			return resources;
		} else if (what.equals("supply")) {
			Supply supply = new Supply(bwapi.getSelf().getSupplyUsed(), bwapi.getSelf().getSupplyTotal());
			return supply;
		}
		return null;
	}

	/**
	 * Do an action
	 * Called from LIDA
	 */
	@Override
	public void processAction(Object arg) {
		Action action = (Action)arg;
		switch (action.type) {
		case Attack:
			bwapi.attack(action.unit.getID(), action.x, action.y);
			break;
		case Build:
			bwapi.build(action.unit.getID(), action.x, action.y, action.buildingType);
			break;
		case Mine:
			bwapi.rightClick(action.unit.getID(), action.x, action.y);
			break;
		case Morph:
			bwapi.morph(action.unit.getID(), action.targetType);
			break;
		case Move:
			bwapi.move(action.unit.getID(), action.x, action.y);
			break;
		default:
		}
	}

	/**
	 * Reset the environment state
	 * Called from LIDA
	 */
	@Override
	public void resetState() {
		bwapi.restartGame();
	}

	/**
	 * Called when the game starts
	 * Called from BWAPI
	 */
	@Override
	public void gameStarted() {
		logger.log(Level.INFO, "Game started", TaskManager.getCurrentTick());
		
		bwapi.enableUserInput();
		bwapi.enablePerfectInformation();
		bwapi.setGameSpeed(0);
		bwapi.loadMapData(true);
		matchRunning = true;
	}

	/**
	 * Called every frame
	 * Called from BWAPI
	 */
	@Override
	public void gameUpdate() {} // SANDSMARK DON'T CARE

	@Override
	public void gameEnded() {
		matchRunning = false;		
	}
	@Override
	public void matchEnded(boolean winner) {
		matchRunning = false;
	}

	@Override
	public void keyPressed(int keyCode) {} //SANDSMARK DON'T CARE


	/**
	 * Called when a player leaves the game
	 */
	@Override
	public void playerLeft(int id) {
		// If another player leaves, we leave
		if (id != bwapi.getSelf().getID())
			bwapi.leaveGame();
	}

	@Override
	public void nukeDetect(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nukeDetect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitDiscover(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitEvade(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitShow(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitHide(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitCreate(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitDestroy(int unitID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitMorph(int unitID) {
		// TODO Auto-generated method stub
		
	}
}
