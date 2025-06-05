package com.example.blackjackgamegr12cscptwithjavafx;

public record Suit(String suit) {

    public static final String Spade = "♠";
    public static final String Heart = "♥";
    public static final String Club = "♣";
    public static final String Diamond = "♦";

    public Suit(String suit) {
        if (suit.equalsIgnoreCase("spade") || suit.equals(Spade)) {
            this.suit = Suit.Spade;
        } else if (suit.equalsIgnoreCase("heart") || suit.equals(Heart)) {
            this.suit = Suit.Heart;
        } else if (suit.equalsIgnoreCase("club") || suit.equals(Club)) {
            this.suit = Suit.Club;
        } else if (suit.equalsIgnoreCase("diamond") || suit.equals(Diamond)) {
            this.suit = Suit.Diamond;
        } else {
            throw new IllegalArgumentException("Invalid suit");
        }
    }

    @Override
    public String toString() {
        return suit;
    }
}
