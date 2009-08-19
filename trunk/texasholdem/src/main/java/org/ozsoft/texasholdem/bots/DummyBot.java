package org.ozsoft.texasholdem.bots;

import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;

/**
 * Very simplistic implementation of a bot that always checks or calls (in that
 * order).
 * 
 * @author Oscar Stigter
 */
public class DummyBot extends Bot {

	/*
	 * (non-Javadoc)
	 * @see th.PlayerClient#act(java.util.Set, th.Card[], java.util.List, int, int)
	 */
	@Override
	public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
		if (actions.contains(Action.CHECK)) {
			return Action.CHECK;
		} else {
			return Action.CALL;
		}
	}
	
}
