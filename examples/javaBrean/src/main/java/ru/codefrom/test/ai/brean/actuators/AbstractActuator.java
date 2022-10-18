package ru.codefrom.test.ai.brean.actuators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Data
public abstract class AbstractActuator {
    String name;
    ActuatorDescription description;
    Population population;
    Consumer<AbstractActuator> onFire;

    private AbstractActuator() {}

    public AbstractActuator(ActuatorDescription description) {
        this.description = description;
        this.name = description.getName();
        initPopulation(description.getPopulationDescription());
        for(Neuron neuron: population.getNeurons()) {
            neuron.setOnFire(x -> fire(x));
        }
    }

    protected abstract void initPopulation(PopulationDescription description);

    private void fire(Neuron neuron) {
        onFire(neuron);
        if (onFire != null) {
            onFire.accept(this);
        }
    }

    public abstract void onFire(Neuron neuron);
}
