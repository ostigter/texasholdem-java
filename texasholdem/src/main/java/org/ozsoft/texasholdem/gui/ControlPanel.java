package org.ozsoft.texasholdem.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.PlayerClient;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * @author Oscar Stigter
 */
public class ControlPanel extends JPanel implements PlayerClient, ActionListener {
    
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Polling interval while waiting for input. */
	private static final long POLLING_INTERVAL = 100L;
	
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
     * Waits for the user to continue.
     */
    public void waitForUserInput() {
    	add(continueButton);
    	repaint();
    	getUserInput();
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.PlayerClient#act(java.util.Set, org.ozsoft.texasholdem.Card[], java.util.List, int, int)
     */
    @Override
	public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
    	// Show the buttons for the allowed actions.
        if (actions.contains(Action.CHECK)) {
        	add(checkButton);
        }
        if (actions.contains(Action.CALL)) {
        	add(callButton);
        }
        if (actions.contains(Action.BET)) {
        	add(betButton);
        }
        if (actions.contains(Action.RAISE)) {
        	add(raiseButton);
        }
        if (actions.contains(Action.FOLD)) {
        	add(foldButton);
        }
        invalidate();
        repaint();
        getUserInput();
        return action;
	}
    
    private void getUserInput() {
        action = null;
        while (action == null) {
        	try {
        		Thread.sleep(POLLING_INTERVAL);
        	} catch (InterruptedException e) {
        		// Ignore.
        	}
        }
        removeAll();
        repaint();
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
