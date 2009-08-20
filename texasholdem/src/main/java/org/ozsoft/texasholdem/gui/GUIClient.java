package org.ozsoft.texasholdem.gui;

import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.PlayerClient;

/**
 * Swing implementation of a player client for a human player.  
 * 
 * @author Oscar Stigter
 */
public class GUIClient implements PlayerClient {

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.texasholdem.PlayerClient#act(java.util.Set, org.ozsoft.texasholdem.Card[], java.util.List, int, int)
	 */
	@Override
	public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
		return null;
	}

}
