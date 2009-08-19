package org.ozsoft.texasholdem;

import java.util.List;

/**
 * A listener of a poker game.
 * 
 * @author Oscar Stigter
 */
public interface GameListener {
	
	/**
	 * Handles a game message.
	 * 
	 * @param message
	 *            The message.
	 */
	void messageReceived(String message);
	
	/**
	 * Handles an update of the board.
	 * 
	 * @param hand
	 *            The number of hands played.
	 * @param cards
	 *            The community cards on the board.
	 * @param bet
	 *            The bet.
	 * @param pot
	 *            The pot.
	 */
	void boardUpdated(int hand, List<Card> cards, int bet, int pot);
	
	/**
	 * Handles the event of a player acting.
	 * 
	 * @param playerInfo
	 *            The player's information.
	 */
	void playerActed(PlayerInfo playerInfo);

}
