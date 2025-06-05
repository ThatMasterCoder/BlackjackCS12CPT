package com.example.blackjackgamegr12cscptwithjavafx;

public class Card {

    private int cardValue;
    private final String rank;
    private final Suit suit;

    public Card(int cardValue, String cardRank, Suit cardSuit) {
        this.cardValue = cardValue;
        this.rank = cardRank;
        this.suit = cardSuit;
    }

    public int getCardValue() {
        return cardValue;
    }

    protected void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    /*
    @Override
    public String toString() {
        return this.rank + "(" + cardValue + ") of " + this.suit;
    }
     */

    @Override
    public String toString() {
        return this.rank + " of " + this.suit;
    }
}
