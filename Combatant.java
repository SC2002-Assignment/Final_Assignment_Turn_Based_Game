package combatants;

import effects.StatusEffect;
import java.util.ArrayList;
import java.util.List;

// base class for all combatants, both player and enemy share this
// holds stats, effects, cooldown tracking and common battle methods
// chooseAction is NOT here anymore - enemies have their own via Enemy, players go thru BattleInputProvider
public abstract class Combatant {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected int atk;
    protected int def;
    protected int spd;

    // tracks special skill cooldown
    protected int skillCooldown;

    // status flags
    protected boolean isStunned;
    protected boolean isInvulnerable;

    // list of active effects on this combatant
    protected List<StatusEffect> activeEffects;

    public Combatant(String name, int hp, int atk, int def, int spd) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.skillCooldown = 0;
        this.isStunned = false;
        this.isInvulnerable = false;
        this.activeEffects = new ArrayList<>();
    }

    public String getName()         { return name; }
    public int getHp()              { return hp; }
    public int getMaxHp()           { return maxHp; }
    public int getAttack()          { return atk; }
    public int getDefense()         { return def; }
    public int getSpeed()           { return spd; }
    public int getCooldown()        { return skillCooldown; }
    public boolean isAlive()        { return hp > 0; }
    public boolean isStunned()      { return isStunned; }
    public boolean isInvulnerable() { return isInvulnerable; }
    public List<StatusEffect> getEffects() { return activeEffects; }

    // engine uses this to decide whether to ask BattleInputProvider or call chooseAction
    // avoids instanceof Player checks in BattleEngine
    public abstract boolean isPlayerControlled();

    // hp cant go below 0
    // if invulnerable (smoke bomb active), skip dmg and print msg
    public void takeDamage(int dmg) {
        if (isInvulnerable) {
            System.out.println("> " + name + " is protected by Smoke Bomb -- 0 damage!");
            return;
        }
        hp = hp - dmg;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(int amount) {
        hp = hp + amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    // used by wizard arcane blast kill bonus
    public void increaseAttack(int amount) {
        atk = atk + amount;
    }

    // used when defend buff is applied
    public void increaseDefense(int amount) {
        def = def + amount;
    }

    // used when defend buff expires
    public void decreaseDefense(int amount) {
        def = def - amount;
        if (def < 0) def = 0;
    }

    public void setCooldown(int cd) {
        skillCooldown = cd;
    }

    // cooldown only goes down when this combatant takes their turn
    public void decrementCooldown() {
        if (skillCooldown > 0) {
            skillCooldown--;
        }
    }

    public void setStunned(boolean val) {
        isStunned = val;
    }

    public void setInvulnerable(boolean val) {
        isInvulnerable = val;
    }

    // add effect and apply it right away
    public void applyEffect(StatusEffect effect) {
        activeEffects.add(effect);
        effect.apply(this);
    }

    // tick all effects and clean up expired ones
    // called at start of each combatants turn in BattleEngine
    public void tickEffects() {
        for (int i = 0; i < activeEffects.size(); i++) {
            activeEffects.get(i).tick();
        }
        // remove expired effects by rebuilding the list
        //System.out.println("tickEffects on " + name + " effects before=" + activeEffects.size());
        List<StatusEffect> stillActive = new ArrayList<>();
        for (int i = 0; i < activeEffects.size(); i++) {
            if (!activeEffects.get(i).isExpired()) {
                stillActive.add(activeEffects.get(i));
            }
        }
        activeEffects = stillActive;
        //System.out.println("effects after=" + activeEffects.size());
    }
}
