package ru.codefrom.test.ai.brean.sensors;

import lombok.Data;
import ru.codefrom.test.ai.brean.general.Shape;
import ru.codefrom.test.ai.brean.model.Neuron;

import java.util.List;

@Data
public class AbstractSensor {
    public Shape shape;
    public List<Neuron> neurons;

    public void tick() {
        for(Neuron neuron: neurons) {
            neuron.tick();
        }
    }
}
