package ui;

import combatants.Combatant;
import combatants.Enemy;
import java.util.List;

// observer interface so the engine can report battle events without knowing anything abt CLI
// BattleEngine calls these, CLIBattleObserver implements them
// swapping to a GUI later just means a new impl, engine stays untouched
public interface BattleObserver {
    // called at the start of each round before turns begin
    void onRoundStart(List<Combatant> combatants, int roundNumber);
    // called after turn order is decided so the player nows whos going when
    void onTurnOrder(List<Combatant> order);
    // called when a combatant is skipping due to stun
    void onStunSkip(Combatant combatant);
    // called at the end of the round w final state
    void onRoundEnd(List<Combatant> combatants, int roundNumber);
    // called when a backup wave spawns so Level doesnt have to print directly
    void onBackupWave(List<Enemy> wave);
}
