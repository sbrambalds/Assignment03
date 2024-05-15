package part1.ex1.simulation;

import part1.ex1.car.AbstractAgent;
import part1.ex1.car.CarAgent;
import part1.ex1.inspector.road.RoadSimStatistics;
import part1.ex1.inspector.stepper.Stepper;
import part1.ex1.inspector.timeStatistics.TimeStatistics;
import part1.ex1.road.AbstractEnvironment;
import part1.ex1.simulation.listener.ModelSimulationListener;
import part1.ex1.simulation.listener.ViewSimulationListener;
import part1.ex1.synchronizers.monitor.startStop.StartStopMonitor;
import part1.ex1.synchronizers.monitor.startStop.StartStopMonitorImpl;
import part1.ex1.synchronizers.worker.master.MasterWorker;
import part1.ex1.synchronizers.worker.master.MultiWorkerGeneric;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for defining concrete simulations
 */
public abstract class AbstractSimulation extends Thread implements InspectorSimulation {

    /* environment of the simulation */
    private AbstractEnvironment env;

    /* list of the agents */
    private final List<AbstractAgent> agents;

    /* logical time step */
    private int dt;

    /* initial logical time */
    private int t0;

    /* in the case of sync with wall-time */
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;

    /* simulation listeners */
    private final List<ModelSimulationListener> modelListeners;
    private final List<ViewSimulationListener> viewListeners;

    // Master Worker
    private MasterWorker masterWorker;

    // Model
    private final StartStopMonitor startStopMonitorSimulation;
    private final RoadSimStatistics roadStatistics;
    private final TimeStatistics timeStatistics;
    private final Stepper stepper;

    protected AbstractSimulation() {
        this.agents = new ArrayList<>();
        this.modelListeners = new ArrayList<>();
        this.viewListeners = new ArrayList<>();

        this.startStopMonitorSimulation = new StartStopMonitorImpl();
        this.roadStatistics = new RoadSimStatistics();
        this.timeStatistics = new TimeStatistics();
        this.stepper = new Stepper();

        this.masterWorker = new MultiWorkerGeneric(20);

        this.toBeInSyncWithWallTime = false;
        this.setupModelListener();
        this.startStopMonitorSimulation.pause();
        this.start();
    }

    /**
     * Method used to configure the simulation, specifying env and agents
     */
    public abstract void setup();

    private void setupModelListener() {
        this.addModelListener(this.roadStatistics);
    }

    @Override
    public AbstractEnvironment environment() {
        return this.env;
    }
    @Override
    public List<AbstractAgent> agents() {
        return this.agents;
    }
    @Override
    public Stepper stepper() {
        return this.stepper;
    }
    @Override
    public StartStopMonitor startStopMonitor() {
        return this.startStopMonitorSimulation;
    }
    @Override
    public TimeStatistics timeStatistics() {
        return this.timeStatistics;
    }
    @Override
    public RoadSimStatistics roadStatistics() {
        return this.roadStatistics;
    }
    @Override
    public void setMasterWorker(final MasterWorker masterWorker) {
        this.masterWorker = masterWorker;
    }

    /**
     * Method running the simulation for a number of steps,
     * using a sequential approach
     */
    @Override
    public void run() {
        this.startStopMonitorSimulation.awaitUntilPlay();
        this.timeStatistics.setStartWallTime();

        /* initialize the env and the agents inside */
        int t = this.t0;

        this.env.init();
        for (final var a : this.agents) {
            a.init(this.env);
        }

        this.notifyReset(t);

        long timePerStep = 0;

        while (this.stepper.hasMoreSteps()) {
            this.startStopMonitorSimulation.awaitUntilPlay();
            this.timeStatistics.setCurrentWallTime(System.currentTimeMillis());

            /* make a step */
            this.env.step(this.dt);
            this.masterWorker.execute(this.dt);

            t += this.dt;

            this.notifyStepDone(t);

            this.stepper.increaseStep();
            timePerStep += System.currentTimeMillis() - this.timeStatistics.currentWallTime();

            if (this.toBeInSyncWithWallTime) {
                this.syncWithWallTime();
            }
        }

        this.timeStatistics.setEndWallTime(System.currentTimeMillis());
        this.timeStatistics.setAverageTimeForStep((double) timePerStep / this.stepper.totalStep());

        System.out.println("COMPLETED IN: " + this.timeStatistics().totalWallTime() + " ms");
        System.out.println("AVERAGE TIME PER STEP: " + this.timeStatistics().averageTimeForStep() + " ms");
        this.notifyEnd();
    }

    /* methods for configuring the simulation */
    protected void setupTimings(final int t0, final int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(final int nCyclesPerSec) {
        this.toBeInSyncWithWallTime = true;
        this.nStepsPerSec = nCyclesPerSec;
    }
    protected void setupEnvironment(final AbstractEnvironment env) {
        this.env = env;
    }

    protected void addAgent(final AbstractAgent agent) {
        this.agents.add(agent);
        this.masterWorker.addCarAgent((CarAgent) agent);
    }

    // listener
    // adders
    public void addModelListener(final ModelSimulationListener listener) {
        this.modelListeners.add(listener);
    }
    public void addViewListener(final ViewSimulationListener listener) {
        this.viewListeners.add(listener);
    }
    // actions
    private void notifyReset(final int t0) {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyInit(t0, this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyInit(t0, this);
        }
        this.masterWorker.setup();
    }
    private void notifyStepDone(final int t) {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyStepDone(t, this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyStepDone(t, this);
        }
    }
    private void notifyEnd() {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyEnd(this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyEnd(this);
        }
        this.masterWorker.terminateExecution();
    }

    /* method to sync with wall time at a specified step rate */

    private void syncWithWallTime() {
        try {
            final long newWallTime = System.currentTimeMillis();
            final long delay = 1000 / this.nStepsPerSec;
            final long wallTimeDT = newWallTime - this.timeStatistics.currentWallTime();
            if (wallTimeDT < delay) {
                Thread.sleep(delay - wallTimeDT);
            }
        } catch (final Exception ex) {
        }
    }



}
