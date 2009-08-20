package org.ozsoft.texasholdem;

import java.util.List;
import java.util.Set;

/**
 * A Texas Hold'em player.
 * 
 * The player's actions are delegated to a PlayerClient, which can be either
 * human controlled or computer controlled.
 * 
 * @author Oscar Stigter
 */
public class Player {
    
	/** The name. */
	private final String name;
	
	/** The client responsible for the actual behavior. */
	private final PlayerClient client;
	
    /** The hand of cards. */
    private final Hand hand;

	/** Current amount of cash. */
    private int cash;
    
    /** The current bet. */
    private int bet;
    
    /** The number of bets and raises in the current betting round. */
    private int raises;
    
    /** The last action performed. */
    private Action action;

    /** Whether the player has gone all-in. */
    private boolean allIn;
    
	/**
	 * Constructs a player.
	 * 
	 * @param name
	 *            The player's name.
	 * @param client
	 *            The client interface.
	 * @param cash
	 *            The player's starting amount of cash.
	 */
    public Player(String name, PlayerClient client, int cash) {
        this.name = name;
        this.client = client;
        this.cash = cash;

        hand = new Hand();

        resetHand();
    }
    
    /**
     * Prepares the player for another hand.
     */
    public void resetHand() {
        action = null;
    	resetBet();
    }
    
    /**
     * Prepares the player for another betting round.
     */
    public void resetBet() {
        bet = 0;
        raises = 0;
        allIn = false;
    }
    
    /**
     * Sets the hole cards.
     */
    public void setCards(List<Card> cards) {
        hand.removeAllCards();
    	if (cards != null) {
    		if (cards.size() == 2) {
    	        hand.addCards(cards);
//    	        System.out.format("%s's cards: %s\n", name, hand);
    		} else {
    			throw new IllegalArgumentException("Invalid number of cards");
    		}
    	}
    }
    
    /**
     * Returns the player's name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns whether the player is broke.
     *
     * @return True if the player is broke, otherwise false.
     */
    public boolean isBroke() {
        return (cash == 0);
    }
    
    /**
     * Returns the player's current amount of cash.
     *
     * @return The amount of cash.
     */
    public int getCash() {
        return cash;
    }
    
    /**
     * Returns the player's current bet.
     *
     * @return The current bet.
     */
    public int getBet() {
        return bet;
    }
    
    /**
	 * Returns the number of raises the player has done in this betting round.
	 * 
	 * @return The number of raises.
	 */
    public int getRaises() {
    	return raises;
    }
    
    /**
     * Returns whether the player has gone all-in.
     *
     * @return True if all-in, otherwise false.
     */
    public boolean isAllIn() {
        return allIn;
    }
    
    /**
     * Returns the player's action.
     *
     * @return  the action
     */
    public Action getAction() {
        return action;
    }
    
    /**
     * Returns the player's hand of cards.
     *
     * @return The hand of cards.
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * Returns the player's hole cards.
     *
     * @return The hole cards.
     */
    public Card[] getCards() {
        return hand.getCards();
    }
    
	/**
	 * Posts the small blind.
	 * 
	 * @param blind
	 *            The small blind.
	 */
    public void postSmallBlind(int blind) {
        action = Action.SMALL_BLIND;
        cash -= blind;
        bet += blind;
    }
    
	/**
	 * Posts the big blinds.
	 * 
	 * @param blind
	 *            The big blind.
	 */
    public void postBigBlind(int blind) {
        action = Action.BIG_BLIND;
        cash -= blind;
        bet += blind;
    }
    
	/**
	 * Asks the player to act and returns the selected action.
	 * 
	 * @param actions
	 *            The allowed actions.
	 * @param holeCards
	 *            The player's hole cards.
	 * @param boardCards
	 *            The community cards on the board.
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 * 
	 * @return The selected action.
	 */
    public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
    	action = client.act(actions, holeCards, boardCards, minBet, currentBet);
    	switch (action) {
        	case CHECK:
        		break;
        	case CALL:
                cash -= currentBet - bet;
                bet += currentBet - bet;
        		break;
        	case BET:
                if (minBet >= cash) {
                    minBet = cash;
                    allIn = true;
                }
                cash -= minBet;
                bet += minBet;
                raises++;
        		break;
        	case RAISE:
                currentBet += minBet;
                cash -= currentBet - bet;
                bet += currentBet - bet;
                raises++;
        		break;
        	case FOLD:
                hand.removeAllCards();
        		break;
    	}
    	return action;
    }
    
	/**
	 * Wins the pot.
	 * 
	 * @param pot
	 *            The pot.
	 */
    public void win(int pot) {
        cash += pot;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return name;
    }
    
}
