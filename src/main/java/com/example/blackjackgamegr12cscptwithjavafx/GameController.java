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
    @FXML private Button rejectInsuranceButton;
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

    /**
     * Initializes the game controller, setting up the initial game state, UI components,
     * and event handlers. This method is called automatically by JavaFX when the FXML file
     * is loaded.
     */
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
/*
        game.getDeck().addCard(new FaceCard("K", new Suit(Suit.Club)));
        game.getDeck().addCard(new Card(4, "4", new Suit(Suit.Heart)));
        game.getDeck().addCard(new FaceCard("J",new Suit(Suit.Spade)));
        game.getDeck().addCard(new Ace(new Suit(Suit.Spade)));
        */



    }


    /**
     * Displays a dialog box that allows the player to set a new bet amount.
     * <p>
     * This method creates and configures a custom dialog box with the following features:
     * <ul>
     *   <li>Input validation to ensure the bet is within acceptable limits (minimum $50, maximum: player's current money)</li>
     *   <li>Custom styling with CSS for visual consistency with the game</li>
     *   <li>Custom icon (poker chip) replacing the default dialog icon</li>
     *   <li>Visual feedback through the message label showing the result of the bet change</li>
     * </ul>
     * </p>
     * The method handles three cases:
     * <ol>
     *   <li>Valid bet amount within acceptable range - updates the bet amount</li>
     *   <li>Invalid bet amount (outside range) - shows an error message</li>
     *   <li>Non-numeric input - shows an error message</li>
     * </ol>
     */
    @FXML
    private void showBetPopup(){
        // Create dialog with current bet as default value
        TextInputDialog dialog = new TextInputDialog(String.valueOf(betAmount));
        dialog.setTitle("Set new Bet");
        dialog.setHeaderText("Enter new bet amount (Min bet is $50): ");
        dialog.setContentText("Bet:");

        /* Replace the default icon with a custom poker chip icon */
        ImageView icon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png"))
                        .toExternalForm()
        ));
        icon.setFitWidth(40);  // Set icon dimensions
        icon.setFitHeight(40);
        dialog.setGraphic(icon);

        // Apply custom CSS styling to the dialog
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/styles.css")).toExternalForm());

        /* Fix the default question mark icon by setting the owner window and replacing with custom icon */
        Window owner = messageLabel.getScene().getWindow();
        dialog.initOwner(owner);

        // Set the custom icon for the dialog window
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png")
        ).toExternalForm()));

        // Process the user's input when they click OK
        dialog.showAndWait().ifPresent(input -> {
            try {
                // Parse the input to an integer
                int inputBet = Integer.parseInt(input);

                // Validate bet amount (minimum $50, maximum: player's current money)
                if (inputBet >= 50 && inputBet <= game.getPlayer().getMoney()) {
                    betAmount = inputBet;
                    messageLabel.setText("New bet has been successfully set!");
                    updateUI();
                } else {
                    // Show error for bet outside valid range
                    messageLabel.setText("Bet must be between $50 and your current money ($" + game.getPlayer().getMoney() + ").");
                }
            } catch (NumberFormatException e) {
                // Show error for non-numeric input
                messageLabel.setText("Please enter a number");
            }
        });
    }

    /**
     * Handles the "Deal" button click event to start a new round of Blackjack.
     * This method initializes the game state, deals cards to the player and dealer,
     * and updates the UI accordingly. It also manages special cases like calling for insurance
     * when the dealer shows an Ace.
     */
    @FXML
    private void onDeal() {
        String playResult = game.startNewRound(betAmount);
        if (playResult.equals("play")){ // no issues
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
                hitButton.setDisable(true);
                standButton.setDisable(true);
                doubleButton.setDisable(true);
                getInsuranceButton.setDisable(false);
                rejectInsuranceButton.setDisable(false);
            } else if (game.getDealer().hasBlackjack()){ // dealer no ace, but has blackjack
                standButton.fire();
            } else {
                messageLabel.setText("Hit or Stand?");
            }

            if (game.getPlayer().getHand().isBlackjack()) {
                messageLabel.setText("Blackjack Won!");
                handlePlayerBlackjack();
                endRound(); // Only call endRound() here, not in handleBlackjack()
            }
        } else if (playResult.equals("Deck was low on cards, reshuffled.")) {
            messageLabel.setText("Deck was low on cards, reshuffled. Please try again.");
            System.out.println("Deck was low on cards, reshuffled.");
        } else { // playResult is "Cannot bet, insufficient funds"
            if (game.getPlayer().getMoney() < 50) {
                messageLabel.setText("Not enough money to start a new round! Contact the casino manager.");
            } else if (game.getPlayer().getMoney() < betAmount) {
                messageLabel.setText("Not enough money to bet $" + betAmount + "! Please change your bet amount.");
            }
            System.out.println("Broke, cant bet amount");
        }
    }

    /**
     * Updates the UI components to reflect the current game state, including displaying
     * the player's and dealer's hands, scores, and other relevant information.
     */
    @FXML
    private void onInsurance(){
        boolean insuranceBought = game.buyInsurance();
        getInsuranceButton.setDisable(true); // cant buy insurance again...
        rejectInsuranceButton.setDisable(true);
        if (insuranceBought){
            messageLabel.setText("Insurance bought successfully!");
            if (game.getDealer().hasBlackjack()){
                // Dealer has blackjack, end the round immediately
                lastOutcome = game.getOutcome(); // Get the outcome which handles insurance payout
                revealDealerButton.setDisable(true); // No need to press reveal
                endRound(); // End the round directly
            } else {
                // Continue the game if dealer doesn't have blackjack
                hitButton.setDisable(false);
                standButton.setDisable(false);
                doubleButton.setDisable(game.getPlayer().getMoney() < betAmount);
                messageLabel.setText("Insurance bought. Dealer doesn't have Blackjack.");
            }
        } else {
            messageLabel.setText("Not enough money to buy insurance!");
        }
        updateUI();
    }


    /**
     * Processes the player's decision to reject insurance when the dealer shows an ace.
     * <p>
     * This method handles two different scenarios based on whether the dealer has blackjack:
     * <ol>
     *   <li><b>Dealer has blackjack:</b> The round ends immediately, revealing the dealer's
     *       hand and determining the outcome. The player loses their bet since they rejected
     *       the insurance that would have covered this loss.</li>
     *   <li><b>Dealer doesn't have blackjack:</b> The game continues normally, allowing the
     *       player to hit, stand, or double down. This validates the player's decision to
     *       decline insurance as the optimal strategy.</li>
     * </ol>
     * </p>
     * After processing the insurance decision, the UI is updated to reflect the current
     * game state, including any changes to the player's money, bet, and available actions.
     */
    @FXML
    private void onRejectInsurance() {
        // Disable insurance buttons to prevent multiple selections
        getInsuranceButton.setDisable(true);
        rejectInsuranceButton.setDisable(true);

        if (game.getDealer().hasBlackjack()){
            // Dealer has blackjack, end the round immediately
            lastOutcome = game.getOutcome(); // Get the outcome which handles insurance payout
            revealDealerButton.setDisable(true); // No need to press reveal
            messageLabel.setText("Insurance Rejected. Dealer has Blackjack!");
            endRound(); // End the round directly
        } else {
            // Dealer doesn't have blackjack, continue playing
            hitButton.setDisable(false);
            standButton.setDisable(false);
            doubleButton.setDisable(game.getPlayer().getMoney() < betAmount);
            messageLabel.setText("Insurance rejected. Dealer does not have Blackjack.");
        }

        // Update the UI to reflect current game state
        updateUI();
    }


    /**
     * Handles the case when the player is dealt a blackjack (a natural 21 with the first two cards).
     * <p>
     * This method processes the outcome of the player having a blackjack by:
     * <ol>
     *   <li>Getting the game outcome from the game logic, which determines the appropriate
     *       payout (typically 3:2 on the original bet)</li>
     *   <li>Updating the message displayed to the player to show they've won with a blackjack</li>
     * </ol>
     * </p>
     * The actual game ending logic is handled by the caller (typically onDeal), which calls
     * endRound() after this method. This separation prevents double-calling getOutcome(),
     * which would incorrectly calculate payouts twice.
     *
     * @see Game#getOutcome()
     * @see #endRound()
     */
    private void handlePlayerBlackjack() {
        // Get the outcome from the game logic (typically blackjack pays 3:2)
        lastOutcome = game.getOutcome();
        // Display the outcome to the player
        messageLabel.setText(lastOutcome);
        // endRound() is called by the parent method to avoid double-calling getOutcome()
    }


    /**
     * Handles the action when a player chooses to hit (take another card).
     * <p>
     * This method processes the player's request for another card by:
     * <ol>
     *   <li>Calling the game logic to add a card to the player's hand</li>
     *   <li>Disabling the insurance button as insurance is only available on initial deal</li>
     *   <li>Updating the UI to reflect the new game state</li>
     *   <li>Handling various outcomes based on the player's status after hitting:</li>
     * </ol>
     * </p>
     * <ul>
     *   <li>If the player busts (goes over 21), the round ends and they lose</li>
     *   <li>If the player reaches exactly 21, they automatically stand</li>
     *   <li>If the player has 5 cards without busting (Five-card Charlie), they automatically stand</li>
     *   <li>Otherwise, the game continues and the player can choose their next action</li>
     * </ul>
     *
     * @see Game#playerHit()
     * @see PlayerStatus
     * @see #updateUI()
     * @see #endRound()
     */
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

    /**
     * Handles the action when a player chooses to stand (keep current hand).
     * <p>
     * This method processes the player's decision to stand by:
     * <ol>
     *   <li>Disabling all player action buttons (hit, stand, double) to prevent further actions</li>
     *   <li>Checking the dealer's current hand and score to determine the next steps</li>
     *   <li>Handling two different scenarios based on the dealer's hand:</li>
     * </ol>
     * </p>
     * <ul>
     *   <li><b>Dealer has 17+ (not soft 17):</b> The round ends immediately as the dealer
     *       doesn't need to draw more cards. The game outcome is determined and displayed.</li>
     *   <li><b>Dealer has less than 17 or soft 17:</b> The dealer needs to draw more cards,
     *       so the reveal button is enabled for the player to control the dealer's card drawing
     *       process.</li>
     * </ul>
     * <p>
     * The method finally updates the UI to reflect the current game state.
     * </p>
     *
     * @see Game#getOutcome()
     * @see #updateUI()
     * @see #endRound()
     */
    @FXML
    private void onStand() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        doubleButton.setDisable(true);

        Hand dealerHand = game.getDealer().getHand();
        int dealerScore = dealerHand.getScore();

        // Check if dealer already has 17+ and doesn't need to draw more cards
        if (dealerScore >= 17 && !(dealerScore == 17 && dealerHand.hasSoftAces())) {
            // Dealer already has 17+ (not a soft 17), no need to draw more cards
            lastOutcome = game.getOutcome();
            messageLabel.setText(lastOutcome);
            revealDealerButton.setDisable(true); // No need for player to press reveal
            endRound();
        } else {
            // Dealer needs to draw more cards
            messageLabel.setText("Dealer's turn... click to reveal cards.");
            revealDealerButton.setDisable(false);
        }

        updateUI();
    }



    /**
     * Handles the action when a player chooses to double down (double their bet and receive one more card).
     * <p>
     * This method processes the player's decision to double down by:
     * <ol>
     *   <li>Calling the game logic to process the double down action, which doubles the bet and deals one card</li>
     *   <li>Setting the doubledDown flag to track this special bet state</li>
     *   <li>Updating the UI to reflect the new game state</li>
     *   <li>Handling the outcome based on the player's status after receiving the additional card:</li>
     * </ol>
     * </p>
     * <ul>
     *   <li>If the player busts (goes over 21), the round ends immediately and they lose their doubled bet</li>
     *   <li>If the player doesn't bust, control is transferred to the dealer's turn via the reveal button</li>
     * </ul>
     * <p>
     * Note: The ability to double down is prevented when the player doesn't have enough money,
     * which is handled in the onDeal() method by disabling the doubleButton.
     * </p>
     *
     * @see Game#doubleDown()
     * @see PlayerStatus
     * @see #updateUI()
     * @see #endRound()
     */
    @FXML
    private void onDoubleDown() {
        PlayerStatus status = game.doubleDown();  // Call game logic to double bet and deal one card
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

    /**
     * Finalizes the current round of Blackjack and prepares for the next round.
     * <p>
     * This method handles all end-of-round tasks:
     * <ol>
     *   <li>Calculating and displaying the player's net gain or loss for the round</li>
     *   <li>Displaying the appropriate message about the round's outcome</li>
     *   <li>Resetting the game UI controls for the next round:</li>
     *   <ul>
     *     <li>Enabling the deal button to start a new round</li>
     *     <li>Disabling gameplay buttons (hit, stand, double, reveal)</li>
     *     <li>Enabling the bet change button to allow adjusting bets</li>
     *   </ul>
     *   <li>Displaying insurance results if applicable</li>
     *   <li>Updating all UI elements to reflect the final game state</li>
     * </ol>
     * </p>
     * <p>
     * The method formats the net change display with appropriate symbols (+ or -)
     * to clearly indicate gains or losses to the player.
     * </p>
     *
     * @see Game#getPlayer()
     * @see Player#getLastNetChange()
     * @see Player#getLastInsuranceChange()
     * @see #updateUI()
     */
    private void endRound(){
        // Calculate and format net change display with appropriate +/- symbol
        int net = game.getPlayer().getLastNetChange();
        String netChange = "Net Change: ";
        if (net > 0) {
            netChange += "+$" + net;  // Add plus sign for gains
        } else if (net < 0) {
            netChange += "-$" + Math.abs(net);  // Show negative amount as positive with minus sign
        } else {
            netChange += "$0";  // No change
        }
        System.out.println("Net change: " + netChange);
        payoutOrLossLabel.setText(netChange);
        System.out.println(lastOutcome);
        messageLabel.setText(lastOutcome);

        // Reset UI controls for next round
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

    /**
     * Displays the Casino Manager Popup and provides a console-like interface for interacting with
     * the application using text-based commands. The popup allows the user to input commands, which
     * are then processed, providing feedback or executing specific actions based on the input.
     * This feature is primarily intended for advanced management or debugging purposes.
     *<p>
     * The popup includes the following features:
     * <ul>
     *  <li>A customizable dialog box for entering commands.</li>
     *  <li>Styled UI components to enhance the appearance of the dialog.</li>
     *  <li>A poker chip icon replacing the default dialog question mark. </li>
     *  <li>A central positioning relative to the main application window. </li>
     *  <li>Continuous input prompts, where the user can execute commands or exit the console. </li>
     *  </ul>
     * </p>
     * Commands can include but are not limited to actions such as "exit" to close the console,
     * or additional commands processed by `handleConsoleCommand` for managing the game or application state.

     * Customization includes:
     * <ol>
     * <li>Styling via an external stylesheet.</li>
     * <li>A custom application icon for consistent branding.</li>
     * <li>Re-centering of the dialog whenever content changes.</li>
     * </ol>
     *
     * The process will terminate if the user chooses to exit or closes the popup.
     *
     * @see #handleConsoleCommand(String command)
     */
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

        // Center the dialog on screen
        stage.setOnShown(e -> {
            stage.setX((owner.getX() + owner.getWidth() / 2) - stage.getWidth() / 2);
            stage.setY((owner.getY() + owner.getHeight() / 2) - stage.getHeight() / 2);
        });

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

            // Re-center the dialog after content changes
            stage.setX((owner.getX() + owner.getWidth() / 2) - stage.getWidth() / 2);
            stage.setY((owner.getY() + owner.getHeight() / 2) - stage.getHeight() / 2);
        }

        loggedIn = false; // Reset login state when console is closed
    }

    /**
     * Authenticates the user by validating the provided password.
     * The authentication checks if the input password matches the hardcoded password.
     *
     * @param password The input password entered by the user for authentication.
     * @return true if the password matches the hardcoded value, false otherwise.
     */
    private boolean login(String password){
        return "casino123".equals(password); // Simple hardcoded password for demo purposes
    }

    /**
     * Handles console commands received as input and performs the corresponding actions or operations
     * based on the command. Commands include actions such as logging in, logging out, managing game states,
     * adjusting the player's money, reshuffling the deck, and more. For certain commands, interaction with
     * the game's state and UI is required.
     *
     * @param command The console command input as a string. This should include the command type and
     *                any optional arguments separated by spaces. Examples of commands include:
     *                "login <password>", "setmoney <amount>", "addmoney <amount>", "reset", "logout", and "help".
     * @return A string message providing feedback about the operation or any relevant instructions.
     *         Examples include success messages like "Logged in as Casino Manager.", error messages
     *         such as "Invalid Login Credentials. Please try again.", or usage information such as
     *         "Usage: addmoney <amount>".
     */
    private String handleConsoleCommand(String command) {
        // Handle the console command here
        // For now, just print it to the console
        System.out.println("Console Command: " + command);

        String[] parts = command.split(" ");
        // You can implement specific commands here, e.g.:
        if (!command.strip().equals("help") && !loggedIn){

            if (parts[0].equalsIgnoreCase("login")){
                // Simple login check, in a real application you would check against a database or secure storage
                if (parts.length < 2) {
                    return "Please provide a password to log in. Usage: login <password>";
                }

                if (login(command.substring(parts[0].length() + 1).trim())) {
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

                // cause on reset the logged in state is reset to false; we dont want to log out yet
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

            case "getcards":
                int cardsLeft = game.getDeck().getDeckArrayList().size();
                String cardsMessage = "Cards left in the deck: " + cardsLeft;
                System.out.println(cardsMessage);
                return cardsMessage;
            case "help":
                String helpText = """
                        Available commands:
                        exit - Exits the console.
                        reset - Resets the game to initial state.
                        setmoney <amount> - Sets the player's money to the specified amount.
                        addmoney <amount> - Adds the specified amount to the player's money.
                        getcards - Displays number of cards left in the deck.
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
            // Before reveal, only show the first card's value
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

    // todo: if time permits, implement splitting
}
