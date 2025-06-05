package com.example.blackjackgamegr12cscptwithjavafx;



public class Dealer {
    private Hand hand = new Hand();

    public Hand getHand() {
        return hand;
    }

/*
    public void play(Deck deck){
        while (hand.getScore() < 17) {

            Card card = deck.drawCard();
            hand.addCard(card);
            System.out.println("Dealer draws: " + card); // for testing
        }
    }

 */
    public void play(Deck deck){
        throw new RuntimeException("Not implemented, no longer in use");
    }

    public void resetHand(){
        hand.reset();
    }

    public boolean showsAce() {
        return !(hand.getCards().isEmpty())  && hand.getCards().get(0) instanceof Ace;
    }

    public boolean hasBlackjack(){
        return hand.isBlackjack();
    }
}
