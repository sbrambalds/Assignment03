package part1.ex1.synchronizers.worker.slave;

import part1.ex1.car.command.CarCommand;

import java.util.concurrent.Callable;

public interface Worker extends Callable<Void> {

    void setCarCommand(CarCommand command);

}
