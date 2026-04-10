package strategies;

import combatants.Combatant;
import java.util.List;

// strategy interface for determining combat turn order
// allows different ordering rules (speed-based, random, etc.) without modifying the BattleEngine
public interface TurnOrderStrategy {

    // given all combatants in the battle, return them in the order they should take turns
    List<Combatant> getTurnOrder(List<Combatant> combatants);
}