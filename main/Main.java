package main;

import engine.GameManager;
import ui.CLI;

// entry point, shows loading screen then starts the game
public class Main {
    public static void main(String[] args) {
        CLI.showLoadingScreen();
        GameManager gm = new GameManager();
        gm.startGame();
    }
}
