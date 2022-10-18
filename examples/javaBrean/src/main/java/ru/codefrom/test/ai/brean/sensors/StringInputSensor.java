package ru.codefrom.test.ai.brean.sensors;

import ru.codefrom.test.ai.brean.model.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StringInputSensor extends AbstractSensor {

    char inputSignal;

    public StringInputSensor(SensorDescription description) {
        super(description);
        generatePopulation(description.getPopulationDescription());
    }

    protected void generatePopulation(PopulationDescription description) {
        ArrayList<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < 255; i++) {
            Neuron neuron = Neuron.builder()
                    .type(NeuronType.SENSOR)
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

    public void setInputSignal(char input) {
        inputSignal = input;
    }

    // Processing one time
    public void tick() {
        population.getNeurons().get((int)inputSignal).input(0, 15);
        population.getNeurons().get((int)inputSignal).tick();
    }

}
