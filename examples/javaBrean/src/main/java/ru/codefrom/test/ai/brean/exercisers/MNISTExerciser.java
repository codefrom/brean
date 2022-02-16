package ru.codefrom.test.ai.brean.exercisers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.actuators.StringOutputActuator;
import ru.codefrom.test.ai.brean.datasets.mnist.MnistDataReader;
import ru.codefrom.test.ai.brean.datasets.mnist.MnistMatrix;
import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;
import ru.codefrom.test.ai.brean.sensors.ImageInputSensor;
import ru.codefrom.test.ai.brean.sensors.MonochromeImageInputSensor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MNISTExerciser extends AbstractExerciser {
    private static Logger logger = LogManager.getLogger(MNISTExerciser.class);
    MonochromeImageInputSensor sensor;
    StringOutputActuator actuator;
    MnistMatrix[] mnistMatrix;
    int index = -1;

    public MNISTExerciser(Brean model) {
        super(model);
        MnistDataReader dataReader = new MnistDataReader();
        try {
            mnistMatrix = dataReader.readData("c:/3D/Work/Pets/Java/javaBrean/examples/javaBrean/src/main/resources/mnist/train-images.idx3-ubyte", "c:/3D/Work/Pets/Java/javaBrean/examples/javaBrean/src/main/resources/mnist/train-labels.idx1-ubyte");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public MNISTExerciser(Brean model, MonochromeImageInputSensor imageInputSensor, StringOutputActuator stringOutputActuator) {
        super(model);
        sensor = imageInputSensor;
        actuator = stringOutputActuator;
        MnistDataReader dataReader = new MnistDataReader();
        try {
            mnistMatrix = dataReader.readData("c:/3D/Work/Pets/Java/javaBrean/examples/javaBrean/src/main/resources/mnist/train-images.idx3-ubyte", "c:/3D/Work/Pets/Java/javaBrean/examples/javaBrean/src/main/resources/mnist/train-labels.idx1-ubyte");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    @Override
    public boolean next() {
        index++;
        return index != mnistMatrix.length;
    }

    List<Neuron> visited = new ArrayList<>();

    int havePathBetweenNeurons(Neuron from, Neuron to) {
        if (visited.contains(from)) {
            return -1;
        }

        visited.add(from);
        for(Synapse synapse: from.getOutputs()) {
            if (synapse.getTo() == to) {
                return 1;
            }
        }

        for(Synapse synapse: from.getOutputs()) {
            int pathLength = havePathBetweenNeurons(synapse.getTo(), to);
            if (pathLength > 0) {
                return pathLength + 1;
            }
        }

        return -1;
    }

    @Override
    public void prepare() {
        // let's check if path exists
        /*Neuron toNeuron = actuator.getNeurons().get(Integer.toString(mnistMatrix[index].getLabel()).charAt(0));
        for(Neuron fromNeuron : sensor.getNeurons()) {
            visited.clear();
            int pathLength = havePathBetweenNeurons(fromNeuron, toNeuron);
            logger.info("Path between sensor neuron and actuator neuron : {}", pathLength);
        }*/

        // 1. set input signal
        sensor.setInputSignal(getImage(mnistMatrix[index]));
        // 2. set output feedback
        actuator.setSignal(Integer.toString(mnistMatrix[index].getLabel()));
    }

    @Override
    public List<Neuron> getTargetNeurons() {
        List<Neuron> targets = new ArrayList<>();
        Neuron toNeuron = actuator.getNeurons().get(Integer.toString(mnistMatrix[index].getLabel()).charAt(0));
        targets.add(toNeuron);
        return targets;
    }

    private BufferedImage getImage(MnistMatrix matrix) {
        int width = matrix.getNumberOfColumns();
        int height = matrix.getNumberOfRows();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = matrix.getValue(y, x);
                bufferedImage.setRGB(x, y, new Color(value, value, value).getRGB());
            }
        }

        return bufferedImage;
    }

    @Override
    public int error() {
        int result = actuator.getOutput();
        int expectedResult = Integer.toString(mnistMatrix[index].getLabel()).charAt(0);
        /*if (expectedResult == result) {
            logger.info("[MATCHED] Expected {}, got {}.", expectedResult, result);
        } else {
            logger.info("[NOTMATCHED] Expected {}, got {}.", expectedResult, result);
        }*/
        return Math.abs(result - expectedResult);
    }
}
