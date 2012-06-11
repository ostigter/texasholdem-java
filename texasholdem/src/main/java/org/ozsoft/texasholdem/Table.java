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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Fixed Limit Texas Hold'em poker table. <br />
 * <br />
 * 
 * Controls the game flow for a single poker table.
 * 
 * @author Oscar Stigter
 */
public class Table {
    
    /** The maximum number of bets or raises in a single hand per player. */
    private static final int MAX_RAISES = 4;
    
    /** The size of the big blind. */
    private final int bigBlind;
    
    /** The players at the table. */
    private final List<Player> players;
    
    /** The active players in the current hand. */
    private final List<Player> activePlayers;
    
    /** The deck of cards. */
    private final Deck deck;
    
    /** The community cards on the board. */
    private final List<Card> board;
    
    /** The current dealer position. */
    private int dealerPosition;

    /** The current dealer. */
    private Player dealer;

    /** The position of the acting player. */
    private int actorPosition;
    
    /** The acting player. */
    private Player actor;

    /** The minimum bet in the current hand. */
    private int minBet;
    
    /** The current bet in the current hand. */
    private int bet;
    
    /** The pot in the current hand. */
    private final List<Pot> pots;
    
    /** Whether the game is over. */
    private boolean gameOver;
    
    /**
     * Constructor.
     * 
     * @param bigBlind
     *            The size of the big blind.
     */
    public Table(int bigBlind) {
        this.bigBlind = bigBlind;
        players = new ArrayList<Player>();
        activePlayers = new ArrayList<Player>();
        deck = new Deck();
        board = new ArrayList<Card>();
        pots = new ArrayList<Pot>();
    }
    
    /**
     * Adds a player.
     * 
     * @param player
     *            The player.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * Main game loop.
     */
    public void start() {
        resetGame();
        while (!gameOver) {
            playHand();
        }
        notifyMessage("Game over.");
    }
    
    /**
     * Resets the game.
     */
    private void resetGame() {
        dealerPosition = -1;
        actorPosition = -1;
        gameOver = false;
        for (Player player : players) {
            player.getClient().joinedTable(bigBlind, players);
        }
    }
    
    /**
     * Plays a single hand.
     */
    private void playHand() {
        resetHand();
        
        // Small blind.
        rotateActor();
        postSmallBlind();
        
        // Big blind.
        rotateActor();
        postBigBlind();
        
        // Pre-Flop.
        dealHoleCards();
        doBettingRound();
        
        // Flop.
        if (activePlayers.size() > 1) {
            bet = 0;
            dealCommunityCards("Flop", 3);
            doBettingRound();

            // Turn.
            if (activePlayers.size() > 1) {
                bet = 0;
                dealCommunityCards("Turn", 1);
                minBet = 2 * bigBlind;
                doBettingRound();

                // River.
                if (activePlayers.size() > 1) {
                    bet = 0;
                    dealCommunityCards("River", 1);
                    doBettingRound();

                    // Showdown.
                    if (activePlayers.size() > 1) {
                        bet = 0;
                        doShowdown();
                    }
                }
            }
        }
    }
    
    /**
     * Resets the game for a new hand.
     */
    private void resetHand() {
        board.clear();
        bet = 0;
        pots.clear();
        notifyBoardUpdated();
        activePlayers.clear();
        for (Player player : players) {
            player.resetHand();
            if (!player.isBroke()) {
                activePlayers.add(player);
            }
        }
        dealerPosition = (dealerPosition + 1) % activePlayers.size();
        dealer = activePlayers.get(dealerPosition);
        deck.shuffle();
        actorPosition = dealerPosition;
        minBet = bigBlind;
        bet = minBet;
        for (Player player : players) {
            player.getClient().handStarted(dealer);
        }
        notifyPlayersUpdated(false);
        notifyMessage("New hand, %s is the dealer.", dealer);
    }

    /**
     * Rotates the position of the player in turn (the actor).
     */
    private void rotateActor() {
        if (activePlayers.size() > 0) {
            do {
                actorPosition = (actorPosition + 1) % players.size();
                actor = players.get(actorPosition);
            } while (!activePlayers.contains(actor));
            for (Player player : players) {
                player.getClient().actorRotated(actor);
            }
        } else {
            // Should never happen.
            throw new IllegalStateException("No active players left");
        }
    }
    
    /**
     * Posts the small blind.
     */
    private void postSmallBlind() {
        final int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        contributePot(smallBlind);
        notifyBoardUpdated();
        notifyPlayerActed();
    }
    
    /**
     * Posts the big blind.
     */
    private void postBigBlind() {
        actor.postBigBlind(bigBlind);
        contributePot(bigBlind);
        notifyBoardUpdated();
        notifyPlayerActed();
    }
    
    /**
     * Deals the Hole Cards.
     */
    private void dealHoleCards() {
        for (Player player : activePlayers) {
            player.setCards(deck.deal(2));
        }
        notifyPlayersUpdated(false);
        notifyMessage("%s deals the hole cards.", dealer);
    }
    
    /**
     * Deals a number of community cards.
     * 
     * @param phaseName
     *            The name of the phase.
     * @param noOfCards
     *            The number of cards to deal.
     */
    private void dealCommunityCards(String phaseName, int noOfCards) {
        for (int i = 0; i < noOfCards; i++) {
            board.add(deck.deal());
        }
        notifyPlayersUpdated(false);
        notifyMessage("%s deals the %s.", dealer, phaseName);
    }
    
    /**
     * Performs a betting round.
     */
    private void doBettingRound() {
        // Determine the number of active players.
        int playersToAct = activePlayers.size();
        // Determine the initial player and bet size.
        if (board.size() == 0) {
            // Pre-Flop; player left of big blind starts, bet is the big blind.
            bet = bigBlind;
        } else {
            // Otherwise, player left of dealer starts, no initial bet.
            actorPosition = dealerPosition;
            bet = 0;
        }
        notifyBoardUpdated();
        while (playersToAct > 0) {
            rotateActor();
            Set<Action> allowedActions = getAllowedActions(actor);
            Action action = actor.act(allowedActions, minBet, bet);
            if (!allowedActions.contains(action)) {
                String msg = String.format("Illegal action (%s) from player %s!", action, actor);
                throw new IllegalStateException(msg);
            }
            playersToAct--;
            switch (action) {
                case CHECK:
                    // Do nothing.
                    break;
                case ALL_IN:
                    // Do nothing.
                    break;
                case CALL:
                    contributePot(actor.getBetIncrement());
                    break;
                case BET:
                    bet = minBet;
                    contributePot(actor.getBetIncrement());
                    playersToAct = activePlayers.size();
                    break;
                case RAISE:
                    bet += minBet;
                    contributePot(actor.getBetIncrement());
                    if (actor.getRaises() == MAX_RAISES) {
                        // Max. number of raises reached; other players get one more turn.
                        playersToAct = activePlayers.size() - 1;
                    } else {
                        // Otherwise, all players get another turn.
                        playersToAct = activePlayers.size();
                    }
                    break;
                case FOLD:
                    actor.setCards(null);
                    activePlayers.remove(actor);
                    if (activePlayers.size() == 1) {
                        // The player left wins the pot.
                        notifyBoardUpdated();
                        notifyPlayerActed();
                        Player winner = activePlayers.get(0);
                        int amount = getTotalPot();
                        winner.win(amount);
                        notifyBoardUpdated();
                        notifyMessage("%s wins $%d.", winner, amount);
                        playersToAct = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("Invalid action: " + action);
            }
            if (playersToAct > 0) {
                notifyBoardUpdated();
                notifyPlayerActed();
            }
        }
        
        // Reset player's bets.
        for (Player player : activePlayers) {
            player.resetBet();
        }
        notifyBoardUpdated();
        notifyPlayersUpdated(false);
    }
    
    /**
     * Returns the allowed actions of a specific player.
     * 
     * @param player
     *            The player.
     * 
     * @return The allowed actions.
     */
    private Set<Action> getAllowedActions(Player player) {
        Set<Action> actions = new HashSet<Action>();
        if (player.isBroke()) {
            actions.add(Action.CHECK);
        } else {
            int actorBet = actor.getBet();
            if (bet == 0) {
                actions.add(Action.CHECK);
                if (player.getRaises() < MAX_RAISES) {
                    actions.add(Action.BET);
                }
            } else {
                if (actorBet < bet) {
                    actions.add(Action.CALL);
                    if (player.getRaises() < MAX_RAISES) {
                        actions.add(Action.RAISE);
                    }
                } else {
                    actions.add(Action.CHECK);
                    if (player.getRaises() < MAX_RAISES) {
                        actions.add(Action.RAISE);
                    }
                }
            }
            actions.add(Action.FOLD);
        }
        return actions;
    }
    
    /**
     * Contributes to the pot.
     * 
     * @param amount
     *            The amount to contribute.
     */
    private void contributePot(int amount) {
        for (Pot pot : pots) {
            if (!pot.hasContributer(actor)) {
                int potBet = pot.getBet();
                if (amount >= potBet) {
                    // Regular call, bet or raise.
                    pot.addContributer(actor);
                    amount -= pot.getBet();
                } else {
                    // Partial call (all-in); redistribute pots.
                    pots.add(pot.split(actor, amount));
                    amount = 0;
                }
            }
            if (amount <= 0) {
                break;
            }
        }
        if (amount > 0) {
            Pot pot = new Pot(amount);
            pot.addContributer(actor);
            pots.add(pot);
        }
    }
    
    /**
     * Performs the Showdown.
     */
    private void doShowdown() {
        // Sort players by hand value (highest to lowest).
        System.out.print("Board: ");
        for (Card card : board) {
            System.out.print(card + " ");
        }
        System.out.println();
        Map<HandValue, List<Player>> rankedPlayers = new TreeMap<HandValue, List<Player>>();
        for (Player player : activePlayers) {
            // Create a hand with the community cards and the player's hole cards.
            Hand hand = new Hand(board);
            hand.addCards(player.getCards());
            // Store the player together with other players with the same hand value.
            HandValue handValue = new HandValue(hand);
            System.out.format("%s: %s\n", player, handValue);
            List<Player> playerList = rankedPlayers.get(handValue);
            if (playerList == null) {
                playerList = new ArrayList<Player>();
            }
            playerList.add(player);
            rankedPlayers.put(handValue, playerList);
        }

        printPot();
        int totalPot = getTotalPot();

        // Per rank (single or multiple winners), calculate pot distribution.
        Map<Player, Integer> potDivision = new HashMap<Player, Integer>();
        for (HandValue handValue : rankedPlayers.keySet()) {
            List<Player> winners = rankedPlayers.get(handValue);
            for (Pot pot : pots) {
                // Determine how many winners share this pot.
                int noOfWinnersInPot = 0;
                for (Player winner : winners) {
                    if (pot.hasContributer(winner)) {
                        noOfWinnersInPot++;
                    }
                }
                if (noOfWinnersInPot > 0) {
                    // Divide pot over winners.
                    int potShare = pot.getValue() / noOfWinnersInPot;
                    for (Player winner : winners) {
                        if (pot.hasContributer(winner)) {
                            Integer oldShare = potDivision.get(winner);
                            if (oldShare != null) {
                                potDivision.put(winner, oldShare + potShare);
                            } else {
                                potDivision.put(winner, potShare);
                            }
                            
                        }
                    }
                    pot.clear();
                }
            }
        }
        
        // Divide winnings.
        StringBuilder winnerText = new StringBuilder();
        int totalWon = 0;
        for (Player winner : potDivision.keySet()) {
            int potShare = potDivision.get(winner);
            winner.win(potShare);
            totalWon += potShare;
            if (winnerText.length() > 0) {
                winnerText.append(", ");
            }
            winnerText.append(String.format("%s wins $%d", winner, potShare));
            notifyPlayersUpdated(false);
        }
        winnerText.append('.');
        notifyMessage(winnerText.toString());
        
        // Sanity check.
        if (totalWon != totalPot) {
            throw new IllegalStateException("Incorrect pot division");
        }
    }
    
    /**
     * Notifies listeners with a custom game message.
     * 
     * @param message
     *            The formatted message.
     * @param args
     *            Any arguments.
     */
    private void notifyMessage(String message, Object... args) {
        message = String.format(message, args);
        for (Player player : players) {
            player.getClient().messageReceived(message);
        }
    }
    
    /**
     * Notifies clients that the board has been updated.
     */
    private void notifyBoardUpdated() {
        int pot = getTotalPot();
        for (Player player : players) {
            player.getClient().boardUpdated(board, bet, pot);
        }
    }
    
    private int getTotalPot() {
        int totalPot = 0;
        for (Pot pot : pots) {
            totalPot += pot.getValue();
        }
        return totalPot;
    }

    /**
     * Notifies clients that one or more players have been updated.
     * 
     * A player's secret information is only sent its own client; other clients
     * see only a player's public information.
     */
    private void notifyPlayersUpdated(boolean showdown) {
        for (Player playerToNotify : players) {
            for (Player player : players) {
                if (!showdown && !player.equals(playerToNotify)) {
                    // Hide secret information to other players.
                    player = player.publicClone();
                }
                playerToNotify.getClient().playerUpdated(player);
            }
        }
    }
    
    /**
     * Notifies clients that a player has acted.
     */
    private void notifyPlayerActed() {
        for (Player p : players) {
            Player playerInfo = p.equals(actor) ? actor : actor.publicClone();
            p.getClient().playerActed(playerInfo);
        }
    }
    
    /**
     * Debug method to print the pot.
     */
    private void printPot() {
        System.out.println("Pot:");
        for (Pot pot : pots) {
            System.out.format("  %s\n", pot);
        }
        System.out.format("  Total: %d\n", getTotalPot());
    }

}
