package actions;

import combatants.Combatant;
import effects.DefendBuff;
import java.util.List;

// defend action, gives +10 def for curr turn and next turn
public class Defend implements Action {

    public boolean execute(Combatant source, List<? extends Combatant> targets) {
        source.applyEffect(new DefendBuff(2, 10));
        System.out.println("> " + source.getName()
                + " is defending! DEF +10 for this turn and next turn");
        return true;
    }
}
