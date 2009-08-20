package org.ozsoft.texasholdem.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.GameEngine;
import org.ozsoft.texasholdem.GameListener;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.PlayerInfo;
import org.ozsoft.texasholdem.bots.DummyBot;

/**
 * The game's main frame.
 * 
 * @author Oscar Stigter
 */
public class Main extends JFrame implements GameListener {
    
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
//	/** The number of players at the table. */
//	private static final int NO_OF_PLAYERS = 4;
	
    /** The starting cash per player. */
    private static final int STARTING_CASH = 100;
    
    /** The size of the big blind. */
    private static final int BIG_BLIND = 2;
    
    /** The players at the table. */
    private final List<Player> players;
    
    /** The GridBagConstraints. */
    private GridBagConstraints gc;
    
    /** The board panel. */
    private BoardPanel boardPanel;
    
    /** The player panels. */
    private Map<String, PlayerPanel> playerPanels;

    /**
     * Constructor.
     */
    public Main() {
        super("Limit Texas Hold'em poker");
        
        players = new ArrayList<Player>();
        players.add(new Player("Player", new ControlPanel(), STARTING_CASH));
        players.add(new Player("Joe",    new DummyBot(),     STARTING_CASH));
        players.add(new Player("Mike",   new DummyBot(),     STARTING_CASH));
        players.add(new Player("Eddie",  new DummyBot(),     STARTING_CASH));
        
        createUI();
        
        GameEngine gameEngine = new GameEngine(BIG_BLIND, players);
        gameEngine.addListener(this);
        gameEngine.start();
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

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.texasholdem.GameListener#boardUpdated(int, java.util.List, int, int)
	 */
	@Override
	public void boardUpdated(int hand, List<Card> cards, int bet, int pot) {
		boardPanel.update(hand, cards, bet, pot);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.texasholdem.GameListener#messageReceived(java.lang.String)
	 */
	@Override
	public void messageReceived(String message) {
		boardPanel.setMessage(message);
		boardPanel.waitForUserInput();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.texasholdem.GameListener#playerActed(org.ozsoft.texasholdem.PlayerInfo)
	 */
	@Override
	public void playerActed(PlayerInfo playerInfo) {
		String name = playerInfo.getName();
		PlayerPanel playerPanel = playerPanels.get(name);
		if (playerPanel != null) {
			playerPanel.update(playerInfo);
			boardPanel.setMessage(String.format("%s %s.", name, playerInfo.getAction().getVerb()));
			boardPanel.waitForUserInput();
		} else {
			throw new IllegalStateException(
					String.format("No PlayerPanel found for player '%s'", name));
		}
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
        addComponent(boardPanel, 1, 1, 1, 1);
        
        playerPanels = new HashMap<String, PlayerPanel>();
        for (int i = 0; i < players.size(); i++) {
        	PlayerPanel panel = new PlayerPanel();
        	playerPanels.put(players.get(i).getName(), panel);
        	switch (i) {
        		case 0:
        			addComponent(panel, 1, 0, 1, 1);
        			break;
        		case 1:
        			addComponent(panel, 2, 1, 1, 1);
        			break;
        		case 2:
        			addComponent(panel, 1, 2, 1, 1);
        			break;
        		case 3:
        			addComponent(panel, 0, 1, 1, 1);
        			break;
        		default:
        			// Do nothing.
        	}
        }
        
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

}
