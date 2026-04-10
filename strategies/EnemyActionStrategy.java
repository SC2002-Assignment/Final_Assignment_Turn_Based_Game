package strategies;

import actions.Action;
import combatants.Combatant;
import combatants.Enemy;
import java.util.List;

// strategy interface for enemy decision-making
// allows different enemy AIs to be swapped in without changing Enemy or BattleEngine
// follows the Strategy pattern
public interface EnemyActionStrategy {

    // decide enemy action based on the enemy and possible targets
    Action decideAction(Enemy e, List<? extends Combatant> targets);
}