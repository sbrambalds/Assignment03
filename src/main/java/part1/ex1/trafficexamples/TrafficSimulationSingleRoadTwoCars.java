package part1.ex1.trafficexamples;

import part1.ex1.car.CarAgent;
import part1.ex1.car.CarAgentBasic;
import part1.ex1.road.Road;
import part1.ex1.road.RoadsEnv;
import part1.ex1.road.core.P2d;
import part1.ex1.simulation.AbstractSimulation;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadTwoCars() {
		super();
	}
	
	public void setup() {
		
		final int t0 = 0;
		final int dt = 1;
		
		this.setupTimings(t0, dt);
		
		final RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		
		final Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));
		final CarAgent car1 = new CarAgentBasic("car-1", env, r, 0, 0.1, 0.2, 8);
		this.addAgent(car1);		
		final CarAgent car2 = new CarAgentBasic("car-2", env, r, 100, 0.1, 0.1, 7);
		this.addAgent(car2);
		
		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(25);
	}	
	
}
