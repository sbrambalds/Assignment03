package part1.ex1.road;

import part1.ex1.simulation.engineseq.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(double distance) implements Action {}
