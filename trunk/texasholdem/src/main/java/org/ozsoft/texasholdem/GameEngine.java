package org.ozsoft.texasholdem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Limit Texas Hold'em poker engine, responsible for the game flow.
 * 
 * @author Oscar Stigter
 */
public class GameEngine {
	
    /** The maximum number of bets or raises in a single hand per player. */
    private static final int MAX_RAISES = 4;
    
    /** The size of the big blind. */
    private final int bigBlind;
	
    /** The players at the table. */
    private final List<Player> players;
	
    /** The active players in the current hand. */
    private final List<Player> activePlayers;
	
    /** The deck of cards. */
    private final Deck deck;
	
	/** The community cards on the board. */
    private final List<Card> board;
	
    /** The listeners to this game. */
    private final Set<GameListener> listeners;
	
	/** The number of hands played. */
    private int hand;
	
    /** The current dealer position. */
    private int dealerPosition;

    /** The current dealer. */
    private Player dealer;

    /** The position of the acting player. */
    private int actorPosition;
    
    /** The acting player. */
    private Player actor;

    /** The minimum bet in the current hand. */
    private int minBet;
	
    /** The bet in the current hand. */
    private int bet;
	
    /** The pot in the current hand. */
    private int pot;
	
    /** Whether the game is over. */
    private boolean gameOver;
	
    /**
     * Constructor.
     * 
     * @param bigBlind The size of the big blind.
     * @param players The players at the table.
     */
    public GameEngine(int bigBlind, List<Player> players) {
		this.bigBlind = bigBlind;
		this.players = players;
		activePlayers = new ArrayList<Player>();
		deck = new Deck();
		board = new ArrayList<Card>();
		listeners = new HashSet<GameListener>();
	}
	
	/**
	 * Adds a game listener.
	 * 
	 * @param listener
	 *            The game listener.
	 */
    public void addListener(GameListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a game listener.
	 * 
	 * @param listener
	 *            The game listener.
	 */
    public void removeListener(GameListener listener) {
		listeners.remove(listener);
	}
	
    /**
     * Main game loop.
     */
    public void start() {
		resetGame();
		while (!gameOver) {
			playHand();
		}
		notifyMessage("Game over.");
	}
	
    /**
     * Resets the game.
     */
    private void resetGame() {
//		notifyMessage("New game.");
		hand = 0;
    	dealerPosition = -1;
    	actorPosition = -1;
		gameOver = false;
		for (Player player : players) {
			notifyPlayerActed(player);
		}
	}
	
    /**
     * Plays a single hand.
     */
    private void playHand() {
		resetHand();
		postSmallBlind();
		postBigBlind();
        bet = bigBlind;
		// The Pre-Flop.
		dealHoleCards();
		doBettingRound();
		if (activePlayers.size() > 1) {
			// The Flop.
			dealCommunityCards("Flop", 3);
			doBettingRound();
			if (activePlayers.size() > 1) {
				// The Turn.
    			dealCommunityCards("Turn", 1);
    			doBettingRound();
    			if (activePlayers.size() > 1) {
    				// The River.
        			dealCommunityCards("River", 1);
        	        bet = 2 * bigBlind;
        			doBettingRound();
        			if (activePlayers.size() > 1) {
        				// The Showdown.
        				doShowdown();
        			}
    			}
			}
		}
	}
	
	/**
	 * Resets the game for a new hand.
	 */
    private void resetHand() {
		hand++;
//		notifyMessage("New hand.");
		board.clear();
		for (Player player : players) {
			player.resetHand();
		}
		activePlayers.clear();
		for (Player player : players) {
			if (!player.isBroke()) {
				activePlayers.add(player);
			}
		}
		rotateDealer();
//		notifyMessage("%s shuffles the deck.", dealer);
		deck.shuffle();
		actorPosition = dealerPosition;
		minBet = bigBlind;
	}

    /**
     * Rotates the dealer position.
     */
    private void rotateDealer() {
        dealerPosition = (dealerPosition + 1) % players.size();
        dealer = players.get(dealerPosition);
    	for (GameListener listener : listeners) {
    		listener.dealerRotated(dealer.getName());
    	}
    	notifyMessage("%s is the dealer.", dealer);
    }

    /**
     * Rotates the position of the player in turn (the actor).
     */
    private void rotateActor() {
    	if (activePlayers.size() > 0) {
        	do {
        		actorPosition = (actorPosition + 1) % players.size();
        		actor = players.get(actorPosition);
        	} while (!activePlayers.contains(actor));
        	for (GameListener listener : listeners) {
        		listener.actorRotated(actor.getName());
        	}
    	} else {
    		// Should never happen.
    		throw new IllegalStateException("No active players left");
    	}
    }
    
    /**
     * Posts the small blind.
     */
    private void postSmallBlind() {
        rotateActor();
		int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        pot += smallBlind;
        notifyPlayerActed(actor);
	}
	
    /**
     * Posts the big blind.
     */
    private void postBigBlind() {
        rotateActor();
        actor.postBigBlind(bigBlind);
        pot += bigBlind;
        notifyPlayerActed(actor);
	}
	
    /**
     * Deals the Hole Cards.
     */
    private void dealHoleCards() {
        for (Player player : players) {
            player.setCards(deck.deal(2));
            notifyPlayerUpdated(player, true);
        }
        notifyMessage("%s deals the Hole Cards.", dealer);
	}
	
	/**
	 * Deals a number of community cards.
	 * 
	 * @param name
	 *            The name of the phase.
	 * @param noOfCards
	 *            The number of cards to deal.
	 */
    private void dealCommunityCards(String name, int noOfCards) {
        notifyMessage("%s deals the %s.", dealer, name);
        for (int i = 0; i < noOfCards; i++) {
        	board.add(deck.deal());
        }
//        notifyMessage("Board: %s", board);
	}
	
    /**
     * Performs a betting round.
     */
    private void doBettingRound() {
    	// Determine the number of active players.
		int playersToAct = activePlayers.size();
		// Determine the initial player and bet size.
		if (board.size() == 0) {
			// Pre-Flop; player left of big blind starts, bet is the big blind.
			bet = bigBlind;
		} else {
			// Otherwise, player left of dealer starts, no initial bet.
			actorPosition = dealerPosition;
			bet = 0;
		}
		while (playersToAct > 0) {
//        	notifyMessage("Hand: %d, MinBet: %d, Bet: %d, Pot: %d", hand, minBet, bet, pot);
        	rotateActor();
        	notifyMessage("It's %s's turn to act.", actor);
        	int smallBlind = bigBlind / 2;
        	boolean isSmallBlindPosition = (actor.getBet() == smallBlind);
        	Set<Action> allowedActions = getAllowedActions(actor);
        	Action action = actor.act(allowedActions, actor.getCards(), board, minBet, bet);
        	if (!allowedActions.contains(action)) {
        		String msg = String.format("Illegal action (%s) from player %s!", action, actor);
        		throw new IllegalStateException(msg);
        	}
        	playersToAct--;
        	switch (action) {
        		case CHECK:
        			// Do nothing.
        			break;
        		case CALL:
        			if (isSmallBlindPosition) {
            			// Correct bet for small blind.
        				bet -= smallBlind;
        			}
    				pot += bet;
        			break;
        		case BET:
        			bet = minBet;
        			pot += bet;
        			playersToAct = activePlayers.size();
        			break;
        		case RAISE:
        			bet += minBet;
        			pot += bet;
        			if (actor.getRaises() == MAX_RAISES) {
        				// Max. number of raises reached; other players get one more turn.
            			playersToAct = activePlayers.size() - 1;
        			} else {
        				// Otherwise, all players get another turn.
            			playersToAct = activePlayers.size();
        			}
        			break;
        		case FOLD:
            		actor.setCards(null);
            		activePlayers.remove(actor);
            		if (activePlayers.size() == 1) {
            			// The player left wins.
            			playerWins(activePlayers.get(0));
            			playersToAct = 0;
            		}
            		break;
        		default:
        			throw new IllegalStateException("Invalid action: " + action);
        	}
    		notifyPlayerActed(actor);
		}
		for (Player player : players) {
			player.resetBet();
		}
	}
	
	/**
	 * Returns the allowed actions of a specific player.
	 * 
	 * @param player
	 *            The player.
	 * 
	 * @return The allowed actions.
	 */
    private Set<Action> getAllowedActions(Player player) {
		int actorBet = actor.getBet();
    	Set<Action> actions = new HashSet<Action>();
    	if (bet == 0) {
    		actions.add(Action.CHECK);
    		if (player.getRaises() < MAX_RAISES) {
    			actions.add(Action.BET);
    		}
    	} else {
        	if (actorBet < bet) {
        		actions.add(Action.CALL);
        		if (player.getRaises() < MAX_RAISES) {
        			actions.add(Action.RAISE);
        		}
        	} else {
        		actions.add(Action.CHECK);
        		if (player.getRaises() < MAX_RAISES) {
        			actions.add(Action.RAISE);
        		}
        	}
    	}
    	actions.add(Action.FOLD);
    	return actions;
	}
	
    /**
     * Performs the Showdown.
     */
    private void doShowdown() {
		notifyMessage("Showdown!");
//		notifyMessage("The board: %s", new Hand(board));
		notifyBoardUpdated();
		int highestValue = 0;
		List<Player> winners = new ArrayList<Player>();
		for (Player player : activePlayers) {
			// Create a hand with the community cards and the player's hole cards.
			Hand playerHand = new Hand(board);
			playerHand.addCards(player.getCards());
			// Evaluate the combined hand.
			HandEvaluator evaluator = new HandEvaluator(playerHand);
			int value = evaluator.getValue();
//			String description = evaluator.getType().getDescription();
//			notifyMessage("%s's cards:  %s\t(%s, %d)", player, player.getHand(), description, value);
			// Look for one or more winners.
			if (value > highestValue) {
				// New winner.
				highestValue = value;
				winners.clear();
				winners.add(player);
			} else if (value == highestValue) {
				// Tie winner.
				winners.add(player);
			} else {
				// Loser.
			}
		}
		if (winners.size() == 1) {
			// Single winner.
			playerWins(winners.get(0));
		} else {
			// Tie; multiple winners.
			//TODO: Handle tie
			notifyMessage("A tie! X and Y share the pot.");
		}
	}
	
	/**
	 * Let's a player win the pot.
	 * 
	 * @param player
	 *            The winning player.
	 */
    private void playerWins(Player player) {
		notifyMessage("%s wins.", player);
		player.win(pot);
		pot = 0;
	}
	
	/**
	 * Notifies listeners with a custom game message.
	 * 
	 * @param message
	 *            The formatted message.
	 * @param args
	 *            Any arguments.
	 */
    private void notifyMessage(String message, Object... args) {
    	message = String.format(message, args);
    	for (GameListener listener : listeners) {
    		listener.messageReceived(message);
    	}
    }
    
    /**
     * Notifies listeners that the board has been updated.
     */
    private void notifyBoardUpdated() {
    	for (GameListener listener : listeners) {
    		listener.boardUpdated(hand, board, bet, pot);
    	}
    }
    
    private void notifyPlayerUpdated(Player player, boolean showCards) {
    	for (GameListener listener : listeners) {
    		listener.playerActed(new PlayerInfo(player, showCards));
    	}
    }

	/**
	 * Notifies listeners that a player has acted.
	 * 
	 * @param player
	 *            The player that has acted.
	 */
    private void notifyPlayerActed(Player player) {
    	for (GameListener listener : listeners) {
    		listener.playerActed(new PlayerInfo(player, false));
    	}
    }
    
}
