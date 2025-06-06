package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;

public class Main {
    /**
     * Main method to run the Blackjack game.
     * This method initializes a deck of cards and prints each card in the deck.
     * Note: This is a placeholder main method and does not implement the game logic.
     */
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.generateDeck();
        ArrayList<Card> cards = deck.getDeck();

        for (Card card : cards) {
            System.out.println(card);
        }
    }
}