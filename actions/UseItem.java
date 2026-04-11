package actions;

import combatants.Combatant;
import combatants.Player;
import items.Item;
import items.PowerStone;
import java.util.List;

// curr only powerston was the problem
public class UseItem implements Action{
    private Item item;
    private List<? extends Combatant> targets;
    private int targetIndex;

    public UseItem(Item item){
        this.item = item;
        this.targets = null;
        this.targetIndex = 0;
    }

    public UseItem(Item item, List<? extends Combatant> targets, int targetIndex){
        this.item = item;
        this.targets = targets;
        this.targetIndex = targetIndex;
    }

    public boolean execute(Combatant source, List<? extends Combatant> enemies){
        if (item == null) {
            System.out.println("> No item selected");
            return false;
        }

        System.out.println("> " + source.getName() + " uses " + item.getName() + "!");
        boolean succeeded;

        if (item instanceof PowerStone && targets != null){
            succeeded = ((PowerStone) item).use(source, targets, targetIndex);
        } else {
            item.use(source);
            succeeded = true;
        }
        if (succeeded && source instanceof Player){
            ((Player) source).removeItem(item);
        }



        return succeeded;
    }
}
