package part1.ex1.simulation;

import part1.ex1.car.AbstractAgent;
import part1.ex1.inspector.road.RoadSimStatistics;
import part1.ex1.inspector.stepper.Stepper;
import part1.ex1.inspector.timeStatistics.TimeStatistics;
import part1.ex1.road.AbstractEnvironment;
import part1.ex1.synchronizers.monitor.startStop.StartStopMonitor;
import part1.ex1.synchronizers.worker.master.MasterWorker;

import java.util.List;

public interface InspectorSimulation {

    Stepper stepper();

    StartStopMonitor startStopMonitor();

    TimeStatistics timeStatistics();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<AbstractAgent> agents();

    void setMasterWorker(final MasterWorker masterWorker);

    void setup();
}
