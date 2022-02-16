package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
@Builder
public class Neuron {
    NeuronType type;

    @ToString.Exclude
    @Builder.Default
    List<Synapse> inputs = new ArrayList<>(); // axons

    @ToString.Exclude
    @Builder.Default
    List<Synapse> outputs = new ArrayList<>(); // dendrites
    Position position;
    int potential; // inner level of signal
    //int threshold; // upper bound of potential, when neuron fires
    boolean touched;
    @Builder.Default
    int refractoryTicks = 0; // amount of ticks to skip due to refractory
    Consumer<Neuron> onFire;
    Consumer<Neuron> onNeedTick;


    public void rotateAroundPoint(Position origin, double[] rotation) {
        // TODO : move to new position
    }

    public void moveByOrigin(Position origin, int[] position) {
        this.position.getCoordinatesXYZ()[0] = this.position.getCoordinatesXYZ()[0] + position[0] - origin.getCoordinatesXYZ()[0];
        this.position.getCoordinatesXYZ()[1] = this.position.getCoordinatesXYZ()[1] + position[1] - origin.getCoordinatesXYZ()[1];
        this.position.getCoordinatesXYZ()[2] = this.position.getCoordinatesXYZ()[2] + position[2] - origin.getCoordinatesXYZ()[2];
    }

    public void addPotential(int signalStrength) {
        potential += signalStrength;
        potential = Math.min(100, potential);
    }

    public void input(int signalStrength) {
        /*if (refractoryTicks > 0) {
            // can't get more potential while on refractory
        } else {*/
            addPotential(signalStrength);
        /*}*/
    }

    public void input(Neuron input, int signalStrength) {
        input(signalStrength);
    }

    public void input(int inputIndex, int signalStrength) {
        input(signalStrength);
    }

    // each tick is 1ms...
    public void tick() {
        // remove redundant connections
        outputs = outputs.stream().filter(x -> x.getStrength() != 0).collect(Collectors.toList());

        if (refractoryTicks > 0) {
            // can't fire while on refractory
            refractoryTicks--;
        } else {
            //if (potential >= calculateThreshold()) {
            //if (potential >= 10) {
            if (potential >= outputs.size()) {
                refractoryTicks = calculateRefractory(potential);
                fire();
            }
        }

        for(Synapse output: outputs) {
            output.tick();
        }

        // remove redundant connections
        outputs = outputs.stream().filter(x -> x.getStrength() != 0).collect(Collectors.toList());
    }

    private int calculateRefractory(int potential) {
        // TODO : formula for refractory period, minimum is 1 tick
        return (int)Math.round((double)potential / 10.0) + 1;
    }


    public int calculateThreshold() {
        // threshold is sum of all out synapses to fire
        return outputs.stream().mapToInt(x -> Math.abs(x.getStrength())).reduce(0, Integer::sum);
    }

    private void fire() {
        int outputWeight = outputs.stream().map(x -> x.getStrength()).reduce(0, Integer::sum);
        for (Synapse output: outputs) {
            output.fire((int)Math.round((double)potential * ((double)output.getStrength() / (double)outputWeight)));
        }
        potential = 0;

        for (Synapse input : inputs) {
            input.targetFired();
        }
        if (onFire != null) {
            onFire.accept(this);
        }
    }

    public int getSignalOutputStrength(Neuron output) {
        // TODO : add output signal strength function
        return potential;
    }

    public void addOutput(Synapse synapse) {
        if (outputs.stream().noneMatch(x -> x.getTo() == synapse.getTo() && x.getFrom() == synapse.getFrom())) {
            outputs.add(synapse);
        }
    }

    public void addInput(Synapse synapse) {
        if (inputs.stream().noneMatch(x -> x.getTo() == synapse.getTo() && x.getFrom() == synapse.getFrom())) {
            inputs.add(synapse);
        }
    }

    public void needTick() {
        if (onNeedTick != null) {
            onNeedTick.accept(this);
        }
    }

    public boolean equals(Object other){
        if (!(other instanceof Neuron)) {
            return false;
        }
        Neuron otherNeuron = (Neuron)other;

        return this == otherNeuron;
    }
}
