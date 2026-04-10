package actions;

import combatants.Combatant;
import combatants.Player;
import items.Item;
import items.PowerStone;
import java.util.List;

// uses the selected item on the combatant
// only removes item from inventory if the use actually succeeded
// targets and targetIndex only matter for PowerStone, ignored by everything else
public class UseItem implements Action {

    private Item item;
    private List<? extends Combatant> targets;
    private int targetIndex;

    // standard constructor for potions and smoke bombs
    public UseItem(Item item) {
        this.item = item;
        this.targets = null;
        this.targetIndex = 0;
    }

    // constructor for powerstone, targets injected here at action-build time
    public UseItem(Item item, List<? extends Combatant> targets, int targetIndex) {
        this.item = item;
        this.targets = targets;
        this.targetIndex = targetIndex;
    }

    public boolean execute(Combatant source, List<? extends Combatant> enemies) {
        if (item == null) {
            System.out.println("> No item selected");
            return false;
        }

        System.out.println("> " + source.getName() + " uses " + item.getName() + "!");

        boolean succeeded;

        // powerstone gets its targets passed in directly instead of from a stale constructor
        // check the boolean result so we dont consume the stone if skill didnt fire
        if (item instanceof PowerStone && targets != null) {
            succeeded = ((PowerStone) item).use(source, targets, targetIndex);
        } else {
            // potions and smoke bombs always succeed, no fail conditon
            item.use(source);
            succeeded = true;
        }

        //System.out.println("item use result=" + succeeded);

        // only remove from inventory if item actually did smthing
        if (succeeded && source instanceof Player) {
            ((Player) source).removeItem(item);
        }

        return succeeded;
    }
}
