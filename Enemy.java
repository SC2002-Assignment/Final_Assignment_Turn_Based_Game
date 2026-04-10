package combatants;

import actions.Action;
import strategies.EnemyActionStrategy;
import java.util.List;

// base class for all enemies
// uses a strategy to decide what action to take each turn
public abstract class Enemy extends Combatant {

    // strategy decides enemy behaviour, easy to swap later
    protected EnemyActionStrategy actionStrategy;

    public Enemy(String name, int hp, int atk, int def, int spd, EnemyActionStrategy actionStrategy) {
        super(name, hp, atk, def, spd);
        this.actionStrategy = actionStrategy;
    }

    // enemies are AI controlled, not player controlled
    public boolean isPlayerControlled() {
        return false;
    }

    // enemy picks action automatically via strategy
    public Action chooseAction(List<? extends Combatant> targets) {
        return actionStrategy.decideAction(this, targets);
    }
}
