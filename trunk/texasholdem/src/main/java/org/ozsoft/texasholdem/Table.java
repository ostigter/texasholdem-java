package org.ozsoft.texasholdem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Limit Texas Hold'em poker table.
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
    
    /** The bet in the current hand. */
    private int bet;
    
    /** The pot in the current hand. */
    private int pot;
    
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
        rotateActor();
        postSmallBlind();
        rotateActor();
        postBigBlind();
        bet = bigBlind;
        // The Pre-Flop.
        dealHoleCards();
        doBettingRound();
        if (activePlayers.size() > 1) {
            // The Flop.
            dealCommunityCards("Flop", 3);
            doBettingRound();
            if (activePlayers.size() > 1) {
                // The Turn.
                dealCommunityCards("Turn", 1);
                doBettingRound();
                if (activePlayers.size() > 1) {
                    // The River.
                    dealCommunityCards("River", 1);
                    bet = 2 * bigBlind;
                    doBettingRound();
                    if (activePlayers.size() > 1) {
                        // The Showdown.
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
        notifyBoardUpdated();
//		resetBets();
        activePlayers.clear();
        for (Player player : players) {
            player.resetHand();
            if (!player.isBroke()) {
                activePlayers.add(player);
            }
        }
        dealerPosition = (dealerPosition + 1) % players.size();
        dealer = players.get(dealerPosition);
        deck.shuffle();
        actorPosition = dealerPosition;
        minBet = bigBlind;
        for (Player player : players) {
            player.getClient().handStarted(dealer);
        }
        notifyMessage("New hand. %s is the dealer.", dealer);
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
        int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        pot += smallBlind;
        notifyPlayerActed();
    }
    
    /**
     * Posts the big blind.
     */
    private void postBigBlind() {
        actor.postBigBlind(bigBlind);
        pot += bigBlind;
        notifyPlayerActed();
    }
    
    /**
     * Deals the Hole Cards.
     */
    private void dealHoleCards() {
        for (Player player : players) {
            player.setCards(deck.deal(2));
            player.getClient().playerUpdated(player);
        }
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
        notifyBoardUpdated();
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
            int smallBlind = bigBlind / 2;
            boolean isSmallBlindPosition = (actor.getBet() == smallBlind);
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
                case CALL:
                    if (isSmallBlindPosition) {
                        // Correct bet for small blind.
                        pot += bet - smallBlind;
                    } else {
                        pot += bet;
                    }
                    break;
                case BET:
                    bet = minBet;
                    pot += bet;
                    playersToAct = activePlayers.size();
                    break;
                case RAISE:
                    bet += minBet;
                    pot += bet;
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
                        // The player left wins.
                        playerWins(activePlayers.get(0));
                        playersToAct = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("Invalid action: " + action);
            }
            notifyPlayerActed();
        }
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
        int actorBet = actor.getBet();
        Set<Action> actions = new HashSet<Action>();
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
        return actions;
    }
    
    /**
     * Performs the Showdown.
     */
    private void doShowdown() {
        notifyMessage("Showdown!");
        notifyBoardUpdated();
        int highestValue = 0;
        List<Player> winners = new ArrayList<Player>();
        for (Player player : activePlayers) {
            // Create a hand with the community cards and the player's hole cards.
            Hand playerHand = new Hand(board);
            playerHand.addCards(player.getCards());
            // Evaluate the combined hand.
            HandEvaluator evaluator = new HandEvaluator(playerHand);
            int value = evaluator.getValue();
//			String description = evaluator.getType().getDescription();
//			notifyMessage("%s's cards:  %s\t(%s, %d)", player, player.getHand(), description, value);
            // Look for one or more winners.
            if (value > highestValue) {
                // New winner.
                highestValue = value;
                winners.clear();
                winners.add(player);
            } else if (value == highestValue) {
                // Tie winner.
                winners.add(player);
            } else {
                // Loser.
            }
        }
        if (winners.size() == 1) {
            // Single winner.
            playerWins(winners.get(0));
        } else {
            // Tie; multiple winners.
            //TODO: Handle tie
            notifyMessage("A tie! X and Y share the pot.");
        }
    }
    
    /**
     * Let's a player win the pot.
     * 
     * @param player
     *            The winning player.
     */
    private void playerWins(Player player) {
        player.win(pot);
        pot = 0;
//		notifyBoardUpdated();
        notifyMessage("%s wins.", player);
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
        for (Player p : players) {
            p.getClient().messageReceived(message);
        }
    }
    
    /**
     * Notifies clients that the board has been updated.
     */
    private void notifyBoardUpdated() {
        for (Player player : players) {
            player.getClient().boardUpdated(board, bet, pot);
        }
    }

    /**
     * Notifies clients that a player has acted.
     * 
     * @param player
     *            The player that has acted.
     */
    private void notifyPlayerActed() {
        Player publicClone = actor.publicClone();
        for (Player p : players) {
            p.getClient().playerActed(publicClone);
        }
    }
    
}
