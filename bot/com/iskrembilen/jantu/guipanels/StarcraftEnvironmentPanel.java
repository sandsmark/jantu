/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package com.iskrembilen.jantu.guipanels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.iskrembilen.jantu.modules.StarcraftEnvironment;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanel;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanelImpl;

/**
 * A {@link GuiPanel} for the {@link StarcraftEnvironment}
 * @author Ryan McCall
 * @author Javier Snaider
 */
public class StarcraftEnvironmentPanel extends GuiPanelImpl {

    private static final Logger logger = Logger.getLogger(StarcraftEnvironmentPanel.class.getCanonicalName());
    private StarcraftEnvironment environment;
    private BufferedImage img = new BufferedImage(100,
            100, BufferedImage.TYPE_INT_RGB);

    /** Creates new form ButtonEnvironmentPanel */
    public StarcraftEnvironmentPanel() {
    }

    @Override
    public void initPanel(String[] param) {
        environment = (StarcraftEnvironment) agent.getSubmodule(ModuleName.Environment);
        if (environment != null) {
            refresh();
        } else {
            logger.log(Level.WARNING,
                    "Unable to parse module {1} Panel not initialized.",
                    new Object[]{0L, param[0]});
        }
    }

    @Override
    public void refresh() {
        img = (BufferedImage) environment.getModuleContent();
        repaint();
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0,0, getWidth(), getHeight());
        //Image scaledImage = img.getScaledInstance((int)(img.getWidth()*scalingFactor), (int)(img.getHeight()*scalingFactor), Image.SCALE_SMOOTH);
        int xCentered = (getWidth() - img.getWidth(this)) / 2;
        int yCentered = (getHeight() - img.getHeight(this)) / 2;
        g.drawImage(img, xCentered, yCentered, this);
    }
}
