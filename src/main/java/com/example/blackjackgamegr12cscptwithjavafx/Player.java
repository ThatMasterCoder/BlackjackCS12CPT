package com.example.blackjackgamegr12cscptwithjavafx;

public class Player {
    private final Hand hand = new Hand();
    private int money = 10000;
    private boolean insurance = false;
    private int currentBet = 0;

    public Hand getHand() {
        return hand;
    }

    public int getMoney() {
        return money;
    }

    public boolean bet(int amount){
        if (amount <= money ){
            currentBet = amount;
            money -= amount;
            return true;
        } else return false;
    }


    /**
     * Pays the player on a win.
     * @param isBlackjack {@code true} if it is a blackjack win, {@code false} if not
     */
    public void win(boolean isBlackjack){ // we need to know if bj to pay 3:2 instead of 2:1
        if (isBlackjack){
            money += (int)(currentBet * 2.5);
        } else {
            money += currentBet * 2;
        }
        currentBet = 0;
    }


    public void lose(){
        currentBet = 0;
    }

    public void push(){
        money += currentBet;
        currentBet = 0;
        // tie
    }

    public boolean buyInsurance() {
        if (currentBet / 2 <= money) {
            money -= currentBet / 2;
            insurance = true;
            return true;
        } else return false;
    }

    public boolean hasInsurance() {
        return insurance;
    }

    public boolean doubleDown(){
        if (money < currentBet) return false;
        else {
            money -= currentBet;
            /* actually increase the bet */
            currentBet *= 2;
        }
        return true;

    }


    public void resetHand() {
        hand.reset();
        insurance = false;
    }
}
