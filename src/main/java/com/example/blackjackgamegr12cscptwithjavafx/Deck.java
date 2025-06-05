package com.example.blackjackgamegr12cscptwithjavafx;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    private void fillSortedDeck(){
        cards.clear();
        final String[] suits = {Suit.Club, Suit.Diamond, Suit.Heart, Suit.Spade};
        final String[] faceCardRanks = {"J", "Q", "K"};
        for (int decks = 0; decks < 8; decks++) {
            for (int i = 0; i < suits.length; i++) {
                for (int j = 1; j <= 13; j++) {
                    if (j == 1) {
                        cards.add(new Ace(new Suit(suits[i])));
                    } else if (j > 10) {
                        cards.add(new FaceCard(faceCardRanks[j - 11], new Suit(suits[i])));
                    } else {
                        cards.add(new Card(j, Integer.toString(j), new Suit(suits[i])));
                    }
                }
            }
        }

    }

    public boolean lowOnCards(){
        return cards.size() < 20;
    }

    private void shuffleDeck(){
        Collections.shuffle(cards);
    }

    public void addCard(Card card){
        System.out.println("Add card is active");
        cards.add(card);
    }
    public void generateDeck(){
        fillSortedDeck();
        shuffleDeck();
    }

    public ArrayList<Card> getDeck(){
        return cards;
    }

    public Card drawCard(){
        return cards.remove(cards.size()-1); // draw the last card
    }
}
