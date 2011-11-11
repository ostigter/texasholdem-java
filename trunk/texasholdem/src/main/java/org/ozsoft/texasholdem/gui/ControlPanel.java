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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.ozsoft.texasholdem.Action;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * @author Oscar Stigter
 */
public class ControlPanel extends JPanel implements ActionListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Monitor while waiting for user input. */
    private final Object monitor = new Object();
    
    /** The Check button. */
    private final JButton checkButton;
    
    /** The Call button. */
    private final JButton callButton;
    
    /** The Bet button. */
    private final JButton betButton;
    
    /** The Raise button. */
    private final JButton raiseButton;
    
    /** The Fold button. */
    private final JButton foldButton;
    
    /** The Continue button. */
    private final JButton continueButton;

    /** The selected action. */
    private Action action;
    
    /**
     * Constructor.
     * 
     * @param main
     *            The main frame.
     */
    public ControlPanel() {
        setBackground(UIConstants.TABLE_COLOR);
        setLayout(new FlowLayout());
        continueButton = createActionButton(Action.CONTINUE);
        checkButton = createActionButton(Action.CHECK);
        callButton = createActionButton(Action.CALL);
        betButton = createActionButton(Action.BET);
        raiseButton = createActionButton(Action.RAISE);
        foldButton = createActionButton(Action.FOLD);
    }
    
    /**
     * Waits for the user to click the Continue button.
     */
    public void waitForUserInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(continueButton);
                validate();
                repaint();}
        });
        Set<Action> allowedActions = new HashSet<Action>();
        allowedActions.add(Action.CONTINUE);
        getUserInput(allowedActions);
    }
    
    /**
     * Waits for the user to click an action button and returns the selected
     * action.
     * 
     * @param allowedActions
     *            The allowed actions.
     * 
     * @return The selected action.
     */
    public Action getUserInput(final Set<Action> allowedActions) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Show the buttons for the allowed actions.
                removeAll();
                if (allowedActions.contains(Action.CONTINUE)) {
                    add(continueButton);
                } else {
                    if (allowedActions.contains(Action.CHECK)) {
                        add(checkButton);
                    }
                    if (allowedActions.contains(Action.CALL)) {
                        add(callButton);
                    }
                    if (allowedActions.contains(Action.BET)) {
                        add(betButton);
                    }
                    if (allowedActions.contains(Action.RAISE)) {
                        add(raiseButton);
                    }
                    if (allowedActions.contains(Action.FOLD)) {
                        add(foldButton);
                    }
                }
                validate();
                repaint();
            }
        });
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
        return action;
    }
    
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == continueButton) {
            action = Action.CONTINUE;
        } else if (source == checkButton) {
            action = Action.CHECK;
        } else if (source == callButton) {
            action = Action.CALL;
        } else if (source == betButton) {
            action = Action.BET;
        } else if (source == raiseButton) {
            action = Action.RAISE;
        } else {
            action = Action.FOLD;
        }
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }
    
    /**
     * Creates an action button.
     * 
     * @param action
     *            The action.
     * 
     * @return The button.
     */
    private JButton createActionButton(Action action) {
        String label = action.getName();
        JButton button = new JButton(label);
        button.setMnemonic(label.charAt(0));
        button.setSize(100, 30);
        button.addActionListener(this);
        return button;
    }

}
