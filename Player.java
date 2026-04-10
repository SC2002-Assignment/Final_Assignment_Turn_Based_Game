package combatants;

import actions.Action;
import actions.BasicAttack;
import actions.SpecialSkill;
import actions.UseItem;
import items.Item;
import java.util.ArrayList;
import java.util.List;
import ui.CLI;

// base class for all player combatants
// players can hold items and have a special skill
// action building methods live here so CLIInputProvider doesnt need instanceof Warrior/Wizard checks
public abstract class Player extends Combatant {

    // player inventory
    protected List<Item> inventory;

    public Player(String name, int hp, int atk, int def, int spd) {
        super(name, hp, atk, def, spd);
        this.inventory = new ArrayList<>();
    }

    // players are human controlled, engine routes them thru BattleInputProvider
    public boolean isPlayerControlled() {
        return true;
    }

    // check if player still has items
    public boolean hasItems() {
        return inventory.size() > 0;
    }

    // remove item after use
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    // add item to inventory
    public void addItem(Item item) {
        inventory.add(item);
    }

    // get full inventory list
    public List<Item> getItems() {
        return inventory;
    }

    // each player subclass defines its own skill here
    // returns true if the skill actually did smthing, false if it failed
    // SpecialSkill.execute() uses this return val to decide whether to set cooldown
    // UseItem.execute() uses it to decide whether to consume the PowerStone
    public abstract boolean useSkill(List<? extends Combatant> targets, int targetIndex);

    // subclass decides whether it needs a target pick for basic attack (warrior does, wizard doesnt matter)
    // default just picks idx 0 - Warrior overrides to prompt the player
    public Action buildBasicAttackAction(List<? extends Combatant> enemies) {
        int idx = pickTarget(enemies);
        return new BasicAttack(idx);
    }

    // subclass decides how to build the special skill action (target needed or not)
    // Warrior overrides to prompt for a target, Wizard just hits all so idx 0 is fine
    public Action buildSpecialSkillAction(List<? extends Combatant> enemies) {
        return new SpecialSkill(0);
    }

    // subclass decides how to build the powerstone action (target needed or not)
    // Warrior overrides to prompt for a target
    public Action buildPowerStoneAction(Item stone, List<? extends Combatant> enemies) {
        return new UseItem(stone, enemies, 0);
    }

    // shared target selection prompt used by Warrior overrides
    // asks the player to pick from the enemy list and returns the 0-based idx
    protected int pickTarget(List<? extends Combatant> enemies) {
        System.out.println("Pick a target:");
        for (int i = 0; i < enemies.size(); i++) {
            Combatant e = enemies.get(i);
            System.out.println((i + 1) + ". " + e.getName()
                    + " (HP: " + e.getHp() + "/" + e.getMaxHp() + ")");
        }
        return CLI.readChoice(1, enemies.size()) - 1;
    }
}
