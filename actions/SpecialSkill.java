package actions;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

public class SpecialSkill implements Action{
    private int targetIndex;

    public SpecialSkill(int targetIndex){
        this.targetIndex = targetIndex;
    }

    public boolean execute(Combatant source, List<? extends Combatant> targets){
        if (source.getCooldown() > 0) {
            System.out.println("> Skill still on cooldown ("
                    + source.getCooldown() + " turns left), pick another action");
            return false;
        }
        if (!(source instanceof Player)){
            System.out.println("> " + source.getName() + " has no special skill");
            return false;
        }
        boolean succeeded = ((Player) source).useSkill(targets, targetIndex);


        if (succeeded){
            source.setCooldown(3);
        }
        return succeeded;
    }

}

