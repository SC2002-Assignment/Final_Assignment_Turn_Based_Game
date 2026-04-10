package ui;

import combatants.Player;
import combatants.Warrior;
import combatants.Wizard;
import enums.Difficulty;
import items.Item;
import items.Potion;
import items.PowerStone;
import items.SmokeBomb;
import java.util.List;
import java.util.Scanner;

// handles setup screens and end screens only
// battle display is handled by CLIBattleObserver
// battle input is handled by CLIInputProvider
// this keeps CLI focused on game flow, not battle logic
public class CLI {

    private static Scanner sc = new Scanner(System.in);

    // reads int input safely, keeps asking if out of range or not a number
    // package-visible so CLIInputProvider and CLIBattleObserver can use it
    public static int readChoice(int min, int max) {
        while (true) {
            System.out.print("> ");
            String input = sc.nextLine();
            try {
                int choice = Integer.parseInt(input.trim());
                if (choice >= min && choice <= max) return choice;
                System.out.println("Invalid choice, enter a number from " + min + " to " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number");
            }
        }
    }

    // maps effect class names to friendly display strings
    // static so CLIBattleObserver can call it without duplicating the mapping
    public static String getEffectLabel(String className) {
        if (className.equals("StunEffect"))      return "Stunned";
        if (className.equals("DefendBuff"))      return "Defending";
        if (className.equals("SmokeBombEffect")) return "Smoke Screen";
        if (className.equals("ArcaneBlastBuff")) return "Arcane Power";
        return className;
    }

    // kept for backwards compat w any old calls
    public static String getFriendlyEffectName(String className) {
        return getEffectLabel(className);
    }

    // shows game title and all stats info on startup
    public static void showLoadingScreen() {
        System.out.println("========================================");
        System.out.println("   COMBAT ARENA: TURN-BASED RPG");
        System.out.println("========================================");
        System.out.println("\nPLAYER CLASSES:");
        System.out.println("- Warrior: HP: 260, ATK: 40, DEF: 20, SPD: 30");
        System.out.println("  Special: Shield Bash - hit one enemy for atk dmg and stun 2 turns");
        System.out.println("- Wizard:  HP: 200, ATK: 50, DEF: 10, SPD: 20");
        System.out.println("  Special: Arcane Blast - hit all enemies, +10 ATK per kill");
        System.out.println("\nENEMY TYPES:");
        System.out.println("- Goblin:  HP: 55, ATK: 35, DEF: 15, SPD: 25");
        System.out.println("- Wolf:    HP: 40, ATK: 45, DEF: 5,  SPD: 35");
        System.out.println("\nDIFFICULTY LEVELS:");
        System.out.println("- Easy:   3 Goblins");
        System.out.println("- Medium: 1 Goblin + 1 Wolf, backup: 2 Wolves");
        System.out.println("- Hard:   2 Goblins, backup: 1 Goblin + 2 Wolves");
        System.out.println("========================================\n");
    }

    // let player pick warrior or wizard
    public static Player selectPlayer() {
        System.out.println("Select your class:");
        System.out.println("1. Warrior (HP: 260, ATK: 40, DEF: 20, SPD: 30)");
        System.out.println("2. Wizard  (HP: 200, ATK: 50, DEF: 10, SPD: 20)");
        int choice = readChoice(1, 2);
        //System.out.println("player chose: " + choice);
        return choice == 2 ? new Wizard() : new Warrior();
    }

    // player picks 2 items, duplicates allowed
    public static void selectItems(Player player) {
        System.out.println("\nSelect 2 items (duplicates allowed):");
        System.out.println("1. Potion      - Heals 100 HP");
        System.out.println("2. Smoke Bomb  - Enemy attacks deal 0 dmg for curr and next turn");
        System.out.println("3. Power Stone - Triggers special skill once, no cooldown effect");

        for (int i = 0; i < 2; i++) {
            System.out.println("Choose item " + (i + 1) + ":");
            int choice = readChoice(1, 3);
            //System.out.println("item " + (i+1) + " choice=" + choice);
            if (choice == 1) {
                player.addItem(new Potion());
            } else if (choice == 2) {
                player.addItem(new SmokeBomb());
            } else {
                // powerstone targets are injected at use time in CLIInputProvider, not stored on the item
                player.addItem(new PowerStone());
            }
        }
        //System.out.println("inventory size after selection: " + player.getItems().size());
    }

    // let player pick difficulty
    public static Difficulty selectDifficulty() {
        System.out.println("\nSelect Difficulty:");
        System.out.println("1. Easy   (3 Goblins)");
        System.out.println("2. Medium (1 Goblin, 1 Wolf + backup: 2 Wolves)");
        System.out.println("3. Hard   (2 Goblins + backup: 1 Goblin, 2 Wolves)");
        int choice = readChoice(1, 3);
        //System.out.println("difficulty chosen: " + choice);
        if (choice == 2) return Difficulty.MEDIUM;
        if (choice == 3) return Difficulty.HARD;
        return Difficulty.EASY;
    }

    // victory screen w more detail - shows class, difficulty, items left, and final stats
    public static void showVictoryScreen(int hpLeft, int rounds, String playerClass,
                                          Difficulty difficulty, List<Item> itemsLeft) {
        System.out.println("\n*** VICTORY ***");
        System.out.println("Congratulations, you defeated all enemies!");
        System.out.println("Class:          " + playerClass);
        System.out.println("Difficulty:     " + difficulty);
        System.out.println("Remaining HP:   " + hpLeft);
        System.out.println("Total Rounds:   " + rounds);
        // show remaining items or none if all used up
        if (itemsLeft == null || itemsLeft.size() == 0) {
            System.out.println("Items Left:     none");
        } else {
            String itemStr = "";
            for (int i = 0; i < itemsLeft.size(); i++) {
                itemStr += itemsLeft.get(i).getName();
                if (i < itemsLeft.size() - 1) itemStr += ", ";
            }
            System.out.println("Items Left:     " + itemStr);
        }
    }

    // defeat screen w more detail - same extra context as victory
    public static void showDefeatScreen(int enemiesLeft, int rounds, String playerClass,
                                         Difficulty difficulty, List<Item> itemsLeft) {
        System.out.println("\n*** DEFEATED ***");
        System.out.println("Dont give up, try again!");
        System.out.println("Class:              " + playerClass);
        System.out.println("Difficulty:         " + difficulty);
        System.out.println("Enemies Remaining:  " + enemiesLeft);
        System.out.println("Rounds Survived:    " + rounds);
        if (itemsLeft == null || itemsLeft.size() == 0) {
            System.out.println("Items Left:         none");
        } else {
            String itemStr = "";
            for (int i = 0; i < itemsLeft.size(); i++) {
                itemStr += itemsLeft.get(i).getName();
                if (i < itemsLeft.size() - 1) itemStr += ", ";
            }
            System.out.println("Items Left:         " + itemStr);
        }
    }

    public static int promptReplay() {
        System.out.println("\n1. Replay with same settings");
        System.out.println("2. New game");
        System.out.println("3. Exit");
        return readChoice(1, 3);
    }
}
