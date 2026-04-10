package items;

import combatants.Combatant;

public interface Item {
    void use(Combatant source);
    String getName();
}
