package part1.ex1.car.command.invoker;

import part1.ex1.car.CarAgent;
import part1.ex1.car.command.CarCommand;
import part1.ex1.car.command.CommandCar;
import part1.ex1.car.command.concrete.ActionCommand;
import part1.ex1.car.command.concrete.DecideCommand;
import part1.ex1.car.command.concrete.SenseCommand;

import java.util.HashMap;
import java.util.Map;

public class InvokerCarCommandImpl implements InvokerCommand {
    private final CarAgent car;
    private final Map<CommandCar, CarCommand> commands;

    public InvokerCarCommandImpl(final CarAgent car) {
        this.car = car;
        this.commands = new HashMap<>();
        this.commands.put(CommandCar.SENSE, new SenseCommand());
        this.commands.put(CommandCar.DECIDE, new DecideCommand());
        this.commands.put(CommandCar.ACTION, new ActionCommand());
    }

    @Override
    public void setup(final int dt) {
        this.car.setTimeDt(dt);
    }

    @Override
    public void execute(final CommandCar command) {
        this.commands.get(command).execute(this.car);
    }
}
