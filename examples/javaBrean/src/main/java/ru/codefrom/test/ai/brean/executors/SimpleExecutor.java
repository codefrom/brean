package ru.codefrom.test.ai.brean.executors;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.model.*;
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
    int processIterations;

    public SimpleExecutor(Brean inputModel, int processIterations) {
        super(inputModel);
        this.processIterations = processIterations;
    }

    @Override
    public List<Synapse> process() {
        int currentRun = new Random().nextInt(1000);
        List<Synapse> firedSynapses = new ArrayList<>();
        List<Synapse> allFiredSynapses = new ArrayList<>();

        // prepare neurons
        for(Biome biome : model.getBiomes()) {
            for(Population population: biome.getPopulations()) {
                for (Neuron neuron : population.getNeurons()) {
                    neuron.setPotential(0);
                    neuron.setRefractoryTicks(0);

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
        }
        neuronsToTick.clear();

        // prepare actuators
        for (AbstractActuator actuator : model.getActuators()) {
            actuator.setOnFire(x -> { stop = true; });
            for(Neuron neuron: actuator.getPopulation().getNeurons()) {
                neuron.setPotential(0);
                neuron.setRefractoryTicks(0);

                neuron.setOnNeedTick(x -> {
                    if (!neuronsToTick.contains(x)) {
                        neuronsToTick.add(x);
                    }
                });
            }
        }

        // prepare sensors
        for (AbstractSensor sensor : model.getSensors()) {
            for (Neuron neuron : sensor.getPopulation().getNeurons()) {
                neuron.setPotential(0);
                neuron.setRefractoryTicks(0);

                neuron.getOutputs().forEach(synapse -> synapse.setOnFire(y -> {
                   if(!firedSynapses.contains(y)) {
                       firedSynapses.add(y);
                   }
                }));
            }
        }
        // run network
        for (int i = 0; i < processIterations; i++) {
            if (stop) {
                stop = false;
                break;
            }
            // tick sensors every run
            for(AbstractSensor sensor : model.getSensors()) {
                sensor.tick();
            }

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

            // draw network for debug
//            if (i % 10 == 0)
                drawNetwork(neuronsToTick, firedSynapses, currentRun, i);

            allFiredSynapses.addAll(firedSynapses);
            firedSynapses.clear();
        }

        return allFiredSynapses;
    }

    private void drawNetwork(List<Neuron> neuronsToTick, List<Synapse> firedSynapses, int runId, int iteration) {
        int width = 0;
        int height = 0;
        for(AbstractActuator actuator: model.getActuators()) {
            Population population = actuator.getPopulation();
            int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));
            width += biomeSide + 2;
            height = Math.max(height, biomeSide + 10 + 2);
        }
        for(AbstractSensor sensor: model.getSensors()) {
            Population population = sensor.getPopulation();
            int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));
            width += biomeSide + 2;
            height = Math.max(height, biomeSide + 10 + 2);
        }
        for(Biome biome: model.getBiomes()) {
            for(Population population: biome.getPopulations()) {
                int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));
                width += biomeSide + 2;
                height = Math.max(height, biomeSide + 10 + 2);
            }
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g2d.setPaint (Color.WHITE);
        g2d.fillRect ( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() );
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);

        int currentOrigin = 0;

        for(AbstractSensor sensor: model.getSensors()) {
            Population population = sensor.getPopulation();

            int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));
            // draw label
            //g2d.drawString(sensor.getName(), currentOrigin + 0, 10);
            // sensor color
            g2d.setColor(Color.RED);
            // draw square
            g2d.drawRect(currentOrigin + 0, 10, biomeSide + 1, biomeSide + 1);
            for (int i = 0; i < population.getNeurons().size(); i++) {
                int x = i / biomeSide;
                int y = i % biomeSide;

                Neuron neuron = population.getNeurons().get(i);
                boolean fired = neuron.getOutputs().stream().anyMatch(s -> firedSynapses.contains(s));

                Color neuronColor = fired ? Color.RED : Color.GRAY;
                bufferedImage.setRGB(currentOrigin + x + 1, y + 11, neuronColor.getRGB());
            }
            currentOrigin += biomeSide + 2;
        }

        for(AbstractActuator actuator: model.getActuators()) {
            Population population = actuator.getPopulation();

            int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));

            // draw label
            //g2d.drawString(actuator.getName(), currentOrigin + 0, 10);
            // actuator color
            g2d.setColor(Color.BLUE);
            // draw square
            g2d.drawRect(currentOrigin + 0, 10, biomeSide + 1, biomeSide + 1);
            for (int i = 0; i < population.getNeurons().size(); i++) {
                int x = i / biomeSide;
                int y = i % biomeSide;

                Neuron neuron = population.getNeurons().get(i);
                boolean fired = neuron.getInputs().stream().anyMatch(s -> firedSynapses.contains(s));

                Color neuronColor = fired ? Color.BLUE : Color.CYAN;
                bufferedImage.setRGB(currentOrigin + x + 1, y + 11, neuronColor.getRGB());
            }
            currentOrigin += biomeSide + 2;
        }

        for(Biome biome: model.getBiomes()) {
            for(Population population: biome.getPopulations()) {
                int biomeSide = (int) Math.ceil(Math.sqrt(population.getNeurons().size()));

                // draw label
                //g2d.drawString(biome.getName(), currentOrigin + 0, 10);
                // border color
                g2d.setColor(Color.BLACK);
                // draw square
                g2d.drawRect(currentOrigin + 0, 10, biomeSide + 1, biomeSide + 1);

                for (int i = 0; i < population.getNeurons().size(); i++) {
                    int x = i / biomeSide;
                    int y = i % biomeSide;

                    Neuron neuron = population.getNeurons().get(i);
                    boolean fired = neuron.getOutputs().stream().anyMatch(s -> firedSynapses.contains(s));

                    Color neuronColor = fired ? Color.YELLOW : Color.GREEN;
                    bufferedImage.setRGB(currentOrigin + x + 1, y + 11, neuronColor.getRGB());
                }

                currentOrigin += biomeSide + 2;
            }
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
