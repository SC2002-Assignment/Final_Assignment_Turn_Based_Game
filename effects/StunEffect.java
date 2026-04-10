package effects;

import combatants.Combatant;

// stun effect, prevents target from acting for set number of turns
// rule: effect applies imediately, then decrements AFTER the stunned turn is used
// so StunEffect(2) means the target skips 2 actual turns before stun clears
public class StunEffect implements StatusEffect {

    private int duration;
    private Combatant target;

    public StunEffect(int duration) {
        this.duration = duration;
    }

    public void apply(Combatant c) {
        this.target = c;
        c.setStunned(true);
    }

    // tick is called AFTER the stun check in BattleEngine, not before
    // so the duration decrements after the turn is actually lost, not before
    public void tick() {
        duration--;
        //System.out.println("StunEffect tick, duration now=" + duration);
        // remove stun when it runs out
        if (duration == 0 && target != null) {
            target.setStunned(false);
        }
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
