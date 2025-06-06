package com.example.blackjackgamegr12cscptwithjavafx;

/**
 * Represents a face card in a standard deck of playing cards.
 * Face cards include Jack, Queen, and King, all of which have a value of 10.
 */
public class FaceCard extends Card{
    public FaceCard(String rank, Suit suit){
        super(10, rank, suit);
    }
}
