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

package org.ozsoft.texasholdem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.bots.DummyBot;

/**
 * Console version of a Texas Hold'em client.
 * 
 * @author Oscar Stigter
 */
public class ConsoleClient implements Client {
    
    /** The size of the big blind. */
    private static final int BIG_BLIND = 2;

    /** The amount of starting cash per player. */
    private static final int STARTING_CASH = 100;
    
    /** The console reader. */
    private final BufferedReader consoleReader;
    
    /**
     * Constructor.
     */
    public ConsoleClient() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        Table table = new Table(BIG_BLIND);
        table.addPlayer(new Player("Player", STARTING_CASH, this));
        table.addPlayer(new Player("Joe",    STARTING_CASH, new DummyBot()));
        table.addPlayer(new Player("Mike",   STARTING_CASH, new DummyBot()));
        table.addPlayer(new Player("Eddie",  STARTING_CASH, new DummyBot()));
        table.start();
    }

    /**
     * Application's entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new ConsoleClient();
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#messageReceived(java.lang.String)
     */
    @Override
    public void messageReceived(String message) {
        System.out.println(message);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#joinedTable(int, java.util.List)
     */
    @Override
    public void joinedTable(int bigBlind, List<Player> players) {
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#handStarted(org.ozsoft.texasholdem.Player)
     */
    @Override
    public void handStarted(Player dealer) {
        System.out.format("New hand, %s is the dealer.\n", dealer);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#actorRotated(org.ozsoft.texasholdem.Player)
     */
    @Override
    public void actorRotated(Player actor) {
        System.out.format("It's %s's turn to act.\n", actor);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#boardUpdated(java.util.List, int, int)
     */
    @Override
    public void boardUpdated(List<Card> cards, int bet, int pot) {
        System.out.format("Board: %s, Bet: %d, Pot: %d\n", cards, bet, pot);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#playerUpdated(org.ozsoft.texasholdem.Player)
     */
    @Override
    public void playerUpdated(Player player) {
//		Card[] cards = player.getCards();
//		if (cards.length == 2) {
//			System.out.format("Hole cards: %s %s\n", cards[0], cards[1]);
//		}
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#playerActed(org.ozsoft.texasholdem.Player)
     */
    @Override
    public void playerActed(Player player) {
        System.out.format("%s %s.\n", player, player.getAction().getVerb());
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.texasholdem.Client#act(java.util.Set)
     */
    @Override
    public Action act(Set<Action> actions) {
        StringBuilder sb = new StringBuilder("Please select an action: ");
        int i = actions.size();
        for (Action action : actions) {
            sb.append(action);
            i--;
            if (i > 1) {
                sb.append(", ");
            } else if (i == 1) {
                sb.append(" or ");
            } else {
                // No more actions.
            }
        }
        sb.append("? ");
        String prompt = sb.toString();
        Action selectedAction = null;
        while (selectedAction == null) {
            System.out.print(prompt);
            try {
                String input = consoleReader.readLine();
                if (input != null) {
                    for (Action action : actions) {
                        String command = action.toString().toLowerCase();
                        if (command.startsWith(input.toLowerCase())) {
                            selectedAction = action;
                            break;
                        }
                    }
                    if (selectedAction == null) {
                        System.out.println("Invalid action -- please try again.");
                    }
                }
            } catch (IOException e) {
                // The VM is killed; ignore.
            }
        }
        return selectedAction;
    }

}
