package ui;

import actions.Action;
import combatants.Combatant;
import combatants.Enemy;
import java.util.List;

// interface for supplying player actions to the engine
// BattleEngine calls getAction() and gets back a ready-to-execute Action
// engine doesnt care if it came from a CLI, a GUI, or an AI - it just runs it
public interface BattleInputProvider {
    // returns the action the player wants to take this turn
    // returns null if the input was invalid and the engine shld reprompt
    Action getAction(Combatant player, List<Enemy> enemies);
}
