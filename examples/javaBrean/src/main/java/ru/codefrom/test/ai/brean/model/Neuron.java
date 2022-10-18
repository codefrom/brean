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
    NeuronDescription description;

    @ToString.Exclude
    @Builder.Default
    List<Synapse> inputs = new ArrayList<>(); // axons

    @ToString.Exclude
    @Builder.Default
    List<Synapse> outputs = new ArrayList<>(); // dendrites
    int potential; // inner level of signal
    //int threshold; // upper bound of potential, when neuron fires
    boolean touched;
    @Builder.Default
    int refractoryTicks = 0; // amount of ticks to skip due to refractory
    Consumer<Neuron> onFire;
    Consumer<Neuron> onNeedTick;

    public void addPotential(int signalStrength) {
        potential += signalStrength;
    }

    public void input(int signalStrength) {
        if (refractoryTicks > 0) {
            // can't get more potential while on refractory
        } else {
            addPotential(signalStrength);
        }
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
        //outputs = outputs.stream().filter(x -> x.getStrength() != 0).collect(Collectors.toList());

        if (refractoryTicks > 0) {
            // can't fire while on refractory
            refractoryTicks--;
        } else {
            // resting potential is about –70 mV (in model is 0)
            // threshold potential is around –55 mV (in model is 15)
            // if potential more than threshold - fire
            if (potential >= description.getFireThreshold()) {
                // refractory period is about 1 ms (but fire process is about 2ms), so in model is 3
                refractoryTicks = description.getRefractoryPeriod();
                fire();
            } else {
                // if potential less than threshold - potential slowly moves to resting
                rest();
            }
        }

        for(Synapse output: outputs) {
            output.tick();
        }

        // remove redundant connections
        //outputs = outputs.stream().filter(x -> x.getStrength() != 0).collect(Collectors.toList());
    }

    private void rest() {
        // if not firing - potential slowly moves towards resting potential (0 in model)
        if (potential != 0) {
            if (potential > 0) {
                potential -= Math.min(potential, 1); // TODO: decrease by 1 each ms (why this value...)
            } else {
                potential -= Math.max(potential, -1);
            }
        }
    }

    //
    private void fire() {
        // potential moves to 0
        potential = 0;

        // fire each output synapse
        for (Synapse output: outputs) {
            output.fire();
        }

//        // if fired - strengthen input synapses
//        for (Synapse input : inputs) {
//            input.targetFired();
//        }

        // confirm fired event
        if (onFire != null) {
            onFire.accept(this);
        }
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
