package actions;

import combatants.Combatant;
import java.util.List;

// basic attack, hits one target
// dmg = max(0, attacker atk - target def)
// print attack msg first then apply dmg so smoke bomb msg shows after
public class BasicAttack implements Action {

    private int targetIndex;

    public BasicAttack(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    public boolean execute(Combatant source, List<? extends Combatant> targets) {
        if (targets == null || targets.size() == 0) {
            System.out.println("> No targets to attack");
            return false;
        }
        if (targetIndex < 0 || targetIndex >= targets.size()) {
            System.out.println("> Target index out of range");
            return false;
        }

        Combatant target = targets.get(targetIndex);

        if (!target.isAlive()) {
            System.out.println("> " + target.getName() + " is alredy dead");
            return false;
        }

        int dmg = source.getAttack() - target.getDefense();
        if (dmg < 0) dmg = 0;

        int hpBefore = target.getHp();

        // print attack msg before applying dmg
        System.out.println("> " + source.getName() + " attacks " + target.getName()
                + " for " + dmg + " dmg (" + source.getAttack() + " - " + target.getDefense() + ")");

        // takeDamage handles smoke bomb check internally
        target.takeDamage(dmg);

        // only show hp change if dmg actually went thru (not blocked by smoke bomb)
        if (!target.isInvulnerable()) {
            System.out.println("> " + target.getName() + " HP: " + hpBefore + " -> " + target.getHp());
        }

        //System.out.println("BasicAttack done, target hp=" + target.getHp());

        return true;
    }
}
