package effects;

import combatants.Combatant;

// temp defense buff from defend action, +10 def for 2 turns
public class DefendBuff implements StatusEffect {

    private int duration;
    private int bonus;
    private Combatant target;

    public DefendBuff(int duration, int bonus) {
        this.duration = duration;
        this.bonus = bonus;
    }

    public void apply(Combatant c) {
        this.target = c;
        c.increaseDefense(bonus);
        //System.out.println("DefendBuff applied, def now=" + c.getDefense());
    }

    public void tick() {
        duration--;
        // remove the def bonus when expired
        if (isExpired() && target != null) {
            target.decreaseDefense(bonus);
            //System.out.println("DefendBuff expired, def now=" + target.getDefense());
        }
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
