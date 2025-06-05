package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;

public class Hand {

    private final ArrayList<Card> cards = new ArrayList<Card>();
    private int aces = 0;

    public Card addCard(Card card){
        cards.add(card);

        if (card instanceof Ace) {
            aces++;
        }
        return card;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public int getScore(){
        int total = 0;
        int softAces = aces; // Track how many aces are currently counting as 11

        // First pass - count all cards with aces as 11
        for (Card card : cards) {
            total += card.getCardValue();
        }

        // Second pass - convert aces from 11 to 1 if needed
        while (total > 21 && softAces > 0) {
            total -= 10; // Convert an ace from 11 to 1
            softAces--;
        }

        return total;
    }

    public boolean isBlackjack(){
        return cards.size() == 2 && getScore() == 21;
    }

    public boolean isBust(){
        return getScore() > 21;
    }

    public void reset(){
        cards.clear();
        aces = 0;
    }

    public boolean hasSoftAces() {
        int total = 0;
        int softAces = aces;

        for (Card card : cards) {
            total += card.getCardValue();
        }

        while (total > 21 && softAces > 0) {
            total -= 10;
            softAces--;
        }

        return softAces > 0;
    }

    public boolean hasFiveCards(){
        return cards.size() == 5;
    }

    @Override
    public String toString() {
        return cards.toString() + " (Score: " + getScore() + ")";
    }
}
