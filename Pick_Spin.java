package flax_spin;

import flax_spin.tasks.Pick;
import flax_spin.tasks.Walk;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Constants;
import flax_spin.Task;
import flax_spin.tasks.Bank;
import flax_spin.tasks.Spin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="hsu PICKSPIN", description = "Pick Flax and Spin to Bow Strings in Seer's Village", properties = "author=hsu; topic=999; client=4;")

public class Pick_Spin extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    public String activeTask = "task";

    @Override
    public void start() {

//        String userOptions[] = {"Bank", "Powerchop"};
//        String userChoice = ""+(String)JOptionPane.showInputDialog(null, "Bank or Powerchop", "Willow Chopper", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

//        if (userChoice == "Bank") {
//            taskList.add(new Bank(ctx));
//        } else {
//            ctx.controller.stop();
//        }
        taskList.add(new Spin(ctx));
        taskList.add(new Bank(ctx));
        taskList.add(new Pick(ctx));
        taskList.add(new Walk(ctx));
//        taskList.add(new antibot(ctx));

    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (ctx.controller.isStopping()) {
                System.out.println("Stopping: " + task);
                break;
            }

            if (task.activate()) {
                activeTask = task.toString();
                System.out.println("Running: " + task);
                task.execute();
                break;
            }
        }
    }

    public void stop() {
        System.out.println("Stopped");
        ctx.controller.stop();
    }

    @Override
    public void repaint(Graphics graphics) {

    }
}
