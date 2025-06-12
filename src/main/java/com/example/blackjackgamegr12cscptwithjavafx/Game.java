package com.example.blackjackgamegr12cscptwithjavafx;

/**
 * The {@code Game} class simulates a simplified card game. It manages the core
 * functionality of the game, including player and dealer interactions, betting,
 * and determining outcomes. The game uses a deck of cards and involves separate
 * roles for the Player and the Dealer.
 */
public class Game {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;

    /**
     * Represents the main class for managing the game.
     * This constructor initializes a new game instance by generating and shuffling a fresh deck of cards.
     * It prepares the deck to be used throughout the game.
     */
    public Game(){
        deck = new Deck();
        player = new Player();
        dealer = new Dealer();
        deck.generateDeck();
    }

    /**
     * Initiates a new round in the game. The method ensures that the deck has
     * enough cards, resets both the player's and dealer's hands, processes the
     * player's bet, and deals two initial cards to both the player and the dealer.
     *
     * @param bet The amount to be bet by the player at the start of the round.
     * @return {@code true} if the round was successfully started;
     *         {@code false} if the player does not have sufficient funds to place the bet.
     */
    public String startNewRound(int bet){
        if (deck.lowOnCards()){
            deck.generateDeck();
            return "Deck was low on cards, reshuffled.";
        }

        player.resetHand();
        dealer.resetHand();

        if (!player.bet(bet)){
            System.out.println("Cannot bet, insufficient funds");
            return "Cannot bet, insufficient funds";
        }


        System.out.println("player got: " + player.getHand().addCard(deck.drawCard()));
        System.out.println("dealer got: " + dealer.getHand().addCard(deck.drawCard()));
        System.out.println("player got: " + player.getHand().addCard(deck.drawCard()));
        System.out.println("dealer got: " + dealer.getHand().addCard(deck.drawCard()));
        return "play";
    }

    /**
     * Processes the player's decision to hit in the game, adding a new card to the player's hand
     * and checking the resulting state of the hand.
     *</p>
     * The method evaluates the player's hand after drawing a card, and can result in one of the following states:
     * - {@link PlayerStatus#CONTINUE}: If the player's score is less than 21 and does not meet any special conditions.
     * - {@link PlayerStatus#BUST}: If the player's hand exceeds a score of 21.
     * - {@link PlayerStatus#TWENTY_ONE}: If the player's hand reaches exactly a score of 21.
     * - {@link PlayerStatus#FIVE_CARDS}: If the player holds five cards without exceeding a score of 21.
     * <p>
     * If the player's hand exceeds 21 (bust), their current bet is lost.
     * This method assumes the deck and player's hand are properly initialized.
     *
     * @return The current {@link PlayerStatus}, representing the state of the player's hand after hitting.
     */
    public PlayerStatus playerHit(){
        System.out.println("Player hits");
        System.out.println("Player gets: " + player.getHand().addCard(deck.drawCard()));
        System.out.println("Player score: "+ player.getHand().getScore());
        if (player.getHand().isBust()) {
            player.lose();
            System.out.println("player busts");
            return PlayerStatus.BUST;
        } else if (player.getHand().getScore() == 21){
            System.out.println("player has 21");
            return PlayerStatus.TWENTY_ONE;
        } else if (player.getHand().hasFiveCards()) {
            System.out.println("player has 5 cards");
            return PlayerStatus.FIVE_CARDS;
        } else {
            return PlayerStatus.CONTINUE;
        }
    }





    /**
     * Attempts to purchase insurance for the player in the blackjack game.
     * This method delegates the operation to the player's `buyInsurance` method.
     * Insurance is a side bet option available in blackjack when the dealer's face-up card is an Ace,
     * allowing the player to bet on the possibility of the dealer having a blackjack.
     *
     * @return {@code true} if the player successfully buys insurance; {@code false} otherwise.
     */
    public boolean buyInsurance(){
        System.out.println("Player tries to buy insurance");
        return player.buyInsurance();
    }

    /**
     * Allows the player to double down in the game.
     * This method checks if the player can double down based on their current hand and available funds.
     * If successful, it adds a new card to the player's hand and updates the game state accordingly.
     *
     * @return {@link PlayerStatus#DOUBLE_DOWN} if the player successfully doubles down,
     *         {@link PlayerStatus#BUST} if the player's hand exceeds 21 after doubling down,
     *         or {@link PlayerStatus#CONTINUE} if the player cannot double down.
     */
    public PlayerStatus doubleDown(){
        System.out.println("player tries to double down");
        boolean canDouble = player.doubleDown();
        if (!canDouble){
            System.out.println("player cannot double");
            return PlayerStatus.CONTINUE;
        }

        System.out.println("player doubles down");

        player.getHand().addCard(deck.drawCard());

        if (player.getHand().isBust()){
            player.setLastNetChange(-player.getCurrentBet()); // player loses the doubled bet
            return PlayerStatus.BUST;
        } else {
            return PlayerStatus.DOUBLE_DOWN;
        }

    }

    /**
     * Determines the outcome of the game based on the player's and dealer's hands.
     * This method evaluates the scores of both hands, checks for special conditions like blackjack,
     * busts, and five-card hands, and updates the player's status accordingly.
     *
     * @return A string message indicating the outcome of the game, such as who wins or if there is a push.
     */
    public String getOutcome(){
        int playerScore = player.getHand().getScore();
        int dealerScore = dealer.getHand().getScore();


        if (player.getHand().isBust()) {
            player.lose();
            return "Player busts! Dealer wins.";
        }

// Five-card Charlie win
        else if (player.getHand().hasFiveCards()) {
            player.win(false);
            return "Player has 5 cards! Player wins!";
        }

// Dealer bust
        else if (dealer.getHand().isBust()) {
            player.win(false);
            return "Dealer busts! Player wins!";
        }

// Both have blackjack
        else if (player.getHand().isBlackjack() && dealer.hasBlackjack()) {
            if (player.hasInsurance()) {
                // refund insurance money because it wasn't needed
                player.refundInsurance();
            }
            player.push();
            return "Both have Blackjack. Push.";
        }

// Player has blackjack
        else if (player.getHand().isBlackjack()) {
            player.win(true);
            return "Blackjack! Player wins!";
        }

// Dealer has blackjack
        else if (dealer.hasBlackjack()) {
            if (player.hasInsurance()) {
                if (player.getHand().isBlackjack()) {
                    // Both have blackjack: push main bet, refund insurance
                    player.refundInsurance();
                    player.push();
                    return "Both have Blackjack. Push.";
                } else {
                    // Dealer has blackjack, player does not: lose main bet, win insurance
                    player.winInsurance();
                    player.lose();
                    return "Dealer has Blackjack. Insurance pays.";
                }
            } else {
                player.lose();
                return "Dealer has Blackjack. Player loses.";
            }
        }

// Compare final scores
        else if (playerScore > dealerScore) {
            player.win(false);
            return "Player wins!";
        } else if (playerScore < dealerScore) {
            player.lose();
            return "Dealer wins.";
        } else {
            player.push();
            return "Push.";
        }

    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }
}
