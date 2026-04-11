package actions;

import combatants.Combatant;
import java.util.List;

// basic attack for both enemy n player
public class BasicAttack implements Action{
    private int targetIndex;

    public BasicAttack(int targetIndex){
        this.targetIndex = targetIndex;
    }

    public boolean execute(Combatant source, List<? extends Combatant> targets){
        if (targets == null || targets.size() == 0) {
            System.out.println("> No targets to attack");
            return false;
        }
        if (targetIndex < 0 || targetIndex >= targets.size()){
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
        System.out.println("> " + source.getName() + " attacks " + target.getName()
                + " for " + dmg + " dmg (" + source.getAttack() + " - " + target.getDefense() + ")");
        target.takeDamage(dmg);

        if (!target.isInvulnerable()) {
            System.out.println("> " + target.getName() + " HP: " + hpBefore + " -> " + target.getHp());
        }
        //System.out.println("BasicAttack done, target hp=" + target.getHp());




        return true;
    }
}
