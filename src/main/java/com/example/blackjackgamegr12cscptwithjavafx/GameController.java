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




    // <editor-fold desc="FXML Components, such as buttons, images, labels etc.">
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
    @FXML private Label insuranceResultLabel;
    @FXML private Button casinoManagerButton;
    // </editor-fold>

    // Constants and variables
    public static final int DEFAULT_BET = 100;
    private int betAmount = DEFAULT_BET;
    private boolean doubledDown;
    private Game game;
    private String lastOutcome = "";
    private boolean loggedIn; // for casino manager login state

    @FXML
    public void initialize() {
        game = new Game();
        game.getPlayer().resetHand();
        game.getDealer().resetHand();
        betAmount = DEFAULT_BET;
        doubledDown = false;
        loggedIn = false;

        // Assign CSS classes for gold color
        moneyLabel.getStyleClass().add("money-label");
        betLabel.getStyleClass().add("bet-label");

        /* background cause java 17 is dumb */
        Image background = new Image(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/BJ_TABLE.png")).toExternalForm());
        table.setImage(background);

        updateUI();


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
            casinoManagerButton.setDisable(false); // allow casino manager to open console
            insuranceResultLabel.setText(""); // Clear insurance result label

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
            if (game.getPlayer().getMoney() < 50) {
                messageLabel.setText("Not enough money to start a new round! Contact the casino manager.");
            } else if (game.getPlayer().getMoney() < betAmount) {
                messageLabel.setText("Not enough money to bet $" + betAmount + "! Please change your bet amount.");
            }
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
        updateUI();
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
                lastOutcome = "You Busted! You lose."; // Set lastOutcome so endRound() displays it
                messageLabel.setText(lastOutcome);
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
        int net = game.getPlayer().getLastNetChange();
        String netChange = "Net Change: ";
        if (net > 0) {
            netChange += "+$" + net;
        } else if (net < 0) {
            netChange += "-$" + Math.abs(net);
        } else {
            netChange += "$0";
        }
        System.out.println("Net change: " + netChange);
        payoutOrLossLabel.setText(netChange);
        System.out.println(lastOutcome);
        messageLabel.setText(lastOutcome);
        dealButton.setDisable(false);
        hitButton.setDisable(true);
        standButton.setDisable(true);
        doubleButton.setDisable(true);
        revealDealerButton.setDisable(true);
        changeBetButton.setDisable(false);

        // Show insurance result in the new label
        if (game.getPlayer().getLastInsuranceChange() < 0) {
            insuranceResultLabel.setText("Insurance purchased: -$" + Math.abs(game.getPlayer().getLastInsuranceChange()));
        } else if (game.getPlayer().getLastInsuranceChange() > 0) {
            insuranceResultLabel.setText("Insurance paid out: +$" + game.getPlayer().getLastInsuranceChange());
        } else {
            insuranceResultLabel.setText("");
        }

        System.out.println("End of round. Player money: $" + game.getPlayer().getMoney());

        // Update money display!!
        updateUI();
    }

    @FXML
    private void showCasinoManagerPopup(){

        System.out.println("button pressed, showing console dialog");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Console Casino Manager");
        dialog.setHeaderText("Enter Console Command: ");
        dialog.setContentText(">>> ");
        /* change the icon in the middle */
        ImageView icon = new ImageView();

        icon.setFitWidth(80);  // adjust size as needed
        icon.setFitHeight(40);
        dialog.setGraphic(icon);


        // make the background of the dialog box look nice with a background and font and stuff
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/styles.css")).toExternalForm());
        // Apply the console-log style class
        dialog.getDialogPane().getStyleClass().add("console-log");
        dialog.getEditor().getStyleClass().add("console-log");

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


        while (true) {
            String prompt = "";
            var result = dialog.showAndWait();
            if (result.isPresent()) {
                String input = result.get();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting console...");
                    dialog.close();
                    break;
                } else if (!input.trim().isEmpty()) {
                    prompt = handleConsoleCommand(input.trim());
                } else {
                    messageLabel.setText("No command entered.");
                }
                dialog.getEditor().clear();
            } else {
                // User clicked Cancel or closed the dialog
                System.out.println("Dialog closed by user.");
                dialog.close();
                break;
            }
            dialog.setHeaderText("Casino Manager Console\n" + prompt);
        }
    }

    private boolean login(String password){
        return "casino123".equals(password); // Simple hardcoded password for demo purposes
    }

    private String handleConsoleCommand(String command) {
        // Handle the console command here
        // For now, just print it to the console
        System.out.println("Console Command: " + command);

        String[] parts = command.split(" ");
        // You can implement specific commands here, e.g.:
        if (!command.strip().equals("help") && !loggedIn){

            if (parts[0].equalsIgnoreCase("login")){
                // Simple login check, in a real application you would check against a database or secure storage
                if (parts.length > 1 && login(parts[1])) {
                    loggedIn = true;
                    System.out.println("logged in as Casino Manager");
                    return "Logged in as Casino Manager.";
                } else {
                    return "Invalid Login Credentials. Please try again.";
                }
            } else {

                System.out.println("Not logged in, need to log in first to run command");
                return "Please log in first using 'login <password>'.";
            }
        }

        switch (parts[0].toLowerCase()){
            case "logout":
                loggedIn = false;
                return "Logged out from Casino Manager.";
            case "reset":
                initialize();

                // cause on reset the logged in state is reset to false, we dont want to log out yet
                loggedIn = true;
                return "Game has been reset to initial state.";
            case "addmoney":
                if (parts.length > 1) {
                    try {
                        int moneyToAdd = Integer.parseInt(parts[1]);
                        game.getPlayer().addMoney(moneyToAdd);
                        String message = "Added $" + moneyToAdd + " to player's money.";
                        System.out.println(message);
                        updateUI();
                        return message;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid money amount. Please enter a valid number.");
                        return ("Invalid money amount. Please enter a valid number.");
                    }
                } else {
                    System.out.println("Usage: addmoney <amount>");
                    return ("Usage: addmoney <amount>");
                }
            case "setmoney":
                if (parts.length > 1) {
                    try {
                        int newMoney = Integer.parseInt(parts[1]);
                        game.getPlayer().setMoney(newMoney);
                        String message = "Money set to $" + newMoney;
                        updateUI();
                        System.out.println(message);
                        return message;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid money amount. Please enter a valid number.");
                        return ("Invalid money amount. Please enter a valid number.");
                    }
                } else {
                    System.out.println("Usage: setmoney <amount>");
                    return ("Usage: setmoney <amount>");
                }
            case "reshuffle":
                game.getDeck().generateDeck();
                String message = "Deck reshuffled.";
                updateUI();
                System.out.println(message);
                return message;

            case "help":
                String helpText = """
                        Available commands:
                        reset - Resets the game to initial state.
                        setmoney <amount> - Sets the player's money to the specified amount.
                        reshuffle - Reshuffles the deck of cards.
                        login <password> - Logs in as Casino Manager.
                        logout - Logs out from Casino Manager.
                        help - Shows this help message.""";

                javafx.scene.control.Dialog<Void> helpDialog = new javafx.scene.control.Dialog<>();
                helpDialog.setTitle("Help");
                helpDialog.setHeaderText("Console Commands Help");

                javafx.scene.control.TextArea helpArea = new javafx.scene.control.TextArea(helpText);
                helpArea.setEditable(false);
                helpArea.setWrapText(true);
                helpArea.setPrefWidth(600);
                helpArea.setPrefHeight(300);
                helpArea.getStyleClass().add("console-log");

                javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(helpArea);
                content.setPrefSize(600, 300);
                helpDialog.getDialogPane().setContent(content);

                // Apply styles
                helpDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/styles.css")).toExternalForm());
                helpDialog.getDialogPane().getStyleClass().add("console-log");

                // Set custom icon
                Stage helpStage = (Stage) helpDialog.getDialogPane().getScene().getWindow();
                helpStage.getIcons().add(new Image(Objects.requireNonNull(
                        getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png")
                ).toExternalForm()));

                helpDialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
                helpDialog.showAndWait();
                break;
            default:
                return ("Invalid command. Please enter a valid command.");
        }

        return ""; // Return empty string if no specific message is needed
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
        // Clear previous cards
        playerCards.getChildren().clear();
        dealerCards.getChildren().clear();

        // Display player's cards
        for (Card card : game.getPlayer().getHand().getCards()) {
            ImageView cardImageView = new ImageView(card.getCardImage());
            cardImageView.setFitHeight(110); // Reduced from 150 to 110 (approximately 3/4 size)
            cardImageView.setFitWidth(75);   // Reduced from 100 to 75 (approximately 3/4 size)
            cardImageView.setPreserveRatio(true);
            playerCards.getChildren().add(cardImageView);
        }

        // Display dealer's cards
        boolean dealerRevealed = !(dealButton.isDisabled() && revealDealerButton.isDisabled()) || game.getPlayer().getHand().isBust();
        ArrayList<Card> dealerCardsList = game.getDealer().getHand().getCards();

        for (int i = 0; i < dealerCardsList.size(); i++) {
            Card card = dealerCardsList.get(i);
            boolean showCard = dealerRevealed || i == 0; // Show first card always, others only when revealed

            ImageView cardImageView = new ImageView(showCard ? card.getCardImage() : Card.getCardBackImage());
            cardImageView.setFitHeight(110); // Reduced from 150 to 110 (approximately 3/4 size)
            cardImageView.setFitWidth(75);   // Reduced from 100 to 75 (approximately 3/4 size)
            cardImageView.setPreserveRatio(true);

            dealerCards.getChildren().add(cardImageView);
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

    // todo: also, if time permits, change cards to images
    // todo: if time permits, implement splitting
}
