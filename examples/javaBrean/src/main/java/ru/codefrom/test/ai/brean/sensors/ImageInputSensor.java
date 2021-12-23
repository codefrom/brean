package ru.codefrom.test.ai.brean.sensors;

import ru.codefrom.test.ai.brean.model.Neuron;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageInputSensor extends AbstractSensor {
    public BufferedImage inputSignal;
    private int width;
    private int height;
    private List<Neuron> neurons;

    public ImageInputSensor() {
        width = 1;
        height = 1;
    }

    public ImageInputSensor(BufferedImage firstInput) {
        inputSignal = firstInput;
        width = firstInput.getWidth();
        height = firstInput.getHeight();
        generateNeurons();
    }

    private void generateNeurons() {
        neurons = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            // TODO : generate
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

    }
}