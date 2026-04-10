package items;

import combatants.Combatant;
import effects.SmokeBombEffect;

// defensive item that makes the player invulnerable for a short time
// actual logic is in SmokeBombEffect
// this class is just responsible for applying that effect to the user
public class SmokeBomb implements Item {

    @Override
    public void use(Combatant source) {
        // duration 2 = current turn + next turn
        source.applyEffect(new SmokeBombEffect(2));
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }
}