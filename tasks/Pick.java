package flax_spin.tasks;

import flax_spin.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Pick extends Task {

    public static int FLAX_ID = 14896;
    public static Area FLAX_AREA = new Area(new Tile(2737, 3445, 0), new Tile(2743, 3437, 0));

    public Pick(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.inventory.isFull() && ctx.players.local().tile().distanceTo(FLAX_AREA.getCentralTile()) < 5;
    }

    @Override
    public void execute() {
        if (ctx.menu.containsAction("Pick")) {
            ctx.input.click(ctx.input.getLocation(), true);
            Condition.sleep(Random.nextInt(35, 60));
        } else {
            GameObject flax = ctx.objects.select().id(FLAX_ID).nearest().poll();
            flax.interact("Pick");

            Condition.sleep(Random.nextInt(35, 60));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion();
                }
            }, 100, 30);
        }
    }
}
