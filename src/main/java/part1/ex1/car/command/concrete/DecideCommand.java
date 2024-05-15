package part1.ex1.car.command.concrete;

import part1.ex1.car.CarAgent;
import part1.ex1.car.command.CarCommand;

import java.util.Optional;

public class DecideCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.setSelectedAction(Optional.empty());
        carAgent.decide();
    }
}
