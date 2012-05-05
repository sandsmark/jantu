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
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iskrembilen.jantu.BWAPIEventListener;
import com.iskrembilen.jantu.JNIBWAPI;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.util.BWColor;

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
    
    private BufferedImage image;
    private BackgroundTask backgroundTask;
    private final int DEFAULT_TICKS_PER_RUN = 100;
    private int ticksPerRun = DEFAULT_TICKS_PER_RUN;
    public static final int ENVIRONMENT_WIDTH = 100;
    public static final int ENVIRONMENT_HEIGHT = 100;
    private int lastPressedButton =0;

    private JNIBWAPI bwapi;
    
    private class BWAPIThread extends Thread {
    	public void run() {
    		bwapi = new JNIBWAPI(StarcraftEnvironment.this);
    		bwapi.start();
    	}
    }
    
    @Override
    public void init() {
        image = new BufferedImage(ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT, BufferedImage.TYPE_INT_RGB);
        clearImage();
        ticksPerRun = (Integer)getParam("ticksPerRun",DEFAULT_TICKS_PER_RUN);
        backgroundTask = new BackgroundTask(ticksPerRun);
        taskSpawner.addTask(backgroundTask);

        BWAPIThread bwapiThread = new BWAPIThread();
        bwapiThread.start();
    }

    private void clearImage() {
        image.getGraphics().setColor(Color.WHITE);
        image.getGraphics().fillRect(0, 0, ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT);
    }

    @Override
    public Object getState(Map<String, ?> params) {
        int startX = (Integer)params.get("x");
        int startY = (Integer)params.get("y");
        int w = (Integer)params.get("xsize");
        int h = (Integer)params.get("ysize");
        int[] rgbArray = (int[])params.get("imageLayer");

        return image.getRGB(startX, startY, w, h, rgbArray, 0, w);
    }

    /**
     * @return the lastPressedButton
     */
    public int getLastPressedButton() {
        return lastPressedButton;
    }

    private class BackgroundTask extends FrameworkTaskImpl {

        public BackgroundTask(int ticksPerRun) {
            super(ticksPerRun);
        }

        @Override
        protected void runThisFrameworkTask() {
            driveEnvironment();
        }
    }
    private final int upperLeftX = 40;
    private final int upperLeftY = 40;
    private final int objectSizeX = 20;
    private final int objectSizeY = 20;

    public void driveEnvironment() {
        int randomValue = (int) (Math.random() * 3);

        Graphics g = image.getGraphics();
        switch (randomValue) {
            case 0:
                clearImage();
                g.setColor(Color.RED);
                g.fillRect(upperLeftX, upperLeftY, objectSizeX, objectSizeY);
                logger.log(Level.FINE, "Red square displayed.", TaskManager.getCurrentTick());
                break;
            case 1:
                clearImage();
                g.setColor(Color.BLUE);
                g.fillOval(upperLeftX, upperLeftY, objectSizeX, objectSizeY);
                logger.log(Level.FINE, "Blue circle displayed.", TaskManager.getCurrentTick());
                break;
            case 2:
                clearImage();
                logger.log(Level.FINE, "Image cleared.", TaskManager.getCurrentTick());
                break;
            default:
                break;
        }
    }

    @Override
    public void resetState() {
        clearImage();
    }

    @Override
    public String toString() {
        return "ButtonEnvironment";
    }

    @Override
    public Object getModuleContent(Object... params) {
        return image;
    }

    @Override
    public void processAction(Object o) {
        String action = (String) o;
        if ("algorithm.press1".equalsIgnoreCase(action)) {
           logger.log(Level.INFO, "Button 1 pressed", TaskManager.getCurrentTick());
            lastPressedButton = 1;
        } else if("algorithm.press2".equalsIgnoreCase(action)) {
            logger.log(Level.INFO, "Button 2 pressed", TaskManager.getCurrentTick());
            lastPressedButton = 2;
        } else if("algorithm.releasePress".equalsIgnoreCase(action)) {
            logger.log(Level.INFO, "Button released", TaskManager.getCurrentTick());
            lastPressedButton = 0;
        }else{
            logger.log(Level.INFO, "Got an invalid action", TaskManager.getCurrentTick());
        }
    }

	@Override
	public void connected() {
	}

	@Override
	public void gameStarted() {
	}

	@Override
	public void gameUpdate() {
		for(Unit u : bwapi.getAllUnits())
		{
			bwapi.drawCircle(u.getX(), u.getY(), 5, BWColor.RED, true, false);
		}
	}

	@Override
	public void gameEnded() {
	}

	@Override
	public void keyPressed(int keyCode) {
	}

	@Override
	public void matchEnded(boolean winner) {
	}

	@Override
	public void playerLeft(int id) {
	}

	@Override
	public void nukeDetect(int x, int y) {
	}

	@Override
	public void nukeDetect() {
	}

	@Override
	public void unitDiscover(int unitID) {
	}

	@Override
	public void unitEvade(int unitID) {
	}

	@Override
	public void unitShow(int unitID) {
	}

	@Override
	public void unitHide(int unitID) {
	}

	@Override
	public void unitCreate(int unitID) {
	}

	@Override
	public void unitDestroy(int unitID) {
	}

	@Override
	public void unitMorph(int unitID) {		
	}
}
