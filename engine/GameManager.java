package engine;

import combatants.Player;
import combatants.Warrior;
import combatants.Wizard;
import enums.Difficulty;
import items.Item;
import items.Potion;
import items.PowerStone;
import items.SmokeBomb;
import java.util.ArrayList;
import java.util.List;
import strategies.SpeedBasedTurnOrder;
import ui.CLI;
import ui.CLIBattleObserver;
import ui.CLIInputProvider;

// handles game setup, restart and the main loop
// wires together the engine w the CLI implementations of BattleInputProvider and BattleObserver
// swapping to a different UI just means passing different impls here
public class GameManager {

    private BattleEngine engine;
    private String savedPlayerClass;
    // store item type names instead of instances so replay always gets fresh objs
    private List<String> savedItemNames = new ArrayList<>();
    private Difficulty savedDifficulty;

    public void startGame() {
        Player player = CLI.selectPlayer();
        savedPlayerClass = player instanceof Warrior ? "Warrior" : "Wizard";

        CLI.selectItems(player);

        // record item names by type so replay can recreate clean instances
        savedItemNames.clear();
        for (int i = 0; i < player.getItems().size(); i++) {
            savedItemNames.add(player.getItems().get(i).getName());
        }

        Difficulty difficulty = CLI.selectDifficulty();
        savedDifficulty = difficulty;

        //System.out.println("startGame class=" + savedPlayerClass + " diff=" + savedDifficulty);

        setupEngine(player, difficulty);
        runGame(player);
    }

    public void runGame(Player player) {
        while (true) {
            engine.runRound();

            if (engine.isGameOver()) {
                Player alivePlayer = engine.getAlivePlayer();

                if (alivePlayer != null) {
                    // pass class and difficulty so the end screen can show full info
                    CLI.showVictoryScreen(
                        alivePlayer.getHp(),
                        engine.getRoundCount(),
                        savedPlayerClass,
                        savedDifficulty,
                        alivePlayer.getItems()
                    );
                } else {
                    // player is dead so grab items from last known player ref
                    CLI.showDefeatScreen(
                        engine.getAliveEnemies().size(),
                        engine.getRoundCount(),
                        savedPlayerClass,
                        savedDifficulty,
                        player.getItems()
                    );
                }

                int choice = CLI.promptReplay();

                if (choice == 1) {
                    replayGame();
                } else if (choice == 2) {
                    startGame();
                } else {
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                }

                break;
            }
        }
    }

    // restart w same player class and items but recreate everything fresh
    public void replayGame() {
        Player player = savedPlayerClass.equals("Warrior") ? new Warrior() : new Wizard();

        // recreate items by type so mutable state from the last run doesnt bleed over
        for (int i = 0; i < savedItemNames.size(); i++) {
            String name = savedItemNames.get(i);
            if (name.equals("Potion")) {
                player.addItem(new Potion());
            } else if (name.equals("Smoke Bomb")) {
                player.addItem(new SmokeBomb());
            } else if (name.equals("Power Stone")) {
                player.addItem(new PowerStone());
            }
        }

        //System.out.println("replayGame player=" + savedPlayerClass);

        setupEngine(player, savedDifficulty);
        runGame(player);
    }

    private void setupEngine(Player player, Difficulty difficulty) {
        Level level = new Level(difficulty);

        // wire up the CLI impls of both interfaces
        // to swap to a GUI later, just pass different impls here instead
        engine = new BattleEngine(
            new SpeedBasedTurnOrder(),
            level,
            new CLIInputProvider(),
            new CLIBattleObserver()
        );

        engine.addCombatants(level.getInitialWave());

        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        engine.addCombatants(playerList);
    }
}
