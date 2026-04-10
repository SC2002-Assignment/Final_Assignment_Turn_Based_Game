package strategies;

import actions.Action;
import actions.BasicAttack;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import java.util.List;

// simple enemy AI strategy
// enemies using this strategy will always try to attack a living player
// if no player is found (edge case), it defaults to the first target
public class BasicAttackStrategy implements EnemyActionStrategy {

    @Override
    public Action decideAction(Enemy e, List<? extends Combatant> targets) {

        // look for the first living player in the target list
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i) instanceof Player && targets.get(i).isAlive()) {
                return new BasicAttack(i);
            }
        }

        // fallback: attack index 0 to avoid crashing
        return new BasicAttack(0);
    }
}