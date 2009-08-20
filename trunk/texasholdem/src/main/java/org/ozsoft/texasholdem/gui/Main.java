package org.ozsoft.texasholdem.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

/**
 * The game's main frame.
 * 
 * @author Oscar Stigter
 */
public class Main extends JFrame {
    
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The number of players at the table. */
	private static final int NO_OF_PLAYERS = 4;
	
//    /** The starting cash per player. */
//    private static final int STARTING_CASH = 100;
//    
//    /** The size of the big blind. */
//    private static final int BIG_BLIND = 2;
//    
//    /** The polling delay in ms when waiting for a player to act. */
//    private static final long POLLING_DELAY = 100L;
    
    /** The GridBagConstraints. */
    private GridBagConstraints gc;
    
    /** The board panel. */
    private BoardPanel boardPanel;
    
    /** The player panels. */
    private PlayerPanel[] playerPanels;

    /**
     * Constructor.
     */
    public Main() {
        super("Limit Texas Hold'em poker");
        
        createUI();
        
//        Player[] players = new Player[] {
//            new Player("Player", new GUIClient(), STARTING_CASH),
//            new Player("Joe",    new DummyBot(),  STARTING_CASH),
//            new Player("Mike",   new DummyBot(),  STARTING_CASH),
//            new Player("Eddy",   new DummyBot(),  STARTING_CASH),
//        };
     }
    
	/**
	 * The application's entry point.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		new Main();
	}

	/**
	 * Creates the UI.
	 */
	private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());

        gc = new GridBagConstraints();
        
        boardPanel = new BoardPanel(this);        
        
        playerPanels = new PlayerPanel[NO_OF_PLAYERS];
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            playerPanels[i] = new PlayerPanel();
        }
        
        // 4 player table.
        addComponent(boardPanel,      1, 1, 1, 1);
        addComponent(playerPanels[0], 1, 0, 1, 1);
        addComponent(playerPanels[1], 2, 1, 1, 1);
        addComponent(playerPanels[2], 1, 2, 1, 1);
        addComponent(playerPanels[3], 0, 1, 1, 1);
        
//        // 10 player table.
//        addComponent(boardPanel,      1, 1, 3, 3);
//        addComponent(playerPanels[0], 1, 0, 1, 1);
//        addComponent(playerPanels[1], 2, 0, 1, 1);
//        addComponent(playerPanels[2], 3, 0, 1, 1);
//        addComponent(playerPanels[3], 4, 1, 1, 1);
//        addComponent(playerPanels[4], 4, 3, 1, 1);
//        addComponent(playerPanels[5], 3, 4, 1, 1);
//        addComponent(playerPanels[6], 2, 4, 1, 1);
//        addComponent(playerPanels[7], 1, 4, 1, 1);
//        addComponent(playerPanels[8], 0, 3, 1, 1);
//        addComponent(playerPanels[9], 0, 1, 1, 1);

//      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//      setSize(dimension.width, dimension.height - 30);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
   }
    
	/**
	 * Adds an UI component.
	 * 
	 * @param component
	 *            The component.
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param width
	 *            The number of columns to span.
	 * @param height
	 *            The number of rows to span.
	 */
    private void addComponent(Component component, int x, int y, int width, int height) {
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        getContentPane().add(component, gc);
    }
    
//	/**
//	 * Asks the human player to select an action, and returns the action.
//	 * 
//	 * @param actions
//	 *            The allowed actions.
//	 * 
//	 * @return The selected action.
//	 */
//    private int getInput(final int actions) {
//        boardPanel.setActions(actions);
//        waitingForPlayer = true;
//        try {
//            while (waitingForPlayer) {
//                Thread.sleep(POLLING_DELAY);
//            }
//        } catch (InterruptedException e) {
//            // Ignore.
//        }
//        return boardPanel.getAction();
//    }
    
}
