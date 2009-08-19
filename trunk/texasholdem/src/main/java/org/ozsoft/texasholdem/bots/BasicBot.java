package org.ozsoft.texasholdem.bots;

import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;

/**
 * Basic Texas Hold'em poker bot.
 * 
 * @author Oscar Stigter
 */
public class BasicBot extends Bot {

	/*
	 * (non-Javadoc)
	 * @see th.PlayerClient#act(java.util.Set, th.Card[], java.util.List, int, int)
	 */
	@Override
	public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
		int holeValue = evaluateHoleCards(holeCards);
		if (actions.contains(Action.CHECK)) {
			return Action.CHECK;
		} else {
			return Action.CALL;
		}
	}
	
	public int evaluateHoleCards(Card[] cards) {
		if (cards.length != 2) {
			throw new IllegalArgumentException("Invalid number of cards: " + cards.length);
		}

		int rank1 = cards[0].getRank();
		int suit1 = cards[0].getSuit();
		int rank2 = cards[1].getRank();
		int suit2 = cards[1].getSuit();
		int highRank = Math.max(rank1, rank2);
		int lowRank = Math.min(rank1, rank2);
		boolean isSuited = (suit1 == suit2);
		boolean isPair = (rank1 == rank2);
		int distance = highRank - lowRank;
		boolean isSequential = (distance == 1);
		
		int value = highRank * Card.NO_OF_RANKS + lowRank;
		
		if (isPair) {
			value += Card.NO_OF_RANKS + 1;
		}
		
		return value;
	}

}
