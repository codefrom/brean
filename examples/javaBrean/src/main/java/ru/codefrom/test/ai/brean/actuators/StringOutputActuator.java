package ru.codefrom.test.ai.brean.actuators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: more like letter output
@Data
public class StringOutputActuator extends AbstractActuator {
    List<Character> output = new ArrayList<>();

    public StringOutputActuator(ActuatorDescription description) {
        super(description);
    }
    @Override
    protected void initPopulation(PopulationDescription description) {
        ArrayList<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < 255; i++) {
            Neuron neuron = Neuron.builder()
                    .type(NeuronType.ACTUATOR)
                    .description(description.getNeuronDescription())
                    .build();

            neurons.add(neuron);
        }

        population = Population.builder()
                .name(description.getName())
                .description(description)
                .neurons(neurons)
                .build();
    }

    @Override
    public void onFire(Neuron neuron) {
        output.add((char)population.getNeurons().indexOf(neuron));
    }
}
