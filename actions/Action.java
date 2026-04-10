package actions;

import combatants.Combatant;
import java.util.List;

// interface for all actions a combatant can do
// retuns true if action went thru, false if it failed (e.g skill on cooldown)
// BattleEngine uses this to noe if it shld reprompt the player
public interface Action {
    boolean execute(Combatant source, List<? extends Combatant> targets);
}
