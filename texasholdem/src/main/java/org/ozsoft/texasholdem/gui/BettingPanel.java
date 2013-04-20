// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Panel for selecting the amount to bet or raise.
 * 
 * @author Oscar Stigter
 */
public class BettingPanel extends JPanel implements ChangeListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 171860711156799253L;

    /** Number of increasing amounts to choose from (ticks on slider bar). */
    private static final int NO_OF_TICKS = 10;

    /** Slider with the amount to bet or raise. */
    private final JSlider slider;

    /** Label with selected amount. */
    private final JLabel valueLabel;
    
    /** Bet/Raise button. */
    private final JButton betRaiseButton;
    
    /** Cancel button. */
    private final JButton cancelButton;
    
//    /** Minimum bet amount. */
//    private int minBet;
//    
//    /** Maximum bet amount (i.e. player's remaining cash). */
//    private int maxBet;
    
    /** Incremental bet amounts mapped to slider's index. */
    private HashMap<Integer, Integer> values;
    
    /**
     * Constructor.
     * 
     * @param main
     *            The main frame.
     */
    public BettingPanel() {
//        setBackground(UIConstants.TABLE_COLOR);
        setBackground(Color.WHITE);
        
        values = new HashMap<Integer, Integer>();
        
        setLayout(new FlowLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel label = new JLabel("Amount to bet:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 0);
        add(label, gbc);
        
        slider = new JSlider();
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 0, 10);
        add(slider, gbc);

        valueLabel = new JLabel(" ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(valueLabel, gbc);
        
        betRaiseButton = new JButton("Bet");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(betRaiseButton, gbc);
        
        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(cancelButton, gbc);
    }
    
    public void show(int minBet, int maxBet) {
//        this.minBet = minBet;
//        this.maxBet = maxBet;
        
        values.clear();
        int noOfValues = 0;
        int value = minBet;
        while (value < maxBet && noOfValues < (NO_OF_TICKS - 1)) {
            values.put(noOfValues, value);
            noOfValues++;
            value *= 2;
        }
        values.put(noOfValues, maxBet);
        slider.setMinimum(0);
        slider.setMaximum(noOfValues);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int index = slider.getValue();
        int amount = values.get(index);
        System.out.format("%d = $%d\n", index, amount);
        valueLabel.setText(String.format("Bet amount: $%d", amount));
    }
    
}
