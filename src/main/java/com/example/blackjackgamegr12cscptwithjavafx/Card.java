package com.example.blackjackgamegr12cscptwithjavafx;

import javafx.scene.image.Image;

import java.util.Objects;


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

    /**
     * Returns the rank of this card.
     * @return The rank as a String (e.g. "A", "K", "10", etc.)
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns the image for this card.
     * @return An Image object representing this card's graphical representation
     */
    public Image getCardImage() {
        String suitName;
        String fileName;

        // Convert Unicode suit symbols to their text names for file paths
        suitName = switch (suit.toString()) {
            case Suit.Spade -> "spades";
            case Suit.Heart -> "hearts";
            case Suit.Club -> "clubs";
            default -> "diamonds";
        };

        // Handle face cards and number cards differently in the filename
        if (rank.equals("A")) {
            fileName = "ace_of_" + suitName + ".png";
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            // Convert face cards to lowercase full name (e.g., "king_of_hearts.png")
            String fullRank = switch(rank) {
                case "K" -> "king";
                case "Q" -> "queen";
                case "J" -> "jack";
                default -> rank.toLowerCase();
            };
            fileName = fullRank + "_of_" + suitName + ".png";
        } else {
            fileName = rank + "_of_" + suitName + ".png";
        }

        String imagePath = "/com/example/blackjackgamegr12cscptwithjavafx/images/Cards/" + fileName;
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * Returns the image for the back of a card.
     * @return An Image object representing the back of a card
     */
    public static Image getCardBackImage() {
        return new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/com/example/blackjackgamegr12cscptwithjavafx/images/Cards/Back_of_card.jpg")));
    }
}
