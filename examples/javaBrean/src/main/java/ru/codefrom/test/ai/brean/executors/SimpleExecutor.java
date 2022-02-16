package ru.codefrom.test.ai.brean.executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleExecutor extends AbstractExecutor {
    private static Logger logger = LogManager.getLogger(SimpleExecutor.class);
    boolean stop = false;
    List<Neuron> neuronsToTick = new ArrayList<>();

    public SimpleExecutor(Brean inputModel) {
        super(inputModel);
    }

    @Override
    public List<Synapse> process() {
        int currentRun = new Random().nextInt(1000);
        List<Synapse> firedSynapses = new ArrayList<>();

        // prepare neurons
        for(Biome biome : model.getBiomes()) {
            for(Neuron neuron : biome.getNeurons()) {
                neuron.setOnNeedTick(x -> {
                    if (!neuronsToTick.contains(x)) {
                        neuronsToTick.add(x);
                    }
                });

                neuron.getOutputs().forEach(x -> x.setOnFire(y -> {
                    if (!firedSynapses.contains(y)) {
                        firedSynapses.add(y);
                    }
                }));
            }
        }
        neuronsToTick.clear();
        // prepare actuators
        for (AbstractActuator actuator : model.getActuators()) {
            actuator.setOnFire(x -> { stop = true; });
            actuator.getNeurons().forEach(a -> a.setOnNeedTick(x -> {
                if (!neuronsToTick.contains(x)) {
                    neuronsToTick.add(x);
                }
            }));
        }

        // run network
        for (int i = 0; i < Constants.MAX_PROCESS_ITERATIONS; i++) {
            if (stop) {
                stop = false;
                break;
            }
            // tick sensors every run
            for(AbstractSensor sensor : model.getSensors()) {
                sensor.tick();
            }

            //drawNetwork(neuronsToTick, firedSynapses, currentRun, i);

            // debug output
            /*List<Biome> activeBiomes = new ArrayList<>();
            for(Neuron neuron: neuronsToTick) {
                for(Biome biome: model.getBiomes()) {
                    if (biome.getNeurons().contains(neuron)) {
                        if (!activeBiomes.contains(biome)) {
                            activeBiomes.add(biome);
                        }
                        break;
                    }
                }
            }
            logger.info("Active biomes: {}", StringUtils.join(activeBiomes.stream().map(x -> x.getName()).collect(Collectors.toList()), ','));*/

            // tick every touched neuron
            List<Neuron> neuronsToTickTemp = new ArrayList<>();
            neuronsToTickTemp.addAll(neuronsToTick);
            neuronsToTick.clear();
            for(Neuron neuron: neuronsToTickTemp) {
                neuron.tick();
            }

            // tick actuators every run
            for(AbstractActuator actuator : model.getActuators()) {
                actuator.feedback();
            }
        }

        return firedSynapses;
    }

    private void drawNetwork(List<Neuron> neuronsToTick, List<Synapse> firedSynapses, int runId, int iteration) {
        int width = 0;
        int height = 0;
        for(Biome biome: model.getBiomes()) {
            int biomeSide = (int)Math.ceil(Math.sqrt(biome.getNeurons().size()));
            width += biomeSide + 2;
            height = Math.max(height, biomeSide + 10 + 2);
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g2d.setPaint (Color.WHITE);
        g2d.fillRect ( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() );
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);

        int currentOrigin = 0;
        for(Biome biome: model.getBiomes()) {
            int biomeSide = (int)Math.ceil(Math.sqrt(biome.getNeurons().size()));

            // draw label
            //g2d.drawString(biome.getName(), currentOrigin + 0, 10);

            if (biome.getName().contains("sensor") && biome.getName().contains("actuator")) {
                g2d.setColor(Color.MAGENTA);
            } else if (biome.getName().contains("sensor")) {
                g2d.setColor(Color.RED);
            } else if (biome.getName().contains("actuator")) {
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(Color.BLACK);
            }

            // draw square
            g2d.drawRect(currentOrigin + 0, 10, biomeSide + 1, biomeSide + 1);


            for(int i = 0; i < biome.getNeurons().size(); i++) {
                int x = i % biomeSide;
                int y = i / biomeSide;

                Neuron neuron = biome.getNeurons().get(i);
                boolean fired = neuron.getOutputs().stream().anyMatch(s -> firedSynapses.contains(s));
                boolean isSensorNeuron = model.getSensors().stream().anyMatch(sensor ->
                    sensor.getNeurons().stream().anyMatch(sensorNeuron ->
                            sensorNeuron.getOutputs().stream().anyMatch(
                                    sensorNeuronSynapse -> sensorNeuronSynapse.getTo() == neuron
                            )
                    )
                );
                boolean isActuatorNeuron = model.getActuators().stream().anyMatch(actuator ->
                        actuator.getNeurons().stream().anyMatch(actuatorNeuron ->
                                actuatorNeuron.getInputs().stream().anyMatch(
                                        actuatorNeuronSynapse -> actuatorNeuronSynapse.getFrom() == neuron
                                )
                        )
                );

                Color neuronColor = Color.GREEN;
                if (fired) {
                    if (isSensorNeuron && isActuatorNeuron) {
                        neuronColor = Color.MAGENTA;
                    } else if (isSensorNeuron) {
                        neuronColor = Color.RED;
                    } else if (isActuatorNeuron) {
                        neuronColor = Color.BLUE;
                    } else {
                        neuronColor = Color.YELLOW;
                    }
                } else {
                    if (isSensorNeuron && isActuatorNeuron) {
                        neuronColor = Color.PINK;
                    } else if (isSensorNeuron) {
                        neuronColor = Color.GRAY;
                    } else if (isActuatorNeuron) {
                        neuronColor = Color.CYAN;
                    } else {
                        neuronColor = Color.GREEN;
                    }
                }

                bufferedImage.setRGB(currentOrigin + x + 1, y + 11, neuronColor.getRGB());
            }

            currentOrigin += biomeSide + 2;
        }

        g2d.dispose();
        File output = new File("c:/3D/Temp/45/r_" + runId + "_" + iteration + ".bmp");
        try {
            ImageIO.write(bufferedImage, "bmp", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
