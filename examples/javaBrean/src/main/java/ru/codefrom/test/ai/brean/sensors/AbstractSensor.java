package ru.codefrom.test.ai.brean.sensors;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.general.Shape;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Population;
import ru.codefrom.test.ai.brean.model.SensorDescription;

import java.util.List;

@Data
public abstract class AbstractSensor {
    String name;
    SensorDescription description;
    Population population;

    private AbstractSensor() {}
    public AbstractSensor(SensorDescription description) {
        this.description = description;
        this.name = description.getName();
    }

    public void tick() {
        for(Neuron neuron: population.getNeurons()) {
            neuron.tick();
        }
    }
}
