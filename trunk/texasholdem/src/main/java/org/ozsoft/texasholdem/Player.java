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
	private final Client client;
	
    /** The hand of cards. */
    private final Hand hand;
    
	/** Current amount of cash. */
    private int cash;
    
    /** Whether the player has hole cards. */
    private boolean hasCards;

    /** The current bet. */
    private int bet;
    
    /** The number of bets and raises in the current betting round. */
    private int raises;
    
    /** The last action performed. */
    private Action action;

	/**
	 * Constructs a player.
	 * 
	 * @param name
	 *            The player's name.
	 * @param cash
	 *            The player's starting amount of cash.
	 * @param client
	 *            The client interface.
	 */
    public Player(String name, int cash, Client client) {
        this.name = name;
        this.cash = cash;
        this.client = client;

        hand = new Hand();

        resetHand();
    }
    
	/**
	 * Returns the client.
	 * 
	 * @return The client.
	 */
    public Client getClient() {
    	return client;
    }
    
    /**
     * Prepares the player for another hand.
     */
    public void resetHand() {
    	hand.removeAllCards();
    	hasCards = false;
    	resetBet();
    }
    
    public void resetBet() {
        bet = 0;
        action = null;
        raises = 0;
    }
    
    /**
     * Sets the hole cards.
     */
    public void setCards(List<Card> cards) {
        hand.removeAllCards();
    	if (cards == null) {
    		hasCards = false;
    	} else {
    		if (cards.size() == 2) {
    	        hand.addCards(cards);
    	        hasCards = true;
    	        System.out.format("[CHEAT] %s's cards:\t%s\n", name, hand);
    		} else {
    			throw new IllegalArgumentException("Invalid number of cards");
    		}
    	}
    }
    
    public boolean hasCards() {
    	return hasCards;
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
     * Returns the player's current amount of cash.
     *
     * @return The amount of cash.
     */
    public int getCash() {
        return cash;
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
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 * 
	 * @return The selected action.
	 */
    public Action act(Set<Action> actions, int minBet, int currentBet) {
    	action = client.act(actions);
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
    
	/**
	 * Returns a clone of this player with only public information.
	 * 
	 * @return The cloned player.
	 */
    public Player publicClone() {
    	Player clone = new Player(name, cash, null);
    	clone.bet = bet;
    	clone.raises = raises;
    	clone.action = action;
    	return clone;
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
