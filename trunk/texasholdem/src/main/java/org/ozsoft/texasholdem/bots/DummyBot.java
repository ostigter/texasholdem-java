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
 * Very simplistic implementation of a bot that always checks or calls (in that
 * order).
 * 
 * @author Oscar Stigter
 */
public class DummyBot extends Bot {

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
     * @see th.Client#joinedTable(int, java.util.List)
     */
    @Override
    public void joinedTable(int bigBlind, List<Player> players) {
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
        // Not implemented.
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
    public Action act(Set<Action> actions) {
        if (actions.contains(Action.CHECK)) {
            return Action.CHECK;
        } else {
            return Action.CALL;
        }
    }

}
