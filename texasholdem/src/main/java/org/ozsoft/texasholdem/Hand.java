package org.ozsoft.texasholdem;

import java.util.Collection;

/**
 * A generic hand of game cards.
 * 
 * The cards are ordered highest to lowest.
 *
 * NOTE: This class is implemented with the focus on performance (instead of clean design).
 */
public class Hand {
    
    /** The maximum number of cards in a hand. */
    private static final int MAX_NO_OF_CARDS = 10;
    
    /** The cards in this hand. */
    private Card[] cards = new Card[MAX_NO_OF_CARDS];
    
    /** The current number of cards in this hand. */
    private int noOfCards = 0;
    
    /**
     * Constructor for an empty hand.
     */
    public Hand() {
        // Empty implementation.
    }
    
    /**
     * Constructor with an array of initial cards.
     *
     * @param  initialCards  the initial cards
     */
    public Hand(Card[] initialCards) {
        for (Card card : initialCards) {
            addCard(card);
        }
    }
    
    /**
     * Constructor with a collection of initial cards.
     *
     * @param  initialCards  the initial cards
     */
    public Hand(Collection<Card> initialCards) {
        for (Card card : initialCards) {
            addCard(card);
        }
    }
    
    /**
	 * Parses a string as a hand of cards.
	 * 
	 * @param s
	 *            The string to parse.
	 * 
	 * @return The hand of cards.
	 * 
	 * @throws IllegalArgumentException
	 *             If the string could not be parsed.
	 */
    public static Hand parseHand(String s) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null or empty string");
        }
        
        Hand hand = new Hand();
        String[] parts = s.split("\\s");
        for (String part : parts) {
            hand.addCard(new Card(part));
        }
        return hand;
    }
    
    /**
	 * Returns the number of cards.
	 * 
	 * @return The number of cards.
	 */
    public int size() {
        return noOfCards;
    }
    
    /**
	 * Adds a single card.
	 * 
	 * The card is inserted at such a position that the hand remains sorted
	 * (highest ranking cards first).
	 * 
	 * @param card
	 *            The card to add.
	 */
    public void addCard(Card card) {
        int insertIndex = -1;
        for (int i = 0; i < noOfCards; i++) {
            if (card.compareTo(cards[i]) > 0) {
                insertIndex = i;
                break;
            }
        }
        if (insertIndex == -1) {
        	// Could not insert anywhere, so append at the end.
            cards[noOfCards++] = card;
        } else {
            for (int i = noOfCards; i > insertIndex; i--) {
                cards[i] = cards[i - 1];
            }
            cards[insertIndex] = card;
            noOfCards++;
        }
    }
    
    /**
	 * Adds multiple cards.
	 * 
	 * The cards are inserted at such a position that the hand remains sorted
	 * (highest ranking cards first).
	 * 
	 * @param addedCards
	 *            The cards to add.
	 */
    public void addCards(Collection<Card> addedCards) {
        for (Card card : addedCards) {
            addCard(card);
        }
    }
    
    /**
	 * Adds multiple cards.
	 * 
	 * The cards are inserted at such a position that the hand remains sorted
	 * (highest ranking cards first).
	 * 
	 * @param addedCards
	 *            The cards to add.
	 */
    public void addCards(Card[] addedCards) {
        for (Card card : addedCards) {
            addCard(card);
        }
    }
    
    /**
     * Returns the cards.
     *
     * @return The cards.
     */
    public Card[] getCards() {
        Card[] dest = new Card[noOfCards];
        System.arraycopy(cards, 0, dest, 0, noOfCards);
        return dest;
    }
    
    /**
     * Removes all cards.
     */
    public void removeAllCards() {
        noOfCards = 0;
    }
    
    /**
     * Returns a string representation of this object.
     *
     * @return  the string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < noOfCards; i++) {
            sb.append(cards[i]);
            if (i < (noOfCards - 1)) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
    
}
