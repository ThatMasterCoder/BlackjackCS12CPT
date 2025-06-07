package com.example.blackjackgamegr12cscptwithjavafx;


/**
 * Overarching class for all types of cards.
 * Extended for Face Cards, Aces.
 * Holds cardValue, a card Rank and its suit.
 */
public class Card {
    /**
     * Attributes of each card.
     */
    private final int cardValue;
    private final String rank;
    private final Suit suit;


    /** Constructor for the Card class.
     * @see Suit
     * @param cardValue The value of the card (i.e. King would have 10, 6 would have 6)
     * @param cardRank The Rank of the card (i.e. 8, K, A)
     * @param cardSuit The suit of the card.
     */
    public Card(int cardValue, String cardRank, Suit cardSuit) {
        this.cardValue = cardValue;
        this.rank = cardRank;
        this.suit = cardSuit;
    }

    /**
     * @return The value of the card.
     */
    public int getCardValue() {
        return cardValue;
    }


    /**
     * @return A {@code String} in the style "{rank} of {suit}". i.e. "6 of Clubs"
     */
    @Override
    public String toString() {
        return this.rank + " of " + this.suit;
    }

    public Suit getSuit() {
        return suit;
    }
}
