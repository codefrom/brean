package ru.codefrom.test.ai.brean.exercisers;

import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;

import java.util.List;

public abstract class AbstractExerciser {

    protected Brean model;

    public AbstractExerciser(Brean inputModel) {
        model = inputModel;
    }

    public abstract boolean next();

    public abstract void prepare();

    public abstract int error();

    public abstract List<Neuron> getTargetNeurons();
}
