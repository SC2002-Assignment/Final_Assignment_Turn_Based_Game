package combatants;

import actions.Action;
import actions.SpecialSkill;
import effects.ArcaneBlastBuff;
import effects.StatusEffect;
import java.util.List;

// wizard player class
// HP: 200, ATK: 50, DEF: 10, SPD: 20
public class Wizard extends Player {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    // wizard hits all enemies so no target selection needed, idx 0 is fine
    // buildBasicAttackAction and buildPowerStoneAction inherited from Player are alredy correct
    public Action buildSpecialSkillAction(List<? extends Combatant> enemies) {
        return new SpecialSkill(0);
    }

    // Arcane Blast: hit all enemies for basic attack dmg
    // each enemy killed gives +10 ATK, lasts till end of level
    // tracked via ArcaneBlastBuff in the effects list - one buff that grows on each kill
    // rather than a new buff per kill so the effects list stays clean
    // targetIndex not used here since its AoE, kept for interface consistency
    // returns true if at least one enemy was hit, false if there was nothing to hit
    public boolean useSkill(List<? extends Combatant> targets, int targetIndex) {
        if (targets == null || targets.size() == 0) {
            System.out.println("> No targets for Arcane Blast");
            return false;
        }

        System.out.println("> " + this.getName() + " casts Arcane Blast!");

        // track if anything actually got hit so we have a meaningful success signal
        boolean hitAnything = false;

        for (int i = 0; i < targets.size(); i++) {
            Combatant target = targets.get(i);
            if (!target.isAlive()) continue;

            if (target.isInvulnerable()) {
                System.out.println("> " + target.getName() + " blocks the blast with invulnerability");
                continue;
            }

            int dmg = this.getAttack() - target.getDefense();
            if (dmg < 0) dmg = 0;

            int oldHp = target.getHp();
            target.takeDamage(dmg);
            hitAnything = true;

            System.out.println("> " + target.getName()
                    + " takes " + dmg + " dmg ("
                    + this.getAttack() + " - " + target.getDefense() + " = " + dmg + ")"
                    + " HP: " + oldHp + " -> " + target.getHp());

            // kill bonus: +10 ATK per enemy killed, tracked thru ArcaneBlastBuff
            if (!target.isAlive()) {
                addAtkBonus(10);
                System.out.println("> " + this.getName()
                        + " gains +10 ATK from Arcane Blast! (Now " + this.getAttack() + ")");
            }
        }

        //System.out.println("Arcane Blast done, hitAnything=" + hitAnything + " atk now=" + this.getAttack());

        return hitAnything;
    }

    // finds the existing ArcaneBlastBuff and stacks onto it
    // if none exists yet, creates a new one and applies it
    // this keeps exactly one ArcaneBlastBuff in the effects list no matter how many kills
    private void addAtkBonus(int bonus) {
        for (int i = 0; i < this.getEffects().size(); i++) {
            StatusEffect e = this.getEffects().get(i);
            if (e instanceof ArcaneBlastBuff) {
                ((ArcaneBlastBuff) e).stack(bonus);
                return;
            }
        }
        // no existing buff found so create the first one
        this.applyEffect(new ArcaneBlastBuff(bonus));
    }
}
