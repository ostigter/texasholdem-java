package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ozsoft.texasholdem.Card;

/**
 * Board panel with the community cards and general information.
 *  
 * @author Oscar Stigter
 */
public class BoardPanel extends JPanel {
    
	/** The serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Label with the hand number. */
	private final JLabel handLabel;

	/** Label with the bet. */
	private final JLabel betLabel;

	/** Label with the pot. */
	private final JLabel potLabel;

	/** Labels with the community cards. */
    private final JLabel[] cardLabels;
    
    /** Label with a custom message. */
    private final JLabel messageLabel;
    
    /** The control panel. */
    private final ControlPanel controlPanel;
    
	/**
	 * Constructor.
	 * 
	 * @param mainFrame
	 *            The main frame.
	 */
    public BoardPanel(Main mainFrame) {
        setBorder(UIConstants.PANEL_BORDER);
        setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        JLabel label = new JLabel("Hand");
        label.setForeground(Color.GREEN);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 0, 5);
        add(label, gc);
        
        label = new JLabel("Bet");
        label.setForeground(Color.GREEN);
        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 0, 5);
        add(label, gc);
        
        label = new JLabel("Pot");
        label.setForeground(Color.GREEN);
        gc.gridx = 4;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 0, 5);
        add(label, gc);
        
        handLabel = new JLabel();
        handLabel.setBorder(UIConstants.LABEL_BORDER);
        handLabel.setForeground(Color.GREEN);
        handLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        add(handLabel, gc);

        betLabel = new JLabel();
        betLabel.setBorder(UIConstants.LABEL_BORDER);
        betLabel.setForeground(Color.GREEN);
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        add(betLabel, gc);

        potLabel = new JLabel();
        potLabel.setBorder(UIConstants.LABEL_BORDER);
        potLabel.setForeground(Color.GREEN);
        potLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 4;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        add(potLabel, gc);

        // The five card positions.
        cardLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            cardLabels[i] = new JLabel(ResourceManager.getIcon("/images/card_placeholder.png"));
            gc.gridx = i;
            gc.gridy = 2;
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gc.weightx = 0.0;
            gc.weighty = 0.0;
            gc.anchor = GridBagConstraints.CENTER;
            gc.fill = GridBagConstraints.NONE;
            gc.insets = new Insets(10, 5, 10, 5);
            add(cardLabels[i], gc);
        }
        
        // Message label.
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 5;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 0, 5, 0);
        add(messageLabel, gc);
        
        // Control panel.
        controlPanel = new ControlPanel(mainFrame);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 5;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(5, 0, 5, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        add(controlPanel, gc);
        
        update(1, 0, 0);
        setMessage("");
    }
    
	/**
	 * Updates the current hand status.
	 * 
	 * @param handNumber
	 *            The hand number.
	 * @param bet
	 *            The bet.
	 * @param pot
	 *            The pot.
	 */
    public void update(int handNumber, int bet, int pot) {
        handLabel.setText(String.valueOf(handNumber));
    	if (bet == 0) {
            betLabel.setText(" ");
    	} else {
            betLabel.setText("$ " + bet);
    	}
    	if (pot == 0) {
            potLabel.setText(" ");
    	} else {
            potLabel.setText("$ " + pot);
    	}
    }
    
	/**
	 * Sets the community cards.
	 * 
	 * @param cards
	 *            The community cards.
	 */
    public void setCards(Card[] cards, int noOfBoardCards) {
    	for (int i = 0; i < cards.length; i++) {
    		if (i < noOfBoardCards) {
    			cardLabels[i].setIcon(ResourceManager.getCardImage(cards[i]));
    		} else {
    			cardLabels[i].setIcon(ResourceManager.getIcon("/images/card_placeholder.png"));
    		}
    	}
    }
    
	/**
	 * Sets a custom message.
	 * 
	 * @param message
	 *            The message.
	 */
    public void setMessage(String message) {
        if (message.length() == 0) {
            messageLabel.setText(" ");
        } else {
            messageLabel.setText(message);
        }
    }
    
	/**
	 * Sets the allowed actions for the control panel.
	 * 
	 * @param actions
	 *            The allowed actions.
	 */
    public void setActions(final int actions) {
    	controlPanel.setActions(actions);
    }
    
	/**
	 * Returns the selected action from the control panel.
	 * 
	 * @return The selected action.
	 */
    public int getAction() {
    	return controlPanel.getAction();
    }
    
}
