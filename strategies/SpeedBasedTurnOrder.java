package strategies;

import combatants.Combatant;
import combatants.Enemy;
import java.util.List;
import java.util.stream.Collectors;

// determines turn order based on speed (higher speed acts first)
// when speeds are equal, enemies are prioritised over players
// uses public getters to respect Combatant encapsulation
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> getTurnOrder(List<Combatant> combatants) {

        return combatants.stream()
            // dead combatants dont get a turn
            .filter(Combatant::isAlive)

            // sort by speed (descending)
            .sorted((a, b) -> {
                if (b.getSpeed() != a.getSpeed()) {
                    return b.getSpeed() - a.getSpeed();
                }

                // equal sopeed, so enemies go before players
                if (a instanceof Enemy && !(b instanceof Enemy)) {
                    return -1;
                }
                if (!(a instanceof Enemy) && b instanceof Enemy) {
                    return 1;
                }
                return 0;
            })

            .collect(Collectors.toList());
    }
}