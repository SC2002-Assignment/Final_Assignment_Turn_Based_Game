package combatants;

import actions.Action;
import actions.BasicAttack;
import actions.SpecialSkill;
import actions.UseItem;
import effects.StunEffect;
import items.Item;
import java.util.List;

// warrior player class
// HP: 260, ATK: 40, DEF: 20, SPD: 30
public class Warrior extends Player {

    public Warrior() {
        super("Warrior", 260, 40, 20, 30);
    }

    // warrior picks a single target for basic attack
    public Action buildBasicAttackAction(List<? extends Combatant> enemies) {
        int idx = pickTarget(enemies);
        return new BasicAttack(idx);
    }

    // warrior picks a single target for shield bash
    public Action buildSpecialSkillAction(List<? extends Combatant> enemies) {
        int idx = pickTarget(enemies);
        return new SpecialSkill(idx);
    }

    // warrior picks a single target when using power stone too
    public Action buildPowerStoneAction(Item stone, List<? extends Combatant> enemies) {
        int idx = pickTarget(enemies);
        return new UseItem(stone, enemies, idx);
    }

    // Shield Bash: hit one enemy for basic attack dmg and stun them 2 turns
    // returns true if the skill landed, false if it couldnt fire so cooldown and item use stay clean
    public boolean useSkill(List<? extends Combatant> targets, int targetIndex) {
        if (targets == null || targets.size() == 0) {
            System.out.println("> No targets for Shield Bash");
            return false;
        }
        if (targetIndex < 0 || targetIndex >= targets.size()) {
            System.out.println("> Invalid target");
            return false;
        }

        Combatant target = targets.get(targetIndex);

        if (!target.isAlive()) {
            System.out.println("> " + target.getName() + " already defeated");
            return false;
        }
        if (target.isInvulnerable()) {
            System.out.println("> Shield Bash blocked by " + target.getName() + "'s invulnerability");
            return false;
        }

        int dmg = this.getAttack() - target.getDefense();
        if (dmg < 0) dmg = 0;

        //System.out.println("ShieldBash dmg=" + dmg + " target=" + target.getName());

        target.takeDamage(dmg);
        target.applyEffect(new StunEffect(2));

        System.out.println("> " + this.getName()
                + " uses Shield Bash on " + target.getName()
                + " for " + dmg + " dmg ("
                + this.getAttack() + " - " + target.getDefense() + " = " + dmg
                + ") and stuns them for 2 turns!");
        return true;
    }
}
