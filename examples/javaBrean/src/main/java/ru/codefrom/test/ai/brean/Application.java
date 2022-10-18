package ru.codefrom.test.ai.brean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.actuators.StringOutputActuator;
import ru.codefrom.test.ai.brean.executors.AbstractExecutor;
import ru.codefrom.test.ai.brean.executors.SimpleExecutor;
import ru.codefrom.test.ai.brean.exercisers.AbstractExerciser;
import ru.codefrom.test.ai.brean.exercisers.MNISTExerciser;
import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.generators.ActuatorsGenerator;
import ru.codefrom.test.ai.brean.generators.BreanGenerator;
import ru.codefrom.test.ai.brean.generators.GenomeGenerator;
import ru.codefrom.test.ai.brean.generators.SensorsGenerator;
import ru.codefrom.test.ai.brean.learning.AbstractLearner;
import ru.codefrom.test.ai.brean.learning.SimplesReinforcementLearner;
import ru.codefrom.test.ai.brean.model.*;
import ru.codefrom.test.ai.brean.properties.*;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        File propsFile = new File("c:\\3D\\Temp\\45\\properties.json");
        AppProperties properties = new ObjectMapper().readValue(propsFile, AppProperties.class);


        long seed = properties.getSeed();

        // ------------------------------------------------
        // --- I. Generating random brain -----------------
        // ------------------------------------------------

        // 1. generate random genome
        // 1.1. generate sensors description
        List<SensorDescription> sensorDescriptions = properties.getSensorDescriptions().stream().map(sensor -> {
            PopulationDescriptionProperties population = sensor.getPopulationDescriptionProperties();
            NeuronDescriptionProperties neuron = population.getNeuronDescriptionProperties();
            SynapseDescriptionProperties synapse = population.getSynapseDescriptionProperties();

            return SensorDescription.builder()
                    .name(sensor.getName())
                    .type(sensor.getType())
                    .populationDescription(PopulationDescription.builder()
                            .name(population.getName())
                            .neuronType(population.getNeuronType())
                            .neuronCount(population.getNeuronCount())
                            .neuronDescription(NeuronDescription.builder()
                                    .refractoryPeriod(neuron.getRefractoryPeriod())
                                    .fireThreshold(neuron.getFireThreshold())
                                    .build())
                            .synapseDescription(SynapseDescription.builder()
                                    .minStrength(synapse.getMinStrength())
                                    .maxStrength(synapse.getMaxStrength())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        // 1.2. generate actuators description
        List<ActuatorDescription> actuatorDescriptions = properties.getActuatorDescriptions().stream().map(actuator -> {
            PopulationDescriptionProperties population = actuator.getPopulationDescriptionProperties();
            NeuronDescriptionProperties neuron = population.getNeuronDescriptionProperties();
            SynapseDescriptionProperties synapse = population.getSynapseDescriptionProperties();

            return ActuatorDescription.builder()
                    .name(actuator.getName())
                    .type(actuator.getType())
                    .populationDescription(PopulationDescription.builder()
                            .name(population.getName())
                            .neuronType(population.getNeuronType())
                            .neuronCount(population.getNeuronCount())
                            .neuronDescription(NeuronDescription.builder()
                                    .refractoryPeriod(neuron.getRefractoryPeriod())
                                    .fireThreshold(neuron.getFireThreshold())
                                    .build())
                            .synapseDescription(SynapseDescription.builder()
                                    .minStrength(synapse.getMinStrength())
                                    .maxStrength(synapse.getMaxStrength())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        GenomeGenerator genomeGenerator = new GenomeGenerator(seed);
        genomeGenerator.setSensorDescriptions(sensorDescriptions);
        genomeGenerator.setActuatorDescriptions(actuatorDescriptions);

        GenomeGeneratorProperties genomeGeneratorProperties = properties.getGenomeGeneratorProperties();
        genomeGenerator.setBiomesMaxCount(genomeGeneratorProperties.getBiomesMaxCount());
        genomeGenerator.setBiomePopulationsMaxCount(genomeGeneratorProperties.getBiomePopulationsMaxCount());
        genomeGenerator.setBiomePopulationNeuronMaxCount(genomeGeneratorProperties.getBiomePopulationNeuronMaxCount());
        genomeGenerator.setBiomeSynapseMaxCount(genomeGeneratorProperties.getBiomeSynapseMaxCount());

        genomeGenerator.setNeuronFireThreshold(genomeGeneratorProperties.getNeuronFireThreshold());
        genomeGenerator.setNeuronRefractoryPeriod(genomeGeneratorProperties.getNeuronRefractoryPeriod());

        genomeGenerator.setSynapseMinStrength(genomeGeneratorProperties.getSynapseMinStrength());
        genomeGenerator.setSynapseMaxStrength(genomeGeneratorProperties.getSynapseMaxStrength());

        Genome genome = genomeGenerator.generateOne();
        logger.info("============= Genome ============");
        logger.info(genome);

        // 2. from genome generate concrete brean model
        // 2.1 generate sensor
        List<AbstractSensor> sensorsList = SensorsGenerator.generate(sensorDescriptions);

        // 2.2 generate actuators
        List<AbstractActuator> actuatorsList = ActuatorsGenerator.generate(actuatorDescriptions);

        // 2.3 generate brean
        BreanGenerator breanGenerator = new BreanGenerator(seed);
        breanGenerator.setGenome(genome);
        breanGenerator.setSensors(sensorsList);
        breanGenerator.setActuators(actuatorsList);
        Brean brean = breanGenerator.generateOne();

        logger.info("============= Brean ============");
        logger.info(brean);

        // 10. Reinforced learning cycle
        AbstractExerciser exerciser = new MNISTExerciser(brean, sensorsList, actuatorsList);
        AbstractLearner learner = new SimplesReinforcementLearner(brean, seed);
        AbstractExecutor executor = new SimpleExecutor(brean, properties.getLearningProperties().getProcessIterations());
        List<Synapse> firedSynapses = new ArrayList<>();
        while (exerciser.next()) {
            exerciser.prepare();
            int iteration = 0;
            int error = 1;

            int good = 0;

            while (/*error > 0 &&*/ iteration < properties.getLearningProperties().getIterations()) {
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

        // 1. generate biome descriptions
//        BiomeDescriptionGenerator biomeDescriptionGenerator = new BiomeDescriptionGenerator(seed);
//        List<BiomeDescription> biomeDescriptionList = biomeDescriptionGenerator.generate(Constants.MAX_BIOME_DESCRIPTIONS_COUNT);
//
//        String biomeDescriptions = StringUtils.join(biomeDescriptionList, "\n");
//
//        logger.info("============= Biome Descriptions ============");
//        logger.info(biomeDescriptions);
//
//        // 2. generate biomes
//        BiomeGenerator biomeGenerator = new BiomeGenerator(biomeDescriptionList, seed);
//        List<Biome> biomesList = biomeGenerator.generate(Constants.MAX_BIOMES_COUNT);
//
//        String biomes = StringUtils.join(biomesList, "\n");
//
//        logger.info("============= Biomes ========================");
//        logger.info(biomes);
//
//        // 3. position biomes in space
//        BiomesCompactor biomesCompactor = new BiomesCompactor(biomesList, seed);
//        biomesCompactor.transform();
//
//        biomes = StringUtils.join(biomesList, "\n");
//
//        logger.info("============= Biomes compacted ==============");
//        logger.info(biomes);
//
//        // 4. compact neurons
//        NeuronsCompactor neuronsCompactor = new NeuronsCompactor(biomesList, seed);
//        neuronsCompactor.transform();
//
//        biomes = StringUtils.join(biomesList, "\n");
//
//        logger.info("============= Neurons compacted =============");
//        logger.info(biomes);
//
//        // 5. connect neurons (at random to nearest ones)
//        NeuronsConnector neuronsConnector = new NeuronsConnector(biomesList, seed);
//        neuronsConnector.transform();
//
//        biomes = StringUtils.join(biomesList, "\n");
//
//        logger.info("============= Neurons connected =============");
//        logger.info(biomes);
//
//        // 6. Create sensors
//        List<AbstractSensor> sensorsList = new ArrayList<>();
//        MonochromeImageInputSensor imageSensor = new MonochromeImageInputSensor(28, 28);
//        sensorsList.add(imageSensor);
//
//        // 7. Connect sensors
//        SensorsConnector sensorsConnector = new SensorsConnector(sensorsList, biomesList, seed);
//        sensorsConnector.transform();
//
//        // 8. Create actuators
//        List<AbstractActuator> actuatorsList = new ArrayList<>();
//        StringOutputActuator stringOutputActuator = new StringOutputActuator();
//        actuatorsList.add(stringOutputActuator);
//
//        // 9. Connect actuators
//        ActuatorsConnector actuatorsConnector = new ActuatorsConnector(actuatorsList, biomesList, seed);
//        actuatorsConnector.transform();
//
//        // 10. Collect brean model
//        Brean brean = Brean.builder()
//                .biomes(biomesList)
//                .sensors(sensorsList)
//                .actuators(actuatorsList)
//                .build();
//
//        // 10. Reinforced learning cycle
//        AbstractExerciser exerciser = new MNISTExerciser(brean, imageSensor, stringOutputActuator);
//        AbstractLearner learner = new SimplesReinforcementLearner(brean, seed);
//        AbstractExecutor executor = new SimpleExecutor(brean);
//        List<Synapse> firedSynapses = new ArrayList<>();
//        while (exerciser.next()) {
//            exerciser.prepare();
//            int iteration = 0;
//            int error = 1;
//
//            int good = 0;
//
//            while (/*error > 0 &&*/ iteration < Constants.MAX_LEARNING_ITERATIONS) {
//                if (iteration > 0) {
//                    // adjust network
//                    learner.setTarget(exerciser.getTargetNeurons());
//                    learner.adjust(error, firedSynapses);
//                }
//                // run network
//                firedSynapses = executor.process();
//
//                error = exerciser.error();
//                if (error == 0) {
//                    good++;
//                }
//                iteration++;
//            }
//            logger.info("Success ratio: {}", (double)good/(double)iteration);
//        }
    }
}
