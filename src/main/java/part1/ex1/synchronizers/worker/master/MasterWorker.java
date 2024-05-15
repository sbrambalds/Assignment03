package part1.ex1.synchronizers.worker.master;

import part1.ex1.car.CarAgent;

public interface MasterWorker {

    void setup();

    void execute(int dt);

    void addCarAgent(final CarAgent carAgent);

    void startSimulation();

    boolean hasCommands();

    void callNextTaskCommand();

    void terminateExecution();

}
