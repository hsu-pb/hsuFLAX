package flax_spin.tasks;

import flax_spin.Task;
import flax_spin.Walker;
import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    // last: , new Tile(2715, 3471, 1), new Tile(2711, 3471, 1)
    public static final Tile[] pathToWheel = {new Tile(2739, 3441, 0), new Tile(2735, 3442, 0), new Tile(2732, 3445, 0), new Tile(2731, 3449, 0), new Tile(2728, 3452, 0), new Tile(2724, 3455, 0), new Tile(2723, 3459, 0), new Tile(2721, 3463, 0), new Tile(2719, 3467, 0), new Tile(2717, 3471, 0), new Tile(2715, 3471, 0), new Tile(2715, 3471, 1)};
    public static final Tile[] pathToBank = {new Tile(2711, 3471, 1), new Tile(2714, 3470, 0), new Tile(2717, 3473, 0), new Tile(2717, 3477, 0), new Tile(2717, 3481, 0), new Tile(2720, 3484, 0), new Tile(2724, 3485, 0), new Tile(2725, 3489, 0), new Tile(2726, 3493, 0)};
    public static final Tile[] pathToFlax = {new Tile(2725, 3492, 0), new Tile(2725, 3488, 0), new Tile(2727, 3484, 0), new Tile(2727, 3480, 0), new Tile(2729, 3476, 0), new Tile(2730, 3472, 0), new Tile(2730, 3468, 0), new Tile(2730, 3464, 0), new Tile(2731, 3460, 0), new Tile(2731, 3456, 0), new Tile(2730, 3452, 0), new Tile(2733, 3449, 0), new Tile(2734, 3445, 0), new Tile(2738, 3442, 0)};

    public static int FLAX_ITEM = 1779;

    private final Walker walker = new Walker(ctx);

    public Walk(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(FLAX_ITEM).count() > 27 && pathToWheel[pathToWheel.length-1].distanceTo(ctx.players.local()) > 5) ||
                (ctx.inventory.select().count() > 27 && ctx.inventory.select().id(FLAX_ITEM).count() == 0 && pathToBank[pathToBank.length-1].distanceTo(ctx.players.local()) > 6) ||
                (ctx.inventory.select().count() < 28 && pathToFlax[pathToFlax.length-1].distanceTo(ctx.players.local().tile()) > 6);
    }

    @Override
    public void execute() {
        if (!ctx.movement.running()) {
            if (ctx.movement.energyLevel() > Random.nextInt(10, 65)) {
                ctx.movement.running(true);
            }
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if (ctx.inventory.select().id(FLAX_ITEM).count() > 27) {
//                System.out.println("Distance " + pathToWheel[pathToWheel.length-1].distanceTo(ctx.players.local()));
//                System.out.println("Player " + ctx.players.local().tile());
                walker.walkPath(pathToWheel);
            } else if (ctx.inventory.select().count() > 27 && ctx.inventory.select().id(FLAX_ITEM).count() == 0) {
                walker.walkPath(pathToBank);
            } else {
                walker.walkPath(pathToFlax);
            }
        }
    }
}
