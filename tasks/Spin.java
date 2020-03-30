package flax_spin.tasks;

import flax_spin.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Spin extends Task {

    public static int FLAX_ITEM = 1779;
    public static int WHEEL = 25824;
    public static Tile WHEEL_TILE = new Tile(2715, 3471, 0);

    public Spin(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.isFull() && ctx.inventory.id(FLAX_ITEM).count() > 0 && ctx.players.local().tile().floor() == 1;
    }

    @Override
    public void execute() {

        GameObject spinningWheel = ctx.objects.select().id(WHEEL).nearest().poll();

        if (!spinningWheel.inViewport()){
            ctx.camera.turnTo(spinningWheel);
        }

        spinningWheel.interact("Spin");

        Condition.sleep(350);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        }, 100, 20);

        if (ctx.widgets.widget(270).valid()) {
            ctx.input.send("3");
        } else {
            spinningWheel.interact("Spin");
        }

        // To prevent spamming between spins (let me know of a better way to do this, it doesn't always work but is way better than clicking every time)...
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (ctx.players.local().animation() == -1) {
                    Condition.sleep(750);
                    if (ctx.players.local().animation() == -1) {
                        Condition.sleep(750);
                        return ctx.players.local().animation() == -1;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }, 500, 50);
    }
}
