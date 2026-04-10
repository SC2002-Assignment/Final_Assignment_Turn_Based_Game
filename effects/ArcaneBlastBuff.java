package effects;

import combatants.Combatant;

// permanent atk bonus from arcane blast kills, lasts till end of level
// designed as a single accumulating buff - stack() adds to the bonus in place
// rather than creating a new instance per kill, so the effects list stays clean
// Wizard calls stack() on each kill instead of applying a new buff each time
public class ArcaneBlastBuff implements StatusEffect {

    private int bonus;
    private Combatant target;

    public ArcaneBlastBuff(int initialBonus) {
        this.bonus = initialBonus;
    }

    // apply the initial bonus when the buff is first added to the combatant
    public void apply(Combatant c) {
        this.target = c;
        c.increaseAttack(bonus);
        //System.out.println("ArcaneBlastBuff applied, bonus=" + bonus + " atk now=" + c.getAttack());
    }

    // called on each subsequent kill to grow the existing buff instead of adding a new one
    // also applies the addtional bonus immediately to the target
    public void stack(int additionalBonus) {
        this.bonus += additionalBonus;
        if (target != null) {
            target.increaseAttack(additionalBonus);
        }
    }

    public int getBonus() {
        return bonus;
    }

    public void tick() {
        // permanent buff, doesnt tick down
    }

    public boolean isExpired() {
        // never expires, lasts till end of level
        return false;
    }
}
