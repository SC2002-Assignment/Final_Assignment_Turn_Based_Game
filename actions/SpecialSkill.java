package actions;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

// special skill action
// delegates to the player subclass via useSkill so no instanceof needed in execute (OCP + LSP)
// returns false if on cooldown so BattleEngine nows to reprompt player
// cooldown only set to 3 if useSkill actually returned true - no cooldown on a failed skill
public class SpecialSkill implements Action {

    private int targetIndex;

    public SpecialSkill(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    // default constructor used by PowerStone
    public SpecialSkill() {
        this.targetIndex = 0;
    }

    public boolean execute(Combatant source, List<? extends Combatant> targets) {
        if (source.getCooldown() > 0) {
            System.out.println("> Skill still on cooldown ("
                    + source.getCooldown() + " turns left), pick another action");
            return false;
        }

        // only players have special skills
        // downcast is safe here since only players use SpecialSkill
        if (!(source instanceof Player)) {
            System.out.println("> " + source.getName() + " has no special skill");
            return false;
        }

        //System.out.println("triggering skill for " + source.getName() + " targetIdx=" + targetIndex);

        boolean succeeded = ((Player) source).useSkill(targets, targetIndex);

        // only apply cooldown if skill actually did smthing
        // avoids burning cooldown on a failed cast e.g hitting already-dead target
        if (succeeded) {
            source.setCooldown(3);
        }

        //System.out.println("skill result=" + succeeded + " cd now=" + source.getCooldown());

        return succeeded;
    }

    // triggers skill effect only, no cooldown change
    // used by PowerStone so it doesnt mess w cooldown
    // returns success result from useSkill so UseItem can decide whether to consume the stone
    public boolean triggerEffect(Combatant source, List<? extends Combatant> targets) {
        if (source instanceof Player) {
            return ((Player) source).useSkill(targets, targetIndex);
        }
        System.out.println("> " + source.getName() + " has no special skill");
        return false;
    }
}
