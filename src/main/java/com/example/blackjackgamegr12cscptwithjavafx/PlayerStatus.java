package com.example.blackjackgamegr12cscptwithjavafx;


/**
  * Enum representing the possible statuses of a player in a Blackjack game.
  * <ul>
  *   <li>CONTINUE: The player can continue playing.</li>
  *   <li>BUST: The player has exceeded 21 points.</li>
  *   <li>TWENTY_ONE: The player has exactly 21 points.</li>
  *   <li>DOUBLE_DOWN: The player has chosen to double down.</li>
  *   <li>FIVE_CARDS: The player has five cards without busting.</li>
  * </ul>
  */
 public enum PlayerStatus {
     CONTINUE, BUST, TWENTY_ONE, DOUBLE_DOWN, FIVE_CARDS
 }
