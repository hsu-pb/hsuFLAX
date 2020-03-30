package flax_spin.tasks;

import flax_spin.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import java.util.concurrent.Callable;

public class Bank extends Task {

    public static Tile BANK_TILE = new Tile(2726, 3493, 0);
    public static int FLAX_ITEM = 1779;

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.isFull() && ctx.inventory.id(FLAX_ITEM).count() == 0 && ctx.players.local().tile().distanceTo(BANK_TILE) < 6;
    }

    @Override
    public void execute() {
        if (!ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }

        if (!ctx.bank.inViewport()) {
            ctx.camera.turnTo(ctx.bank.nearest());
        }

        if (ctx.bank.open()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 200, 10);
            Condition.sleep(Random.nextInt(10, 200));
        }

        final int inventCount = ctx.inventory.select().count();
        if (ctx.bank.depositInventory()) {
            // Went with this method instead of checking if invent count is zero in case unbankable items in inventory
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != inventCount;
                }
            });
        }

//        if (ctx.bank.close()) {
//            Condition.wait(new Callable<Boolean>() {
//                @Override
//                public Boolean call() throws Exception {
//                    return !ctx.bank.opened();
//                }
//            }, 200, 10);
//        }
    }
}
