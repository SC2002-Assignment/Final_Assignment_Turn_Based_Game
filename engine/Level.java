package engine;

import combatants.Combatant;
import combatants.Enemy;
import enums.Difficulty;
import java.util.List;
import ui.BattleObserver;

// handles enemy waves for a given difficulty
// never prints directly - backup spawn events go thru BattleObserver
public class Level {

    private Difficulty difficulty;
    private List<Enemy> initialWave;
    private List<Enemy> backupWave;
    private boolean backupSpawned = false;

    public Level(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.initialWave = difficulty.getInitialWave();
        this.backupWave = difficulty.getBackupWave();
    }

    public List<Enemy> getInitialWave() {
        return initialWave;
    }

    // check if backup wave shld spawn, only triggers once when initial wave all dead
    // observer handles the display so Level stays out of the UI layer
    public void checkBackupSpawn(List<Combatant> active, BattleEngine engine, BattleObserver observer) {
        if (backupSpawned) return;
        if (backupWave == null || backupWave.isEmpty()) return;

        // check if all enemies in the initial wave are dead
        boolean allDead = true;
        for (int i = 0; i < initialWave.size(); i++) {
            if (initialWave.get(i).isAlive()) {
                allDead = false;
                break;
            }
        }

        //System.out.println("checkBackupSpawn allDead=" + allDead + " backupSpawned=" + backupSpawned);

        if (allDead) {
            engine.addCombatants(backupWave);
            backupSpawned = true;
            // notify thru observer instead of printing here
            observer.onBackupWave(backupWave);
        }
    }
}
