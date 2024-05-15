package part1.ex1.synchronizers.worker.slave;

import part1.ex1.car.CarAgent;
import part1.ex1.car.command.CarCommand;

import java.util.List;

public class WorkerCarBarrier implements Worker {
    private final List<CarAgent> agents;
    private CarCommand command;

    public WorkerCarBarrier(final List<CarAgent> carAgents) {
        super();
        this.agents = carAgents;
    }

    @Override
    public void setCarCommand(final CarCommand command) {
        this.command = command;
    }

    @Override
    public Void call() {
        System.out.print("HIT ");
        this.agents.forEach(this.command::execute);
        return null;
    }

}

