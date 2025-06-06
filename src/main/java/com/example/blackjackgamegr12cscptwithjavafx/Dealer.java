package com.example.blackjackgamegr12cscptwithjavafx;


/** * The Dealer class represents the dealer in a blackjack game.
 * It manages the dealer's hand and provides methods to play the dealer's turn.
 */
public class Dealer {
    private final Hand hand = new Hand();


    public Hand getHand() {
        return hand;
    }

    /**
     * Resets the dealer's hand for a new game.
     */
    public void resetHand(){
        hand.reset();
    }

    /**
     * Checks if the dealer's face-up card is an Ace.
     * <p>
     * The method examines the first card in the dealer's hand if it exists
     * and determines whether it is an instance of the Ace class.
     *
     * @return {@code true} if the dealer's hand is not empty and the first card is an Ace;
     *         {@code false} otherwise.
     */
    public boolean showsAce() {
        return !(hand.getCards().isEmpty())  && hand.getCards().get(0) instanceof Ace;
    }

    /**
     * Determines if the dealer has a blackjack.
     * <p>
     * A blackjack is defined as having exactly two cards with a total value of 21.
     *
     * @return {@code true} if the dealer's hand is a blackjack; {@code false} otherwise.
     */
    public boolean hasBlackjack(){
        return hand.isBlackjack();
    }
}
