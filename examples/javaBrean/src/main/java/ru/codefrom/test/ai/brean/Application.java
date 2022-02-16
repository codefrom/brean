package ru.codefrom.test.ai.brean;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.actuators.StringOutputActuator;
import ru.codefrom.test.ai.brean.executors.AbstractExecutor;
import ru.codefrom.test.ai.brean.executors.SimpleExecutor;
import ru.codefrom.test.ai.brean.exercisers.AbstractExerciser;
import ru.codefrom.test.ai.brean.exercisers.MNISTExerciser;
import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.generators.BiomeDescriptionGenerator;
import ru.codefrom.test.ai.brean.generators.BiomeGenerator;
import ru.codefrom.test.ai.brean.learning.AbstractLearner;
import ru.codefrom.test.ai.brean.learning.SimplesReinforcementLearner;
import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.BiomeDescription;
import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Synapse;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;
import ru.codefrom.test.ai.brean.sensors.ImageInputSensor;
import ru.codefrom.test.ai.brean.sensors.MonochromeImageInputSensor;
import ru.codefrom.test.ai.brean.transformators.*;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        long seed = 123L;

        // ------------------------------------------------
        // --- I. Generating random brain -----------------
        // ------------------------------------------------

        // 1. generate biome descriptions
        BiomeDescriptionGenerator biomeDescriptionGenerator = new BiomeDescriptionGenerator(seed);
        List<BiomeDescription> biomeDescriptionList = biomeDescriptionGenerator.generate(Constants.MAX_BIOME_DESCRIPTIONS_COUNT);

        String biomeDescriptions = StringUtils.join(biomeDescriptionList, "\n");

        logger.info("============= Biome Descriptions ============");
        logger.info(biomeDescriptions);

        // 2. generate biomes
        BiomeGenerator biomeGenerator = new BiomeGenerator(biomeDescriptionList, seed);
        List<Biome> biomesList = biomeGenerator.generate(Constants.MAX_BIOMES_COUNT);

        String biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Biomes ========================");
        logger.info(biomes);

        // 3. position biomes in space
        BiomesCompactor biomesCompactor = new BiomesCompactor(biomesList, seed);
        biomesCompactor.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Biomes compacted ==============");
        logger.info(biomes);

        // 4. compact neurons
        NeuronsCompactor neuronsCompactor = new NeuronsCompactor(biomesList, seed);
        neuronsCompactor.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Neurons compacted =============");
        logger.info(biomes);

        // 5. connect neurons (at random to nearest ones)
        NeuronsConnector neuronsConnector = new NeuronsConnector(biomesList, seed);
        neuronsConnector.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Neurons connected =============");
        logger.info(biomes);

        // 6. Create sensors
        List<AbstractSensor> sensorsList = new ArrayList<>();
        MonochromeImageInputSensor imageSensor = new MonochromeImageInputSensor(28, 28);
        sensorsList.add(imageSensor);
        
        // 7. Connect sensors
        SensorsConnector sensorsConnector = new SensorsConnector(sensorsList, biomesList, seed);
        sensorsConnector.transform();

        // 8. Create actuators
        List<AbstractActuator> actuatorsList = new ArrayList<>();
        StringOutputActuator stringOutputActuator = new StringOutputActuator();
        actuatorsList.add(stringOutputActuator);

        // 9. Connect actuators
        ActuatorsConnector actuatorsConnector = new ActuatorsConnector(actuatorsList, biomesList, seed);
        actuatorsConnector.transform();

        // 10. Collect brean model
        Brean brean = Brean.builder()
                .biomes(biomesList)
                .sensors(sensorsList)
                .actuators(actuatorsList)
                .build();

        // 10. Reinforced learning cycle
        AbstractExerciser exerciser = new MNISTExerciser(brean, imageSensor, stringOutputActuator);
        AbstractLearner learner = new SimplesReinforcementLearner(brean, seed);
        AbstractExecutor executor = new SimpleExecutor(brean);
        List<Synapse> firedSynapses = new ArrayList<>();
        while (exerciser.next()) {
            exerciser.prepare();
            int iteration = 0;
            int error = 1;

            int good = 0;

            while (/*error > 0 &&*/ iteration < Constants.MAX_LEARNING_ITERATIONS) {
                if (iteration > 0) {
                    // adjust network
                    learner.setTarget(exerciser.getTargetNeurons());
                    learner.adjust(error, firedSynapses);
                }
                // run network
                firedSynapses = executor.process();

                error = exerciser.error();
                if (error == 0) {
                    good++;
                }
                iteration++;
            }
            logger.info("Success ratio: {}", (double)good/(double)iteration);
        }
    }
}
