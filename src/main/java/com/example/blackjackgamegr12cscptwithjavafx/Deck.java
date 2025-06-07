package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a deck of playing cards. This deck supports 8 standard 52-card sets
 * (i.e. 416 cards in total when fully populated), provides methods for drawing cards,
 * checking if the deck is low on cards, shuffling, and regenerating a shuffled deck.
 */
public class Deck {
    /**
     * The list of cards in the deck.
     * Initially empty, filled with 8 decks of cards when generated.
     */
    private final ArrayList<Card> cards;

    /**
     * Constructor for the Deck class. <p>
     * Initializes an empty deck of cards.
     */
    public Deck() {
        cards = new ArrayList<>();
    }

    /**
     * Fills the deck with 8 sorted decks of cards, each containing 52 cards.
     * The deck is filled with cards from Ace to King in each suit (Clubs, Diamonds, Hearts, Spades).
     */
    private void fillSortedDeck(){
        cards.clear();
        final String[] suits = {Suit.Club, Suit.Diamond, Suit.Heart, Suit.Spade};
        final String[] faceCardRanks = {"J", "Q", "K"};
        for (int decks = 0; decks < 8; decks++) {
            for (String suit : suits) {
                for (int j = 1; j <= 13; j++) {
                    if (j == 1) {
                        cards.add(new Ace(new Suit(suit)));
                    } else if (j > 10) {
                        cards.add(new FaceCard(faceCardRanks[j - 11], new Suit(suit)));
                    } else {
                        cards.add(new Card(j, Integer.toString(j), new Suit(suit)));
                    }
                }
            }
        }

    }

    /**
     * Checks if the deck has fewer than 20 cards remaining.
     * @return true if the deck has fewer than 20 cards, false otherwise.
     */
    public boolean lowOnCards(){
        return cards.size() < 20;
    }

    /**
     * Shuffles the deck of cards.
     * Uses Collections.shuffle to randomize the order of cards in the deck.
     */
    private void shuffleDeck(){
        Collections.shuffle(cards);
    }

    /**
     * Adds a card to the of the deck. (will be first to be pulled out)
     * <p> Best for edge-case testing for developers...</p>
     * @param card The card to be added to the deck.
     * @deprecated This method is deprecated and should not be used in new code.
     * It is intended for testing purposes only.
     */
    @Deprecated
    public void addCard(Card card){
        System.out.println("Add card is active");
        cards.add(Objects.requireNonNull(card, "Card cannot be null!!"));
    }

    /**
     * Generates a new shuffled deck of cards.
     * This method fills the deck with 8 sorted decks of cards and then shuffles them.
     */
    public void generateDeck(){
        fillSortedDeck();
        shuffleDeck();
    }

    /**
     * Retrieves the current deck of cards.
     * @return An {@code ArrayList<Card>} containing the cards in the deck.
     */
    public ArrayList<Card> getDeck(){
        return cards;
    }

    /**
     * Draws a card from the deck.
     * <p>
     * This method removes and returns the last card in the deck.
     * It is assumed that the deck is not empty when this method is called.
     *
     * @return The drawn {@link Card}.
     */
    public Card drawCard(){
        return cards.remove(cards.size()-1); // draw the last card
    }
}
