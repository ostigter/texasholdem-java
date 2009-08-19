package org.ozsoft.texasholdem;

import java.util.List;
import java.util.Set;

/**
 * Client interface of a Texas Hold'em player.
 * 
 * Must be implemented by any client application acting as a player (human or bot).
 * 
 * @author Oscar Stigter
 */
public interface PlayerClient {

	/**
	 * Asks a player to select a poker action.
	 * 
	 * @param actions
	 *            The allowed actions to select from.
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
    Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet);

}
