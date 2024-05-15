package part1.ex1.car.command.invoker;

import part1.ex1.car.command.CommandCar;

public interface InvokerCommand {

    void setup(final int dt);

    void execute(final CommandCar command);

}
