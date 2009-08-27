package org.ozsoft.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A standard, generic deck of game cards without jokers.
 *
 * NOTE: This class is implemented with the focus on performance (instead of clean design).
 */
public class Deck {
    
    /** The number of cards in a deck. */
    private static final int NO_OF_CARDS = Card.NO_OF_RANKS * Card.NO_OF_SUITS;
    
    /** The cards in the deck. */
    private Card[] cards;
    
    /** The index of the next card to deal. */
    private int nextCardIndex = 0;
    
    /** The randomizer instance. */
    private Random random = new Random();

    /**
     * Constructor.
     * 
     * Starts as a full, ordered deck.
     */
    public Deck() {
        cards = new Card[NO_OF_CARDS];
        int index = 0;
        for (int suit = Card.NO_OF_SUITS - 1; suit >= 0; suit--) {
            for (int rank = Card.NO_OF_RANKS - 1; rank >= 0 ; rank--) {
                cards[index++] = new Card(rank, suit);
            }
        }
    }
    
    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        for (int oldIndex = 0; oldIndex < NO_OF_CARDS; oldIndex++) {
            int newIndex = random.nextInt(NO_OF_CARDS);
            Card tempCard = cards[oldIndex];
            cards[oldIndex] = cards[newIndex];
            cards[newIndex] = tempCard;
        }
        nextCardIndex = 0;
    }
    
    /**
     * Resets the deck to full.
     * 
     * Does not re-order the cards.
     */
    public void reset() {
        nextCardIndex = 0;
    }
    
    /**
     * Returns a single dealt cards.
     *
     * @return  the card dealt
     */
    public Card deal() {
        if (nextCardIndex + 1 >= NO_OF_CARDS) {
            throw new IllegalStateException("No cards left in deck");
        }
        return cards[nextCardIndex++];
    }
    
    /**
     * Returns the specified number of dealt cards.
     * 
     * @param noOfCards
     *            the number of cards to deal
     * 
     * @return The cards.
     * 
     * @throws IllegalArgumentException
     *             If the number of cards is invalid.
     * @throws IllegalStateException
     *             If there are no cards left in the deck.
     */
    public List<Card> deal(int noOfCards) {
        if (noOfCards < 1) {
            throw new IllegalArgumentException("noOfCards < 1");
        }
        if (nextCardIndex + noOfCards >= NO_OF_CARDS) {
            throw new IllegalStateException("No cards left in deck");
        }
        List<Card> dealtCards = new ArrayList<Card>();
        for (int i = 0; i < noOfCards; i++) {
            dealtCards.add(cards[nextCardIndex++]);
        }
        return dealtCards;
    }
    
    /**
     * Deals a specific card.
     * 
     * @param rank
     *            The card's rank.
     * @param suit
     *            The card's suit.
     * 
     * @return The card if available, otherwise null.
     * 
     * @throws IllegalStateException
     *             If there are no cards left in the deck.
     */
    public Card deal(int rank, int suit) {
        if (nextCardIndex + 1 >= NO_OF_CARDS) {
            throw new IllegalStateException("No cards left in deck");
        }
        Card card = null;
        int index = -1;
        for (int i = nextCardIndex; i < NO_OF_CARDS; i++) {
            if ((cards[i].getRank() == rank) && (cards[i].getSuit() == suit)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            if (index != nextCardIndex) {
                Card nextCard = cards[nextCardIndex];
                cards[nextCardIndex] = cards[index];
                cards[index] = nextCard;
            }
            card = deal();
        }
        return card;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    
}
