package ru.codefrom.test.ai.brean.sensors;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.NeuronType;
import ru.codefrom.test.ai.brean.model.Position;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImageInputSensor extends AbstractSensor {
    public BufferedImage inputSignal;
    private int width;
    private int height;

    public ImageInputSensor() {
        width = 1;
        height = 1;
    }

    public ImageInputSensor(int width, int height) {
        this.width = width;
        this.height = height;
        generateNeurons();
    }


    public ImageInputSensor(BufferedImage firstInput) {
        inputSignal = firstInput;
        width = firstInput.getWidth();
        height = firstInput.getHeight();
        generateNeurons();
    }

    private void generateNeurons() {
        neurons = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // generate
                int[] coordinates = {i, j, 0};
                // S M L (blue, green, red..ish) cones, for simplicity - R G B cones
                for (int k = 0; k < 3; k++) {
                    Position position = Position.builder()
                            .coordinatesXYZ(coordinates)
                            .build();
                    Neuron neuron = Neuron.builder()
                            .position(position)
                            .type(NeuronType.SENSOR)
                            .build();
                    neurons.add(neuron);
                }
            }
        }
    }

    public void setInputSignal(BufferedImage input) {
        int newWidth = input.getWidth();
        int newHeight = input.getHeight();
        if (newWidth != width || newHeight != height) {
            throw new IllegalArgumentException("Wrong dimensions of new input");
        }

        inputSignal = input;
    }

    // Processing one time
    public void tick() {
        // for each pixel output signal to neuron
        for (int i = 0 ; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgba = inputSignal.getRGB(i, j);
                neurons.get((i * width + j) * 3).input(0, getSignalStrength(getR(rgba)));
                neurons.get((i * width + j) * 3).tick();
                neurons.get((i * width + j) * 3 + 1).input(0, getSignalStrength(getG(rgba)));
                neurons.get((i * width + j) * 3 + 1).tick();
                neurons.get((i * width + j) * 3 + 2).input(0, getSignalStrength(getB(rgba)));
                neurons.get((i * width + j) * 3 + 2).tick();
            }
        }
    }

    private int getB(int rgba) {
        return rgba & 0x000000ff;
    }

    private int getG(int rgba) {
        return (rgba & 0x0000ff00) >> 8;
    }

    private int getR(int rgba) {
        return (rgba & 0x00ff0000) >> 16;
    }

    public int getSignalStrength(int color) {
        return (int)Math.round((double)color / 10.0);
    }
}