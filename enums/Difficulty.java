package enums;

import combatants.Enemy;
import combatants.Goblin;
import combatants.Wolf;
import java.util.ArrayList;
import java.util.List;

// defines the enemy waves for each difficulty level
public enum Difficulty {
    EASY, MEDIUM, HARD;

    // returns the starting wave of enemies for this difficulty
    public List<Enemy> getInitialWave() {
        List<Enemy> enemies = new ArrayList<>();

        switch (this) {
            case EASY:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
                break;
            case MEDIUM:
                enemies.add(new Goblin("Goblin"));
                enemies.add(new Wolf("Wolf"));
                break;
            case HARD:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                break;
        }

        //System.out.println("getInitialWave size=" + enemies.size());
        return enemies;
    }

    // returns the backup wave, spawns after initial wave is wiped
    public List<Enemy> getBackupWave() {
        List<Enemy> enemies = new ArrayList<>();

        switch (this) {
            case EASY:
                // no backup on easy
                break;
            case MEDIUM:
                enemies.add(new Wolf("Wolf A"));
                enemies.add(new Wolf("Wolf B"));
                break;
            case HARD:
                enemies.add(new Goblin("Goblin"));
                enemies.add(new Wolf("Wolf A"));
                enemies.add(new Wolf("Wolf B"));
                break;
        }

        return enemies;
    }
}
