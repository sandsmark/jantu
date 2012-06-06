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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iskrembilen.jantu.BWAPIEventListener;
import com.iskrembilen.jantu.JNIBWAPI;
import com.iskrembilen.jantu.Resources;
import com.iskrembilen.jantu.Supply;
import com.iskrembilen.jantu.model.ChokePoint;
import com.iskrembilen.jantu.model.Region;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.types.UnitType;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;
import com.iskrembilen.jantu.util.BuildingPlacer;
import com.iskrembilen.jantu.util.TilePosition;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 *
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class StarcraftEnvironment extends EnvironmentImpl implements BWAPIEventListener {

    private static final Logger logger = Logger.getLogger(StarcraftEnvironment.class.getCanonicalName());
    
    private final int MINIMAP_SCALE = 3;
    private final int MILLISECONDS_PER_TICK = 1;
    private final int DEFAULT_TICKS_PER_RUN = 10;
    private final int DEFAULT_FRAMES_PER_TICK = 1;
    private int ticksPerRun = DEFAULT_TICKS_PER_RUN;

    private boolean matchRunning = false;

    private JNIBWAPI bwapi;
    BuildingPlacer buildingPlacer;
    
    private HashMap<Integer, Object> buildingTypes;
    
    private Semaphore runGameSemaphore;
    
    private class BWAPIThread extends Thread {
    	public void run() {
    		bwapi = new JNIBWAPI(StarcraftEnvironment.this);
    		bwapi.start();
    	}
    }
    
	@SuppressWarnings("serial")
	private class EnvironmentBackgroundTask extends FrameworkTaskImpl{
		public EnvironmentBackgroundTask(int ticksPerRun){
			super(ticksPerRun);
		}
		@Override
		protected void runThisFrameworkTask() {
			runGameSemaphore.release(DEFAULT_FRAMES_PER_TICK);
		}		
	}
    
    /**
     * Called by LIDA, start the BWAPI thread.
     */
    @Override
    public void init() {
    	runGameSemaphore = new Semaphore(DEFAULT_FRAMES_PER_TICK);
    	runGameSemaphore.drainPermits();
    	// BWAPI needs its own thread
    	BWAPIThread thread = new BWAPIThread();
    	thread.start();
    	
    	// We need a separate task/thread just for scheduling runs of the game 
    	taskSpawner.addTask(new EnvironmentBackgroundTask(ticksPerRun));
    	
    	
    	// We need to be able to separate out buildings from the rest of the units
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
		else if (what.equals("units")) {
			Collection<Unit> units = new HashSet<Unit>();
			for(Unit u: bwapi.getMyUnits()) {
				if (!buildingTypes.containsKey(u.getTypeID())) {
					units.add(u);
				}
			}
			return units;
		} else if (what.equals("buildings")) {
			Collection<Unit> buildings = new HashSet<Unit>();
			for(Unit u: bwapi.getMyUnits()) {
				if (buildingTypes.containsKey(u.getTypeID())) {
					buildings.add(u);
				}
			}
			return buildings;
		} else if (what.equals("minerals")) {
			Collection<Unit> minerals = new HashSet<Unit>();
			for(Unit u: bwapi.getNeutralUnits()) {
				if (u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
					minerals.add(u);
				}
			}
			return minerals;
		} else if (what.equals("geysers")) {
			Collection<Unit> geysers = new HashSet<Unit>();
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
		String action = (String)arg;
		if (action.equals("algorithm.mineMinerals")) {
			Unit drone = null;
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getTypeID() == UnitTypes.Protoss_Probe.ordinal() && unit.isIdle()) {
					drone = unit;
					break;
				}
			}
			// Didn't find drone
			if (drone == null)
				return;
			for (Unit minerals : bwapi.getNeutralUnits()) {
				if (minerals.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
					double distance = Math.sqrt(Math.pow(minerals.getX() - drone.getX(), 2) + Math.pow(minerals.getY() - drone.getY(), 2));

					if (distance < 400) {
						bwapi.rightClick(drone.getID(), minerals.getID());
						break;
					}
				}
			}
		} else if (action.equals("algorithm.buildWorker")) {
			bwapi.train(getOwnUnitID(UnitTypes.Protoss_Nexus), UnitTypes.Protoss_Probe.ordinal());
		} else if (action.equals("algorithm.buildSupply")) {
			TilePosition buildPos = findPlaceToBuild(UnitTypes.Protoss_Pylon);
			bwapi.build(getOwnUnitID(UnitTypes.Protoss_Probe), buildPos.x(), buildPos.y(), UnitTypes.Protoss_Pylon.ordinal());
		} else if (action.equals("algorithm.buildGateway")) {
			TilePosition buildPos = findPlaceToBuild(UnitTypes.Protoss_Gateway);
			bwapi.build(getOwnUnitID(UnitTypes.Protoss_Probe), buildPos.x(), buildPos.y(), UnitTypes.Protoss_Gateway.ordinal());
		} else if (action.equals("algorithm.buildCybercore")) {
			TilePosition buildPos = findPlaceToBuild(UnitTypes.Protoss_Cybernetics_Core);
			bwapi.build(getOwnUnitID(UnitTypes.Protoss_Probe), buildPos.x(), buildPos.y(), UnitTypes.Protoss_Cybernetics_Core.ordinal());
		} else if (action.equals("algorithm.trainZealot")) {
			bwapi.train(getOwnUnitID(UnitTypes.Protoss_Gateway), UnitTypes.Protoss_Zealot.ordinal());
		} else if (action.equals("algorithm.trainDragoon")) {
			bwapi.train(getOwnUnitID(UnitTypes.Protoss_Gateway), UnitTypes.Protoss_Dragoon.ordinal());
		}
	}
	
	private int getOwnUnitID(UnitType.UnitTypes type) {
		for(Unit unit : bwapi.getMyUnits()) {
			if(unit.getTypeID() == type.ordinal()) {
				return unit.getID();
			}
		}
		return -1;
	}
	
	private TilePosition findPlaceToBuild(UnitTypes unit) {
		for(Unit building : bwapi.getMyUnits()) {
			if (building.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()) {
				return buildingPlacer.getBuildLocationNear(new TilePosition(building.getTileX(), building.getTileY()), bwapi.getUnitType(unit.ordinal()));
			}
		}
		return null;
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
	 * Called from the GUI
	 */
	@Override
	public Object getModuleContent(Object... params){
		if (!matchRunning) {
			return new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
		}

		int height = bwapi.getMap().getHeight() * MINIMAP_SCALE;
		int width = bwapi.getMap().getWidth() * MINIMAP_SCALE;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, width, height);

		g2d.setColor(Color.blue);
		for (Unit unit: bwapi.getMyUnits()) {
			g2d.drawRect(unit.getX() * MINIMAP_SCALE/32, unit.getY() * MINIMAP_SCALE/32, 1, 1);
		}
		g2d.setColor(Color.red);
		for (Unit unit: bwapi.getEnemyUnits()) {
			g2d.drawRect(unit.getX() * MINIMAP_SCALE/32, unit.getY() * MINIMAP_SCALE/32, 1, 1);
		}
		g2d.setColor(Color.green);
		for (Unit unit: bwapi.getNeutralUnits()) {
			g2d.drawRect(unit.getX() * MINIMAP_SCALE/32, unit.getY() * MINIMAP_SCALE/32, 1, 1);
		}
		
		g2d.setColor(Color.gray);
		for(Region r: bwapi.getMap().getRegions()) {
			int coordinates[] = r.getCoordinates();
			for (int i=2; i<coordinates.length/2; i+=2) {
				g2d.drawLine(coordinates[i-2]*MINIMAP_SCALE/32, coordinates[i-1]*MINIMAP_SCALE/32, coordinates[i]*MINIMAP_SCALE/32, coordinates[i+1]*MINIMAP_SCALE/32);
			}
		}

		g2d.setColor(Color.yellow);
		for(ChokePoint c: bwapi.getMap().getChokePoints()) {
			int r = (int) c.getRadius() * MINIMAP_SCALE / 32;
			int x = c.getCenterX() * MINIMAP_SCALE / 32 - r/2;
			int y = c.getCenterY() * MINIMAP_SCALE / 32 - r/2;
			g2d.drawOval(x, y, r, r);
		}

		return img;
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
		buildingPlacer = new BuildingPlacer(bwapi);
		matchRunning = true;
	}

	/**
	 * Called every frame
	 * Called from BWAPI
	 */
	@Override
	public void gameUpdate() {
		try {
			runGameSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
