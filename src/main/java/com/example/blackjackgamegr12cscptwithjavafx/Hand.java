package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;

public class Hand {

    /**
     * A list of cards representing the current set of cards held by the Hand.
     * This collection is used to manage and track the cards associated with the Hand,
     * enabling operations like adding new cards, calculating the score,
     * and determining Blackjack or bust conditions.
     */
    private final ArrayList<Card> cards = new ArrayList<>();

    /**
     * The number of aces in the hand.
     * This is used to adjust the score calculation, allowing aces to be counted as either 1 or 11.
     */
    private int aces = 0;

    /**
     * Adds a card to the hand.
     * If the card is an Ace, it increments the ace count.
     *
     * @param card The card to be added to the hand.
     * @return The added card.
     */
    public Card addCard(Card card){
        cards.add(card);

        if (card instanceof Ace) {
            aces++;
        }
        return card;
    }

    /**
     * Retrieves the list of cards in the hand.
     *
     * @return An {@code ArrayList<Card>} containing the cards in the hand.
     */
    public ArrayList<Card> getCards(){
        return cards;
    }



    public boolean isBlackjack(){
        return cards.size() == 2 && getScore() == 21;
    }

    /**
     * Checks if the hand is bust (i.e., the total score exceeds 21).
     *
     * @return {@code true} if the hand is bust, {@code false} otherwise.
     */
    public boolean isBust(){
        return getScore() > 21;
    }

    /**
     * Resets the hand by clearing all cards and resetting the ace count.
     * This method is typically called at the start of a new game or round.
     */
    public void reset(){
        cards.clear();
        aces = 0;
    }
    
    /**
     * Calculates the total value of the hand and the number of soft aces (aces counted as 11).
     * A soft ace is an ace that can be counted as 11 without busting the hand.
     * If the total exceeds 21, soft aces are converted to hard aces (counted as 1) as needed.
     * @return an int array where index 0 is the total hand value, and index 1 is the number of soft aces
     */
    private int[] calculateTotalAndSoftAces() {
        int total = 0;
        int softAces = aces;

        for (Card card : cards) {
            total += card.getCardValue();
        }

        while (total > 21 && softAces > 0) {
            total -= 10;
            softAces--;
        }

        return new int[]{total, softAces};
    }

    /**
     * Returns the total score of the hand, accounting for soft aces.
     * @return the hand's total value as an int
     */
    public int getScore() {
        return calculateTotalAndSoftAces()[0];
    }

    /**
     * Checks if the hand contains any soft aces (aces counted as 11).
     * @return true if there is at least one soft ace, false otherwise
     */
    public boolean hasSoftAces() {
        return calculateTotalAndSoftAces()[1] > 0;
    }

    /**
     * Checks if the hand contains exactly five cards.
     *
     * @return {@code true} if the hand has exactly five cards, {@code false} otherwise
     */
    public boolean hasFiveCards(){
        return cards.size() == 5;
    }

    @Override
    public String toString() {
        return cards.toString() + " (Score: " + getScore() + ")";
    }
}
