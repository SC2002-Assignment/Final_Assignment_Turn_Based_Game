package engine;

import actions.Action;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import java.util.ArrayList;
import java.util.List;
import strategies.TurnOrderStrategy;
import ui.BattleInputProvider;
import ui.BattleObserver;

// main battle loop, handles rounds and turns
// doesnt know anything abt CLI - all display goes thru BattleObserver
// all player input comes thru BattleInputProvider
// no instanceof Player checks here - uses isPlayerControlled() instead (DIP + OCP)
public class BattleEngine {

    private List<Combatant> allCombatants = new ArrayList<>();
    private TurnOrderStrategy turnOrderStrategy;
    private Level level;
    private int roundNum = 0;

    // injected interfaces, not concrete CLI - engine works w any UI this way
    private BattleInputProvider inputProvider;
    private BattleObserver observer;

    public BattleEngine(TurnOrderStrategy turnOrderStrategy, Level level,
                        BattleInputProvider inputProvider, BattleObserver observer) {
        this.turnOrderStrategy = turnOrderStrategy;
        this.level = level;
        this.inputProvider = inputProvider;
        this.observer = observer;
    }

    public void addCombatants(List<? extends Combatant> list) {
        allCombatants.addAll(list);
    }

    public void runRound() {
        roundNum++;

        // notify observer to handle all display, engine doesnt print anything directly
        observer.onRoundStart(allCombatants, roundNum);

        List<Combatant> turnOrder = turnOrderStrategy.getTurnOrder(allCombatants);
        observer.onTurnOrder(turnOrder);

        for (int i = 0; i < turnOrder.size(); i++) {
            Combatant current = turnOrder.get(i);

            if (!current.isAlive()) continue;

            // stun check comes FIRST before ticking, so the stunned turn is counted properly
            if (current.isStunned()) {
                observer.onStunSkip(current);
                // tick and decrement after the turn is actually skipped, not before
                current.tickEffects();
                current.decrementCooldown();
                continue;
            }

            // tick effects and cooldown at the start of this combatants turn (non-stunned)
            current.tickEffects();
            current.decrementCooldown();

            //System.out.println("" + current.getName() + " turn, isPlayerControlled=" + current.isPlayerControlled());

            // isPlayerControlled() replaces instanceof Player - no UI coupling needed here
            if (current.isPlayerControlled()) {
                // keep reprompting until player picks a valid action
                boolean actionSucceeded = false;
                while (!actionSucceeded) {
                    Action action = inputProvider.getAction(current, getAliveEnemies());
                    if (action != null) {
                        actionSucceeded = action.execute(current, getAliveEnemies());
                    }
                }
            } else {
                // enemy picks action automatically via its own strategy
                Enemy enemy = (Enemy) current;
                Action action = enemy.chooseAction(getAlivePlayers());
                if (action != null) {
                    action.execute(current, getAlivePlayers());
                }
            }

            // check if backup wave shld spawn after each action
            // observer passed thru so Level can notify without printing directly
            level.checkBackupSpawn(allCombatants, this, observer);

            if (isGameOver()) return;
        }

        observer.onRoundEnd(allCombatants, roundNum);
    }

    public boolean isGameOver() {
        return getAliveEnemies().size() == 0 || getAlivePlayers().size() == 0;
    }

    // kept for backwards compat
    public boolean checkEndCondition() {
        return isGameOver();
    }

    public List<Enemy> getAliveEnemies() {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < allCombatants.size(); i++) {
            Combatant c = allCombatants.get(i);
            if (c instanceof Enemy && c.isAlive()) {
                result.add((Enemy) c);
            }
        }
        return result;
    }

    public List<Player> getAlivePlayers() {
        List<Player> result = new ArrayList<>();
        for (int i = 0; i < allCombatants.size(); i++) {
            Combatant c = allCombatants.get(i);
            if (c instanceof Player && c.isAlive()) {
                result.add((Player) c);
            }
        }
        return result;
    }

    public Player getAlivePlayer() {
        List<Player> alive = getAlivePlayers();
        if (alive.size() == 0) return null;
        return alive.get(0);
    }

    public List<Combatant> getAllCombatants() {
        return allCombatants;
    }

    public int getRoundCount() {
        return roundNum;
    }
}
