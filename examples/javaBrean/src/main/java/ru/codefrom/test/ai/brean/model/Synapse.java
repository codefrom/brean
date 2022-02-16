package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.function.Consumer;

@Data
@Builder
public class Synapse {
    Neuron from;
    Neuron to;

    @Builder.Default
    int strength = 10; // 0 means no signal gonna be transmitted, synapse will die soon TODO: base strength 10 constant
    @Builder.Default
    int firedTicks = 0;
    @Builder.Default
    boolean fired = false;

    Consumer<Synapse> onFire;

    public void fire(int potential) {
        to.input(from, potential);
        to.needTick();

        firedTicks = 20; // TODO: 20 ms time gap constant
        fired = true;
        if (onFire != null) {
            onFire.accept(this);
        }
    }

    public void tick() {
        if (fired) {
            if (firedTicks > 0) {
                to.needTick();
                firedTicks--;
            } else {
                // weakening
                firedTicks = 0;
                fired = false;
                strength -= 1;
                strength = Math.max(-100, strength);
            }
        }
    }

    public void targetFired() {
        if (fired) {
            // strengthening
            firedTicks = 0;
            fired = false;
            strength += 1;
            strength = Math.min(100, strength);
        }
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