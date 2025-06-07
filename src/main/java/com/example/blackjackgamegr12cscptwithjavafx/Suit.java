package com.example.blackjackgamegr12cscptwithjavafx;

/**
 * Represents the suit of a playing card (Spade, Heart, Club, Diamond).
 * <p>
 * This record ensures that only valid suits are created, either by name (case-insensitive)
 * or by their Unicode symbol. Used throughout the card classes to specify the suit of each card.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     Suit spade = new Suit("Spade");
 *     Suit heart = new Suit("♥");
 * </pre>
 * </p>
 */
public record Suit(String suit) {

    /** Unicode symbol for Spade */
    public static final String Spade = "♠"; // ♠
    /** Unicode symbol for Heart */
    public static final String Heart = "♥"; // ♥
    /** Unicode symbol for Club */
    public static final String Club = "♣"; // ♣
    /** Unicode symbol for Diamond */
    public static final String Diamond = "♦"; // ♦

    /**
     * Constructs a Suit, validating the input as a recognized suit name or symbol.
     *
     * @param suit The name (e.g., "Spade") or symbol (e.g., "♠") of the suit.
     * @throws IllegalArgumentException if the input is not a valid suit.
     */
    public Suit(String suit) {
        if (suit.equalsIgnoreCase("spade") || suit.equals(Spade)) {
            this.suit = Suit.Spade;
        } else if (suit.equalsIgnoreCase("heart") || suit.equals(Heart)) {
            this.suit = Suit.Heart;
        } else if (suit.equalsIgnoreCase("club") || suit.equals(Club)) {
            this.suit = Suit.Club;
        } else if (suit.equalsIgnoreCase("diamond") || suit.equals(Diamond)) {
            this.suit = Suit.Diamond;
        } else {
            throw new IllegalArgumentException("Invalid suit");
        }
    }

    /**
     * Returns the Unicode symbol of the suit.
     * @return the suit symbol as a String
     */
    @Override
    public String toString() {
        return suit;
    }

    /**
     * Checks if the suit is red (Heart or Diamond).
     * @return true if the suit is red, false otherwise
     */
    public boolean isRed() {
        return this.suit.equals(Heart) || this.suit.equals(Diamond);
    }
}
