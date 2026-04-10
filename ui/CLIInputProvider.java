package ui;

import actions.Action;
import actions.BasicAttack;
import actions.Defend;
import actions.SpecialSkill;
import actions.UseItem;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import items.Item;
import items.PowerStone;
import java.util.List;

// CLI implementation of BattleInputProvider
// handles the action menu and all player input during battle
// BattleEngine talks to this thru the BattleInputProvider interface, never directly
public class CLIInputProvider implements BattleInputProvider {

    // called by BattleEngine each time the player needs to pick an action
    // returns null if the input was invalid so the engine can reprompt
    public Action getAction(Combatant combatant, List<Enemy> enemies) {
        // safe cast - engine only calls this for player combatants
        Player player = (Player) combatant;

        //System.out.println("getAction called for: " + player.getName());
        //System.out.println("enemies alive: " + enemies.size());

        System.out.println("\n" + player.getName() + "'s turn:");
        System.out.println("1. Basic Attack");
        System.out.println("2. Defend (+10 DEF for this turn and next)");
        System.out.println("3. Special Skill ("
                + (player.getCooldown() == 0 ? "ready" : player.getCooldown() + " turns left") + ")");

        boolean hasItems = player.hasItems();
        int maxChoice = 3;

        if (hasItems) {
            System.out.print("4. Use Item (");
            List<Item> inv = player.getItems();
            for (int i = 0; i < inv.size(); i++) {
                System.out.print(inv.get(i).getName());
                if (i < inv.size() - 1) System.out.print(", ");
            }
            System.out.println(")");
            maxChoice = 4;
        }

        int choice = CLI.readChoice(1, maxChoice);
        //System.out.println("player picked option: " + choice);

        if (choice == 2) return new Defend();

        if (choice == 3) {
            // check cooldown b4 asking for a target so player doesnt pick a target for nothing
            if (player.getCooldown() > 0) {
                System.out.println("> Special skill still on cooldown ("
                        + player.getCooldown() + " turns left), pick another action");
                return null;
            }
            // player decides target via their own controller, not by instanceof here
            return player.buildSpecialSkillAction(enemies);
        }

        if (choice == 4 && hasItems) {
            System.out.println("Select item:");
            List<Item> inv = player.getItems();
            for (int i = 0; i < inv.size(); i++) {
                System.out.println((i + 1) + ". " + inv.get(i).getName());
            }
            int itemChoice = CLI.readChoice(1, inv.size());

            // grab the actual item ref from inventory so removeItem can find it
            Item selectedItem = inv.get(itemChoice - 1);
            //System.out.println("selected: " + selectedItem.getName());

            // powerstone needs live targets passed in at action-build time
            if (selectedItem instanceof PowerStone) {
                // player subclass decides how to pick its target (warrior picks one, wizard doesnt)
                return player.buildPowerStoneAction(selectedItem, enemies);
            }

            return new UseItem(selectedItem);
        }

        // basic attack - player subclass decides how to pick a target
        if (enemies == null || enemies.size() == 0) {
            System.out.println("No enemies available");
            return null;
        }
        //System.out.println("building basic attack action");
        return player.buildBasicAttackAction(enemies);
    }
}
