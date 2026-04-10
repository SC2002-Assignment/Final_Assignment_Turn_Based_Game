package effects;

import combatants.Combatant;

// applied to the player when smoke bomb is used
// makes player invulnerable so enemy attacks deal 0 dmg
// invulnerability check is handled in Combatant.takeDamage
// duration 2 turns (current + next)
public class SmokeBombEffect implements StatusEffect {

    private int duration;
    private Combatant target;

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    public void apply(Combatant c) {
        this.target = c;
        c.setInvulnerable(true);
    }

    public void tick() {
        duration--;
        // remove invulnerability when effect runs out
        if (isExpired() && target != null) {
            target.setInvulnerable(false);
            //System.out.println("SmokeBombEffect expired, invuln removed");
        }
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
