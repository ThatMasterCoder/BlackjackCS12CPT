package com.example.blackjackgamegr12cscptwithjavafx;


public class Player {
    private final Hand hand = new Hand();
    private int money = 1000;
    private boolean insurance = false;
    private int currentBet = 0;

    /**
     * Retrieves the player's hand.
     *
     * @return The player's hand, which is an instance of the {@code Hand} class.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Retrieves the amount of money the player currently has.
     *
     * @return The current amount of money the player possesses.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Allows the player to place a bet.
     * The bet amount must be less than or equal to the player's current money.
     * If the bet is successful, the player's money is reduced by the bet amount.
     *
     * @param amount The amount to bet.
     * @return {@code true} if the bet was successful, {@code false} otherwise.
     */
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

    /**
     * Resets the player's bet to zero, indicating a loss.
     * This method is called when the player loses a hand.
     */
    public void lose(){
        currentBet = 0;
    }

    /**
     * Pushes the bet back to the player, indicating a tie.
     * This method resets the current bet to zero and adds it back to the player's money.
     */
    public void push(){
        money += currentBet;
        currentBet = 0;
        // tie
    }


    /**
     * Allows the player to buy insurance against the dealer having a blackjack.
     * The cost of insurance is half of the current bet.
     * If the player has enough money, the insurance is purchased and the player's money is reduced accordingly.
     *
     * @return {@code true} if insurance was successfully purchased, {@code false} otherwise.
     */
    public boolean buyInsurance() {
        if (currentBet / 2 <= money) {
            money -= currentBet / 2;
            insurance = true;
            return true;
        } else return false;
    }


    /**
     * Checks if the player has purchased insurance.
     *
     * @return {@code true} if the player has insurance, {@code false} otherwise.
     */
    public boolean hasInsurance() {
        return insurance;
    }

    /**
     * Allows the player to double down their bet.
     * The player must have enough money to cover the current bet.
     * <p></p>
     * If successful, the player's money is reduced by the current bet,
     * and the current bet is doubled.
     *
     * @return {@code true} if the player successfully doubles down, {@code false} otherwise.
     */
    public boolean doubleDown(){
        if (money < currentBet) return false;
        else {
            money -= currentBet;
            /* actually increase the bet */
            currentBet *= 2;
        }
        return true;

    }


    /**
     * Resets the player's hand and insurance status.
     * This method clears all cards in the player's hand and resets the ace count
     * by invoking the {@code reset} method of the {@code Hand} class, preparing the player for a new game or round.
     * Additionally, the insurance status is set to {@code false}.
     *
     */
    public void resetHand() {
        hand.reset();
        insurance = false;
    }
}
