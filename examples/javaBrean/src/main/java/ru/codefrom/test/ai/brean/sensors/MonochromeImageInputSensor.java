package ru.codefrom.test.ai.brean.sensors;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Data
public class MonochromeImageInputSensor extends AbstractSensor {
    BufferedImage inputSignal;

    public MonochromeImageInputSensor(SensorDescription description) {
        super(description);
        generatePopulation(description.getPopulationDescription());
    }

    private void generatePopulation(PopulationDescription description) {
        ArrayList<Neuron> neurons = new ArrayList<>();
        for(int i = 0; i < description.getNeuronCount(); i++) {
            neurons.add(Neuron.builder()
                    .type(NeuronType.SENSOR)
                    .description(description.getNeuronDescription())
                    .build());
        }
        population = Population.builder()
                .name(description.getName())
                .description(description)
                .neurons(neurons)
                .build();
    }

    public void setInputSignal(BufferedImage input) {
        int newWidth = input.getWidth();
        int newHeight = input.getHeight();
        if (newWidth * newHeight != population.getNeurons().size()) {
            throw new IllegalArgumentException("Wrong dimensions of new input");
        }

        inputSignal = input;
    }

    // Processing one time
    public void tick() {
        // for each pixel output signal to neuron
        int width = inputSignal.getWidth();
        int height = inputSignal.getHeight();
        for (int i = 0 ; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgba = inputSignal.getRGB(i, j);
                population.getNeurons().get(i * width + j).input(0, getSignalStrength(getR(rgba)));
                population.getNeurons().get(i * width + j).tick();
            }
        }
    }

    private int getR(int rgba) {
        return (rgba & 0x00ff0000) >> 16;
    }

    public int getSignalStrength(int color) {
        //return color;
        //return (int)Math.round((double)color / 10.0);

        // color from 0 to 255
        // maximum strength is 10
        // so 0 - 24 = 0, 24 - 50 = 1...
        return (int)Math.floor((double)color / 24.0);
    }
}