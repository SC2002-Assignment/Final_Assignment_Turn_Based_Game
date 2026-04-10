package ui;

import combatants.Combatant;
import combatants.Enemy;
import effects.StatusEffect;
import java.util.List;

// CLI implementation of BattleObserver
// handles all round/turn display output so BattleEngine and Level dont need to touch CLI directly
public class CLIBattleObserver implements BattleObserver {

    public void onRoundStart(List<Combatant> combatants, int roundNumber) {
        System.out.println("\n=== Round " + roundNumber + " ===");
        System.out.println("--- Status at start of Round " + roundNumber + " ---");

        //System.out.println("num combatants: " + combatants.size());

        for (int i = 0; i < combatants.size(); i++) {
            Combatant c = combatants.get(i);
            if (!c.isAlive()) continue;

            // build effect names string
            String effectNames = "none";
            List<StatusEffect> effects = c.getEffects();
            if (effects.size() > 0) {
                String tmp = "";
                for (int j = 0; j < effects.size(); j++) {
                    String raw = effects.get(j).getClass().getSimpleName();
                    tmp += CLI.getEffectLabel(raw);
                    if (j < effects.size() - 1) tmp += ", ";
                }
                effectNames = tmp;
            }

            String stunStr   = c.isStunned()      ? " [Stunned]"             : "";
            String invulnStr = c.isInvulnerable()  ? " [Smoke Screen active]" : "";

            System.out.println("[" + c.getName() + "] "
                    + "HP: " + c.getHp() + "/" + c.getMaxHp()
                    + " | ATK: " + c.getAttack()
                    + " | DEF: " + c.getDefense()
                    + " | SPD: " + c.getSpeed()
                    + " | CD: " + c.getCooldown()
                    + " | Effects: " + effectNames
                    + stunStr + invulnStr);
        }
    }

    public void onTurnOrder(List<Combatant> order) {
        //System.out.println("turn order size: " + order.size());
        System.out.print("Turn order: ");
        for (int i = 0; i < order.size(); i++) {
            System.out.print(order.get(i).getName() + " (SPD " + order.get(i).getSpeed() + ")");
            if (i < order.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    public void onStunSkip(Combatant combatant) {
        //System.out.println(combatant.getName() + " stun duration remaining: " + combatant.getEffects().size());
        System.out.println("> " + combatant.getName() + " is Stunned and skips their turn!");
    }

    public void onRoundEnd(List<Combatant> combatants, int roundNumber) {
        System.out.println("--- End of Round " + roundNumber + " ---");
        System.out.println("HP remaining:");
        for (int i = 0; i < combatants.size(); i++) {
            Combatant c = combatants.get(i);
            String label = c.isAlive()
                    ? c.getHp() + "/" + c.getMaxHp()
                    : "eliminated";
            System.out.println("  " + c.getName() + ": " + label);
        }
        //System.out.println("round " + roundNumber + " done");
    }

    // called by Level via the engine when a backup wave spawns
    // Level never prints directly - all output goes thru here
    public void onBackupWave(List<Enemy> wave) {
        //System.out.println("backup wave size: " + wave.size());
        System.out.println("\n>>> Backup wave appeared!");
        for (int i = 0; i < wave.size(); i++) {
            System.out.println("> " + wave.get(i).getName() + " joined the battle!");
        }
    }
}
