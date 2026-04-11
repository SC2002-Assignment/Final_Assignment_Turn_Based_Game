package actions;

import combatants.Combatant;
import java.util.List;

public interface Action{
    boolean execute(Combatant source, List<? extends Combatant> targets);
}
