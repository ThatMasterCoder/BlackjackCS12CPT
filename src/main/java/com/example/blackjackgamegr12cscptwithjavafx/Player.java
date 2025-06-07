package com.example.blackjackgamegr12cscptwithjavafx;


public class Player {
    private final Hand hand = new Hand();
    private int money = 1000;
    private boolean insurance = false;
    private int currentBet = 0;
    private int lastNetChange = 0;

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
        if (amount <= money){
            currentBet = amount;
            money -= amount;
            return true;
        } else return false;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * Pays the player on a win.
     * @param isBlackjack {@code true} if it is a blackjack win, {@code false} if not
     */
    public void win(boolean isBlackjack){
        System.out.println("PLAYER WINS");
        // we need to know if bj to pay 3:2 instead of 2:1
        if (isBlackjack){
            money += (lastNetChange = (int)(currentBet * 2.5));
            System.out.println("PLAYER WINS WITH BLACKJACK");
        } else {
            money += (lastNetChange = (currentBet * 2));
        }
        lastNetChange -= currentBet; // subtract the bet amount from the win amount
    }

    /**
     * Resets the player's bet to zero, indicating a loss.
     * This method is called when the player loses a hand.
     */
    public void lose(){
        lastNetChange = -currentBet;
    }

    /**
     * Pushes the bet back to the player, indicating a tie.
     * This method resets the current bet to zero and adds it back to the player's money.
     */
    public void push(){
        money += currentBet;
        lastNetChange = 0; // reset last net change to 0 on a push, as it is neither a win nor a loss
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
     * Retrieves the last net change in the player's money.
     * This value is updated after each win, loss, or push.
     *
     * @return The last net change in the player's money.
     */
    public int getLastNetChange(){
        return lastNetChange;
    }

    public void setLastNetChange(int lastNetChange){
        this.lastNetChange = lastNetChange;
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
        currentBet = 0; // reset current bet
        insurance = false;
    }


    public void refundInsurance() {
        if (insurance) {
            money += currentBet / 2; // refund the insurance cost
            insurance = false; // reset insurance status
        }
    }

    /**
     * Pays out the insurance bet to the player.
     * This method is called when the dealer has a blackjack and the player has purchased insurance.
     * The player receives back their insurance bet, which is half of the current bet,
     * effectively paying 2:1 on the insurance bet.
     */
    public void winInsurance() {
        money += currentBet; // insurance pays 2:1 on half the bet = 1x original bet
        insurance = false;
    }

}
