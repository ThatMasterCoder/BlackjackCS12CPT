package com.example.blackjackgamegr12cscptwithjavafx;

public class Game {
    private Deck deck = new Deck();
    private Player player = new Player();
    private Dealer dealer = new Dealer();

    public Game(){
        deck.generateDeck();
    }

    public boolean startNewRound(int bet){
        if (deck.lowOnCards()){
            deck.generateDeck();
        }

        player.resetHand();
        dealer.resetHand();

        if (!player.bet(bet)){
            System.out.println("Cannot bet, insufficient funds");
            return false;
        }


        System.out.println("player got: " + player.getHand().addCard(deck.drawCard()));
        System.out.println("dealer got: " + dealer.getHand().addCard(deck.drawCard()));
        System.out.println("player got: " + player.getHand().addCard(deck.drawCard()));
        System.out.println("dealer got: " + dealer.getHand().addCard(deck.drawCard()));
        return true;
    }

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


    /*
    might remove this later when done just in case
     */

    @Deprecated(forRemoval = true)
    public void playerStand(){
        System.out.println("player stands");
        dealer.play(deck);
    }

    public boolean buyInsurance(){
        System.out.println("Player tries to buy insurance");
        return player.buyInsurance();
    }

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
            return PlayerStatus.BUST;
        } else {
            return PlayerStatus.DOUBLE_DOWN;
        }

    }

    public String getOutcome(){
        int playerScore = player.getHand().getScore();
        int dealerScore = dealer.getHand().getScore();


        if (player.getHand().isBust()){
            player.lose();
            return "Player busts! Dealer wins.";
        } else if (player.getHand().hasFiveCards()){
            player.win(false);
            return "Player has 5 Cards! Player wins.";
        } else if (dealer.getHand().isBust()) {
            player.win(false);
            return "Dealer busts! Player wins!";
        } else if (player.getHand().isBlackjack() && dealer.hasBlackjack()) {
            player.push();
            return "Both have Blackjack. Push.";
        } else if (player.getHand().isBlackjack()) {
            player.win(true);
            return "Blackjack! Player wins!";
        } else if (dealer.hasBlackjack()) {
            if (player.hasInsurance()) {
                player.push(); // insurance pays 2:1
                return "Dealer had Blackjack. Insurance pays.";
            }
            player.lose();
            return "Dealer has Blackjack. Player loses.";
        } else if (playerScore > dealerScore) {
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
