package com.example.blackjackgamegr12cscptwithjavafx;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Objects;

public class GameController {




    // <editor-fold desc="FXML Components">
    @FXML private ImageView table;
    @FXML private Label messageLabel;
    @FXML private Button revealDealerButton;
    @FXML private HBox dealerCards;
    @FXML private HBox playerCards;
    @FXML private Button dealButton;
    @FXML private Button hitButton;
    @FXML private Button standButton;
    @FXML private Button doubleButton;
    @FXML private Button getInsuranceButton;
    @FXML private Label dealerScoreLabel;
    @FXML private Label playerScoreLabel;
    @FXML private Button changeBetButton;
    @FXML private Label moneyLabel;
    @FXML private Label betLabel;
    @FXML private Label insuranceLabel;
    @FXML private Label payoutOrLossLabel;
    // </editor-fold>

    // Constants and variables
    public static final int DEFAULT_BET = 100;
    private int betAmount = DEFAULT_BET;
    private boolean doubledDown;
    private Game game;
    private String lastOutcome = "";

    @FXML
    public void initialize() {
        game = new Game();
        game.getPlayer().resetHand();
        game.getDealer().resetHand();
        betAmount = DEFAULT_BET;
        doubledDown = false;

        // Assign CSS classes for gold color
        moneyLabel.getStyleClass().add("money-label");
        betLabel.getStyleClass().add("bet-label");

        /* background cause java 17 is dumb */
        Image background = new Image(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/BJ_TABLE.png")).toExternalForm());
        table.setImage(background);

        updateUI();

        // case work

        /*

        Card[] cards = {new Card(4, "4", new Suit("Spade")),new Ace(new Suit("Heart")),
        new FaceCard("J", new Suit("Diamond")),
        new Card(2, "2", new Suit("Heart")),
        new Card(8, "8", new Suit("Spade")),
        new FaceCard("K", new Suit("Diamond"))};

        for (int i = cards.length -1; i >= 0; i--) {
            game.getDeck().addCard(cards[i]);
        }
         */
    }

    @FXML
    private void showBetPopup(){
        TextInputDialog dialog = new TextInputDialog(String.valueOf(betAmount));
        dialog.setTitle("Set new Bet");
        dialog.setHeaderText("Enter new bet amount (Min bet is $50): ");
        dialog.setContentText("Bet:");
        /* change the icon in the middle */
        ImageView icon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png"))
                        .toExternalForm()
        ));
        icon.setFitWidth(40);  // adjust size as needed
        icon.setFitHeight(40);
        dialog.setGraphic(icon);


        // make the background of the dialog box look nice with a background and font and stuff
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/styles.css")).toExternalForm());

        /* fix the ugly question mark icon and replace it with my own poker chip icon
        basically I need to make a window OWNER after I initialize but before I show
         */

        Window owner = messageLabel.getScene().getWindow();
        dialog.initOwner(owner);

        // setting the custom icon
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull( // would rather NullPointerException than silent fail...
                getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png")
        ).toExternalForm()));



        dialog.showAndWait().ifPresent(input -> {
            try{
                int inputBet = Integer.parseInt(input);
                if (inputBet >= 50 && inputBet <= game.getPlayer().getMoney()) {
                    betAmount = inputBet;
                    messageLabel.setText("New bet has been successfully set!");
                    updateUI();
                } else {
                    messageLabel.setText("Bet must be between $50 and your current money ($" + game.getPlayer().getMoney() + ").");
                }
            } catch (NumberFormatException e){
                messageLabel.setText("Please enter a number");
            }
        });

    }

    @FXML
    private void onDeal() {
        if (game.startNewRound(betAmount)){
            System.out.println("New Round");
            messageLabel.setText("Welcome to Blackjack! Use the buttons to play.");
            dealButton.setDisable(true);
            hitButton.setDisable(false);
            standButton.setDisable(false);
            getInsuranceButton.setDisable(true);
            changeBetButton.setDisable(true);

            doubleButton.setDisable(game.getPlayer().getMoney() < betAmount);

            doubledDown = false; // reset double down state
            revealDealerButton.setDisable(true);
            updateUI();

            if (game.getDealer().showsAce()){
                messageLabel.setText("Insurance pays 2 to 1. Would you like insurance?");
                getInsuranceButton.setDisable(false);
            }

            if (game.getPlayer().getHand().isBlackjack()) {
                messageLabel.setText("Blackjack Won!");
                handleBlackjack();
                endRound(); // Only call endRound() here, not in handleBlackjack()

            }
        } else {
            messageLabel.setText("Not enough money to bet! Contact the casino manager.");
            System.out.println("Broke, cant bet amount");
        }
    }

    @FXML
    private void onInsurance(){
        boolean insuranceBought = game.buyInsurance();
        getInsuranceButton.setDisable(true); // cant buy insurance again...
        if (insuranceBought){
            messageLabel.setText("Insurance bought successfully!");
        } else {
            messageLabel.setText("Not enough money to buy insurance!");
        }
    }

    private void handleBlackjack() {
        lastOutcome = game.getOutcome();
        messageLabel.setText(lastOutcome);
        // endRound(); // Removed to avoid double-calling getOutcome()
    }

    @FXML
    private void onHit() {
        PlayerStatus status = game.playerHit();
        getInsuranceButton.setDisable(true); // cant buy insurance after the first two cards
        updateUI();

        System.out.println(status);

        switch (status){
            case BUST:
                messageLabel.setText("You Busted! You lose.");
                endRound();
                break;
            case TWENTY_ONE:
                messageLabel.setText("You have 21! ");
                standButton.fire();
                break;
            case FIVE_CARDS:
                messageLabel.setText("You have 5 cards! (Five-card Charlie)");
                standButton.fire();
                break;
            default:
                messageLabel.setText("Hit");
                doubleButton.setDisable(true); // cant double after first two cards
                break;
        }
    }

    @FXML
    private void onStand() {
        messageLabel.setText("Dealer's turn... click to reveal cards.");
        hitButton.setDisable(true);
        standButton.setDisable(true);
        doubleButton.setDisable(true);
        revealDealerButton.setDisable(false);
        updateUI();
    }

    @FXML
    private void onDoubleDown() {
        PlayerStatus status = game.doubleDown();
        doubledDown = true; // Set double down state
        updateUI();
        System.out.println("player doubles down");


        if (status == PlayerStatus.BUST) {
            messageLabel.setText("You Busted after doubling down! You lose.");
            endRound();
        } else { // not having enough money to double down is handled in onDeal()
            messageLabel.setText("Doubled down! Dealer's turn... click to reveal cards.");
            hitButton.setDisable(true);
            standButton.setDisable(true);
            doubleButton.setDisable(true);
            revealDealerButton.setDisable(false); // Enable reveal button
        }
    }

    private void endRound(){
        String netChange = "Net Change: ";
        if (game.getPlayer().getLastNetChange() > 0) {
            netChange += "+$" + game.getPlayer().getLastNetChange();
        } else if (game.getPlayer().getLastNetChange() < 0) {
            netChange += "-$" + Math.abs(game.getPlayer().getLastNetChange());
        } else {
            netChange += "$0";
        }
        payoutOrLossLabel.setText(netChange);
        System.out.println(lastOutcome);
        messageLabel.setText(lastOutcome);
        dealButton.setDisable(false);
        hitButton.setDisable(true);
        standButton.setDisable(true);
        doubleButton.setDisable(true);
        revealDealerButton.setDisable(true);
        changeBetButton.setDisable(false);

        // Update money display!!
        updateUI();
    }

    @FXML
    private void onRevealDealerCard() {
        Hand dealerHand  = game.getDealer().getHand();
        if (dealerHand.getScore() < 17 || (dealerHand.getScore() == 17 && dealerHand.hasSoftAces())) {
            dealerHand.addCard(game.getDeck().drawCard());
        }

        updateUI();

        if (game.getDealer().getHand().getScore() >= 17) {
            lastOutcome = game.getOutcome();
            messageLabel.setText(lastOutcome);
            revealDealerButton.setDisable(true);
            endRound();
        }
    }
    private void updateUI() {
        // Clear cards
        playerCards.getChildren().clear();
        dealerCards.getChildren().clear();

        // Show player cards
        for (Card card : game.getPlayer().getHand().getCards()) {
            Label cardLabel = new Label(card.toString());
            cardLabel.setStyle("-fx-border-color: black; -fx-padding: 5; -fx-background-color: white;");
            playerCards.getChildren().add(cardLabel);
        }

        // Show dealer cards - handle empty hand case... just in case :)
        boolean dealerRevealed = !(dealButton.isDisabled() && revealDealerButton.isDisabled()) || game.getPlayer().getHand().isBust();
        ArrayList<Card> dealerCardsList = game.getDealer().getHand().getCards();

        for (int i = 0; i < dealerCardsList.size(); i++) {
            Card card = dealerCardsList.get(i);
            boolean showCard = dealerRevealed || i == 0; // Show first card always, others only when revealed
            Label cardLabel = new Label(showCard ? card.toString() : "[Hidden]");
            cardLabel.setStyle("-fx-border-color: black; -fx-padding: 5; -fx-background-color: " +
                    (showCard ? "lightgray" : "white") + ";");
            dealerCards.getChildren().add(cardLabel);
        }

        // Update score labels
        int playerScore = game.getPlayer().getHand().getScore();
        String playerScoreText = "Player: " + playerScore;
        if (game.getPlayer().getHand().hasSoftAces() && playerScore <= 21) {
            playerScoreText += " (Soft)";
        }
        playerScoreLabel.setText(playerScoreText);


        String dealerScoreText;
        if (dealerCardsList.isEmpty()) {
            dealerScoreText = "Dealer: 0";
        } else if ((dealButton.isDisabled() && revealDealerButton.isDisabled()) && !game.getPlayer().getHand().isBust()) {
            // Before reveal, only show first card's value
            System.out.println("Hide cards");
            int visibleValue = dealerCardsList.get(0).getCardValue();
            dealerScoreText = "Dealer: " + visibleValue + " + ?";
        } else {
            System.out.println("Show cards");
            // After reveal or if player busted
            int dealerScore = game.getDealer().getHand().getScore();
            dealerScoreText = "Dealer: " + dealerScore;
            if (game.getDealer().getHand().hasSoftAces() && dealerScore <= 21) {
                dealerScoreText += " (Soft)";
            }
        }
        dealerScoreLabel.setText(dealerScoreText);

        // Update money and bet labels
        moneyLabel.setText("Money: $" + game.getPlayer().getMoney());
        String betLabelText = "Bet: $" + betAmount;
        if (doubledDown){
            betLabelText += " x2 (Doubled Down)";
        }
        betLabel.setText(betLabelText);
        insuranceLabel.setText("Insurance: " + (game.getPlayer().hasInsurance() ? "Yes" : "No"));
    }

    // Add helper methods like updatePlayerUI(), updateDealerUI() later
    // todo: Insurance, handleBlackjack, splitting if time permits...
    // todo: also, if time permits, change cards to images
}
