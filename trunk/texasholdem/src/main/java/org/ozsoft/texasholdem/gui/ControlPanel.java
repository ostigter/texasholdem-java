package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel with buttons to let a human player select a poker action.
 * 
 * @author Oscar Stigter
 */
public class ControlPanel extends JPanel implements ActionListener {
    
	private static final long serialVersionUID = 1L;
	
	// The actions.
	public static final int NONE             = 0;
	public static final int CONTINUE         = 1;
	public static final int CHECK            = 2;
	public static final int CALL             = 3;
	public static final int BET              = 4;
	public static final int RAISE            = 5;
	public static final int FOLD             = 6;
	public static final int CHECK_BET_FOLD   = 7;
	public static final int CALL_RAISE_FOLD  = 8;
	public static final int CALL_FOLD        = 9;
	public static final int CHECK_RAISE_FOLD = 10;
    
    private final Main main;
    
    private final JButton continueButton;
    private final JButton checkButton;
    private final JButton callButton;
    private final JButton betButton;
    private final JButton raiseButton;
    private final JButton foldButton;
    
    /** The selected action. */
    private int action;
    
	/**
	 * Constructor.
	 * 
	 * @param main
	 *            The main frame.
	 */
    public ControlPanel(Main main) {
    	super();
        this.main = main;
        setBackground(new Color(0, 128, 0));
        setLayout(new FlowLayout());
        continueButton = createButton("Continue", 'c');
        checkButton = createButton("Check", 'c');
        callButton = createButton("Call", 'c');
        betButton = createButton("Bet", 'b');
        raiseButton = createButton("Raise", 'r');
        foldButton = createButton("Fold", 'f');
    }
    
	/**
	 * Sets the allowed actions.
	 * 
	 * @param actions
	 *            The allowed actions.
	 */
    public void setActions(final int actions) {
        removeAll();
        switch (actions) {
        	case NONE:
        		// Hide all buttons.
        		break;
            case CONTINUE:
            	// Continue button only.
                add(continueButton);
                break;
            case CHECK_BET_FOLD:
            	// Check, Bet or Fold.
                add(checkButton);
                add(betButton);
                add(foldButton);
                break;
            case CALL_RAISE_FOLD:
            	// Call, Raise or Fold.
                add(callButton);
                add(raiseButton);
                add(foldButton);
                break;
            case CALL_FOLD:
            	// Call or Fold.
                add(callButton);
                add(foldButton);
                break;
            case CHECK_RAISE_FOLD:
            	// Check, Raise or Fold.
                add(checkButton);
                add(raiseButton);
                add(foldButton);
                break;
            default:
            	// Should never happen.
                System.err.println("ERROR: Invalid selected action: " + actions);
        }
        repaint();
    }
    
	/**
	 * Returns the selected action.
	 * 
	 * @return The selected action.
	 */
    public int getAction() {
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
    		action = CONTINUE;
    	} else if (source == checkButton) {
    		action = CHECK;
    	} else if (source == callButton) {
    		action = CALL;
    	} else if (source == betButton) {
    		action = BET;
    	} else if (source == raiseButton) {
    		action = RAISE;
    	} else {
    		action = FOLD;
    	}
	}
    
	/**
	 * Creates a button.
	 * 
	 * @param label
	 *            The label text.
	 * @param mnemonic
	 *            The mnemonic character.
	 * 
	 * @return The button.
	 */
    private JButton createButton(String label, char mnemonic) {
    	JButton button = new JButton(label);
    	button.setMnemonic(mnemonic);
    	button.setSize(100, 30);
    	button.addActionListener(this);
    	return button;
    }
    
}
