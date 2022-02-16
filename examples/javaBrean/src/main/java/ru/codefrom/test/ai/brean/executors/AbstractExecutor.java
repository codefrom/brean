package ru.codefrom.test.ai.brean.executors;

import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.List;

public abstract class AbstractExecutor {
    protected Brean model;

    public AbstractExecutor(Brean inputModel) {
        model = inputModel;
    }

    public abstract List<Synapse> process();
}
