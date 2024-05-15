package part1.ex1.car.command.concrete;

import part1.ex1.car.CarAgent;
import part1.ex1.car.command.CarCommand;

public class ActionCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.selectedAction().ifPresent(carAgent::doAction);
    }
}
