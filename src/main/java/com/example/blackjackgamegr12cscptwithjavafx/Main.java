package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.generateDeck();
        ArrayList<Card> cards = deck.getDeck();

        for (Card card : cards) {
            System.out.println(card);
        }
    }
}