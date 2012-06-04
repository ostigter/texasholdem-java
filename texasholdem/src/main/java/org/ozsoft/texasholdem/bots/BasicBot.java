// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.texasholdem.bots;

import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;

/**
 * Basic Texas Hold'em poker bot.
 * 
 * Currently plays solely based on its hole cards.
 * 
 * TODO: 
 * - hole cards (Chen formula)
 * - board cards
 * - bet
 * - pot
 * - tightness (tight/loose)
 * - aggression (careful/aggressive)
 * - bluffing
 *
 * @author Oscar Stigter
 */
public class BasicBot extends Bot {
    
//    /** The hole cards. */
//    private Card[] cards;

    /*
     * (non-Javadoc)
     * @see th.Client#joinedTable(int, java.util.List)
     */
    @Override
    public void joinedTable(int bigBlind, List<Player> players) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see th.Client#messageReceived(java.lang.String)
     */
    @Override
    public void messageReceived(String message) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see th.Client#handStarted(th.Player)
     */
    @Override
    public void handStarted(Player dealer) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see th.Client#actorRotated(th.Player)
     */
    @Override
    public void actorRotated(Player actor) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see th.Client#boardUpdated(java.util.List, int, int)
     */
    @Override
    public void boardUpdated(List<Card> cards, int bet, int pot) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#playerUpdated(org.ozsoft.texasholdem.Player)
     */
    @Override
    public void playerUpdated(Player player) {
//        if (player.getCards().length > 0) {
//            this.cards = player.getCards();
//        }
    }

    /*
     * (non-Javadoc)
     * @see th.Client#playerActed(th.Player)
     */
    @Override
    public void playerActed(Player player) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see th.Client#act(java.util.Set)
     */
    @Override
    public Action act(Set<Action> allowedActions) {
        if (allowedActions.contains(Action.CHECK)) {
            return Action.CHECK;
        } else {
            return Action.CALL;
        }
    }
    
//    /**
//     * Evaluates the hole cards using the Chen formula and returns the value.
//     * 
//     * @return The value of the hole cards.
//     */
//    private int getChenValue() {
//        if (cards.length != 2) {
//            throw new IllegalArgumentException("Invalid number of cards: " + cards.length);
//        }
//
//        int rank1 = cards[0].getRank();
//        int suit1 = cards[0].getSuit();
//        int rank2 = cards[1].getRank();
//        int suit2 = cards[1].getSuit();
//        int highRank = Math.max(rank1, rank2);
//        int lowRank = Math.min(rank1, rank2);
//        boolean isSuited = (suit1 == suit2);
//        boolean isPair = (rank1 == rank2);
//        int distance = highRank - lowRank;
//        boolean isSequential = (distance == 1);
//        
//        int value = highRank * Card.NO_OF_RANKS + lowRank;
//        
//        if (isPair) {
//            value += Card.NO_OF_RANKS + 1;
//        }
//        
//        return value;
//    }

}
