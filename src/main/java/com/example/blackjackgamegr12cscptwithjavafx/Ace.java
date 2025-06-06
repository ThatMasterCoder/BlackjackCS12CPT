package com.example.blackjackgamegr12cscptwithjavafx;

/**
 * This class extends the Card class; has the card value 11.
 * Functionality is such to allow Card<?> instanceof Ace checking.
 */
public class Ace extends Card {

    /**
     * Constructor for the Ace Class. (Value 11)
     * @param suit The suit of which the ace is.
     */
    public Ace(Suit suit){
        super(11, "A", suit);
    }
}
