package ru.codefrom.test.ai.brean.learning;

import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractLearner {
    protected Random random;
    protected Brean model;
    protected List<Neuron> targets;

    public AbstractLearner(Brean inputModel, long seed) {
        model = inputModel;
        random = new Random(seed);
    }

    public abstract void adjust(int error, List<Synapse> firedSynapses);

    public void setTarget(List<Neuron> targetNeurons) {
        targets = targetNeurons;
    };
}
