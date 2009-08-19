package org.ozsoft.texasholdem;

/**
 * A generic game card.
 * 
 * Its value is determined first by rank, then by suit.
 */
public class Card implements Comparable<Card> {
    
	/** The number of ranks in a deck. */
	public static final int NO_OF_RANKS = 13;
	
	/** The number of suits in a deck. */
    public static final int NO_OF_SUITS = 4;
    
    // The ranks.
    public static final int ACE      = 12;
    public static final int KING     = 11;
    public static final int QUEEN    = 10;
    public static final int JACK     = 9;
    public static final int TEN      = 8;
    public static final int NINE     = 7;
    public static final int EIGHT    = 6;
    public static final int SEVEN    = 5;
    public static final int SIX      = 4;
    public static final int FIVE     = 3;
    public static final int FOUR     = 2;
    public static final int THREE    = 1;
    public static final int DEUCE    = 0;
    
    // The suits.
    public static final int SPADES   = 3;
    public static final int HEARTS   = 2;
    public static final int CLUBS    = 1;
    public static final int DIAMONDS = 0;
    
    /** The rank symbols. */
    public static final String[] RANK_SYMBOLS = {
        "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"
    };
    
    /** The suit symbols. */
    public static final char[] SUIT_SYMBOLS = { 'd', 'c', 'h', 's' };

    /** The rank. */
    private final int rank;
    
    /** The suit. */
    private final int suit;
    
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }
    
    /**
	 * Returns the card represented by the specified string.
	 * 
	 * @param s
	 *            The string representation of the card, e.g. "As", "Td", "7h".
	 * 
	 * @return The card.
	 * 
	 * @throws IllegalArgumentException
	 *             If the card string could not be parsed.
	 */
    public Card(String s) {
        if ((s != null) && (s.length() == 2)) {
            int length = s.length();
            String rankSymbol = s.substring(0, length - 1);
            char suitSymbol = s.charAt(length - 1);
            int rank = -1;
            for (int i = 0; i < Card.NO_OF_RANKS; i++) {
                if (rankSymbol.equals(RANK_SYMBOLS[i])) {
                    rank = i;
                    break;
                }
            }
            if (rank == -1) {
                throw new IllegalArgumentException("Error parsing card; unknown rank: " + rankSymbol);
            }
            int suit = -1;
            for (int i = 0; i < Card.NO_OF_SUITS; i++) {
                if (suitSymbol == SUIT_SYMBOLS[i]) {
                    suit = i;
                    break;
                }
            }
            if (suit == -1) {
                throw new IllegalArgumentException("Error parsing card; unknown suit: " + suitSymbol);
            }
            this.rank = rank;
            this.suit = suit;
        } else {
            throw new IllegalArgumentException("Null or empty string, or invalid length");
        }
    }
    
    /**
     * Returns the suit.
     * 
     * @return The suit.
     */
    public int getSuit() {
        return suit;
    }
    
    /**
	 * Returns the rank.
	 * 
	 * @return The rank.
	 */
    public int getRank() {
        return rank;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (rank * NO_OF_SUITS + suit);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
        	return ((Card) obj).hashCode() == hashCode();
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card card) {
        int thisValue = hashCode();
        int otherValue = card.hashCode();
        if (thisValue < otherValue) {
            return -1;
        } else if (thisValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return RANK_SYMBOLS[rank] + SUIT_SYMBOLS[suit];
    }
    
}
