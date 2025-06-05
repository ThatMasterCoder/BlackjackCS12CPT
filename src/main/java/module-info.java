module com.example.blackjackgamegr12cscptwithjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;


    opens com.example.blackjackgamegr12cscptwithjavafx to javafx.fxml;
    exports com.example.blackjackgamegr12cscptwithjavafx;
}