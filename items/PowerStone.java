package items;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

// special item that lets the player trigger their special skill instantly without waiting for cooldowns
// this class is integrated with useitem and player.useskill()
// can work without changing the engine or ui code
public class PowerStone implements Item {

    // no-arg constructor because of CLI and GameManager
    // create PowerStone directly using `new PowerStone()`
    public PowerStone() {
        // no need state
    }

    // this overload is called by useitem when the power stone is used
    // targets and targetindex are injected at action-build time
    // returns whether the skill actually succeeded
    public boolean use(
            Combatant source,
            List<? extends Combatant> targets,
            int targetIndex
    ) {
        // power stones for players
        if (!(source instanceof Player)) {
            return false;
        }

        Player player = (Player) source;

        // delegate the actual logic to the player's existing skill system
        // avoids duplicating special-skill logic here
        return player.useSkill(targets, targetIndex);
    }

    // required by the item interface
    // not used for power stone logic since it needs extra context
    @Override
    public void use(Combatant source) {
        // intentionally left empty
    }

    @Override
    public String getName() {
        return "Power Stone";
    }
}