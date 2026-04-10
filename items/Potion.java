package items;

import combatants.Combatant;

// simple healing item
// restores a fixed amount of HP, capped at the combatant's max HP
// uses the public heal() method instead of touching hp directly
// so we dont break encapsulation or protected access rules
public class Potion implements Item {

    private int healAmount = 100;

    @Override
    public void use(Combatant source) {
        // heal() already handles max HP limits internally
        source.heal(healAmount);
    }

    @Override
    public String getName() {
        return "Potion";
    }
}