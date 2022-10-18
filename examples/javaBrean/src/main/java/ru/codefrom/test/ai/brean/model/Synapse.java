package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.function.Consumer;

@Data
@Builder
public class Synapse {
    Neuron from;
    Neuron to;
    SynapseDescription description;

    @Builder.Default
    int strength = 10; // strength (abs)
    boolean isExcitatory = true;

//    @Builder.Default
//    int firedTicks = 0;
//    @Builder.Default
//    boolean fired = false;

    Consumer<Synapse> onFire;

    public void fire() {
        to.input(from, isExcitatory ? strength : -strength);
        to.needTick();

//        firedTicks = 20; // TODO: 20 ms time gap constant
//        fired = true;
        if (onFire != null) {
            onFire.accept(this);
        }
    }

    public void tick() {
//        if (fired) {
//            if (firedTicks > 0) {
//                to.needTick();
//                firedTicks--;
//            } else {
//                // weakening
//                firedTicks = 0;
//                fired = false;
//            }
//        }
    }

//    public void targetFired() {
//        if (fired) {
//            // strengthening
//            firedTicks = 0;
//            fired = false;
//        }
//    }


    // when learning and getting right result - strengthen connection
    public void strengthen() {
        strengthen(1);
    }
    public void strengthen(int amount) {
        strength = Math.min(description.getMaxStrength(), strength + amount);
    }

    public void weaken() {
        weaken(1);
    }
    public void weaken(int amount) {
        strength = Math.max(description.getMinStrength(), strength - amount);
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Synapse)) {
            return false;
        }
        Synapse otherSynapse = (Synapse)other;

        return from == otherSynapse.from && to == otherSynapse.to;
    }
}