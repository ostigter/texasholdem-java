package org.ozsoft.texasholdem;

/**
 * The public information of a player at the table.
 * 
 * Contains only information that can be seen by all players, so this object can
 * be safely distributed.
 * 
 * Implemented as a immutable class.
 * 
 * @author Oscar Stigter
 */
public class PlayerInfo {
	
	/** The name. */
	private final String name;
	
	/** The current cash. */
	private final int cash;
	
	/** The current bet. */
	private final int bet;
	
	/** The last action. */
	private final Action action;
	
	/** The hole cards. */
	private final Card[] cards;
	
	/**
	 * Constructor based on a player.
	 * 
	 * @param player
	 *            The player.
	 * @param showCards
	 *            Whether to show the player's cards.
	 */
	public PlayerInfo(Player player, boolean showCards) {
		name = player.getName();
		cash = player.getCash();
		bet = player.getBet();
		action = player.getAction();
		if (showCards) {
			cards = player.getCards();
		} else {
			cards = null;
		}
	}
	
	/**
	 * Returns the player's name.
	 * 
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the current amount of cash.
	 * 
	 * @return The current amount of cash.
	 */
	public int getCash() {
		return cash;
	}
	
	/**
	 * Returns the current bet.
	 * 
	 * @return The current bet.
	 */
	public int getBet() {
		return bet;
	}
	
	/**
	 * Returns the last performed action.
	 * 
	 * @return The last performed action.
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Returns the hole cards.
	 * 
	 * @return The hold cards, or null if not visible.
	 */
	public Card[] getCards() {
		return cards;
	}

}
