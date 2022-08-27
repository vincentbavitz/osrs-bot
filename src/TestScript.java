import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.event.impl.TickEvent;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.interactive.util.HealthBar;
import org.dreambot.launcher.Settings;

// Things to consider simulating
// Boredom -- increases as task continues for a long time. Each task has its own intensity.
// Wrist strain
//

@ScriptManifest(
        name = "Script Name",
        description = "My script description!",
        author = "Developer Name",
        version = 1.0,
        category = Category.WOODCUTTING
)
public class TestScript extends AbstractScript {
    private enum State {
        FIND_TREES, BANK, CHOP, WAIT
    };

    public void onStart() {}
    public void onExit() {}

    private State getState() {
        if (Inventory.isFull()) {
            return State.BANK;
        }

        if (Inventory.isEmpty()) {
            return State.CHOP;
        }

        return State.WAIT;
    }

    @Override
    public int onLoop() {
        MethodProvider.log(getState().toString());

        Player player = getLocalPlayer();

        switch (getState()) {
            case CHOP:
                GameObject tree = GameObjects.closest("Oak");
                if (tree != null) {
                    if (!player.isAnimating()) {
                        tree.interact("Chop down");
                    }
                }
                break;
            case BANK:
                if (player.isMoving()) {
                    return 1200;
                }

                Walking.walk(3162, 3489);
                break;
            case WAIT:
                sleep(300);
                break;
        }

        return Calculations.random(300, 900);

    }
}