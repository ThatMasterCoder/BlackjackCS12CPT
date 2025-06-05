package com.example.blackjackgamegr12cscptwithjavafx;

public class Player {
    private Hand hand = new Hand();
    private int money = 10000;
    private int currentBet = 0;
    private boolean insurance = false;

    public Hand getHand() {
        return hand;
    }

    public int getMoney() {
        return money;
    }

    public int getCurrentBet() {
        return currentBet;

    }

    public boolean bet(int amount){
        if (amount <= money ){
            currentBet = amount;
            money -= amount;
            return true;
        } else return false;
    }

    public void win(boolean isBlackjack){ // we need to know if bj to pay 3:2 instead of 2:1
        if (isBlackjack){
            money += (int)(currentBet * 2.5);
        } else {
            money += currentBet * 2;
        }
        currentBet = 0;
    }

    public void win(){
        win(false);
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
        }
        return true;

    }


    public void resetHand() {
        hand.reset();
        insurance = false;
    }
}
