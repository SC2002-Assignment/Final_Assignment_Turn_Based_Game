package effects;

import combatants.Combatant;

// interface for all status effects
public interface StatusEffect {
    // apply effect to target
    void apply(Combatant c);
    // tick down each turn
    void tick();
    // return true when effect shld be removed
    boolean isExpired();
}
