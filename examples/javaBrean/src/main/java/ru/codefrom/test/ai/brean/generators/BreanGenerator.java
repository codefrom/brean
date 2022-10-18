package ru.codefrom.test.ai.brean.generators;

import lombok.Data;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.model.*;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class BreanGenerator  extends AbstractGenerator<Brean> {
    Genome genome;

    List<AbstractSensor> sensors;
    List<AbstractActuator> actuators;

    public BreanGenerator(long seed) {
        super(seed);
    }

    @Override
    public Brean generateOne() {
        ArrayList<Biome> biomes = new ArrayList<>();

        for(BiomeDescription biomeDescription: genome.getBiomeDescriptions()) {
            BiomeGenerator biomeGenerator = new BiomeGenerator(seed);
            biomeGenerator.setBiomeDescription(biomeDescription);
            biomeGenerator.setGenome(genome);
            biomes.add(biomeGenerator.generateOne());
        }

        // connect neurons based on populations connections
        Map<PopulationDescription, Population> populationsByDescription = biomes.stream().flatMap(biome -> biome.getPopulations().stream()).collect(Collectors.toMap(population -> population.getDescription(), population -> population));
        // 1. connect sensors
        for(AbstractSensor sensor: sensors) {
            for(PopulationDescription connectedToDescription : sensor.getPopulation().getDescription().getConnectedTo()) {
                if (populationsByDescription.containsKey(connectedToDescription)) {
                    Population connectToPopulation = populationsByDescription.get(connectedToDescription);
                    connectSensorPopulation(sensor.getPopulation(), connectToPopulation);
                }
            }
        }

        // 2. connect actuators
        for(AbstractActuator actuator: actuators) {
            // find populations, which connects to actuators population
            List<PopulationDescription> populationsConnectTo = populationsByDescription.keySet().stream().filter(description -> description.getConnectedTo().contains(actuator.getPopulation().getDescription())).collect(Collectors.toList());
            for(PopulationDescription description: populationsConnectTo) {
                Population connectToPopulation = populationsByDescription.get(description);
                connectActuatorPopulation(connectToPopulation, actuator.getPopulation());
            }
        }

        // 3. connect populations inside biomes
        for(Population first: populationsByDescription.values()) {
            for(PopulationDescription secondDescription: first.getDescription().getConnectedTo()) {
                if (populationsByDescription.containsKey(secondDescription)) {
                    Population second = populationsByDescription.get(secondDescription);
                    connectBiomePopulations(first, second);
                }
            }
        }

        return Brean.builder()
                .name(randomName("brean"))
                .genome(genome)
                .sensors(sensors)
                .actuators(actuators)
                .biomes(biomes)
                .build();
    }

    private void connectBiomePopulations(Population first, Population second) {
        for(Neuron firstNeuron: first.getNeurons()) {
            // connect at least to 1 other neuron
            // at most 10 other neurons
            int tries = randomCount(first.getDescription().getSynapsesCount()) + 1;
            for (int i = 0; i < tries; i++) {
                int secondNeuronIndex = randomCount(second.getNeurons().size()) - 1;
                Neuron secondNeuron = second.getNeurons().get(secondNeuronIndex);

                if (firstNeuron.getOutputs().stream().filter(synapse -> synapse.getTo() == secondNeuron).findAny().isPresent())
                    continue;

                boolean isExcitatory = random.nextBoolean();
                int strength = randomNumber(first.getDescription().getSynapseDescription().getMinStrength(), first.getDescription().getSynapseDescription().getMaxStrength() + 1);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .strength(strength)
                        .isExcitatory(isExcitatory)
                        .description(first.getDescription().getSynapseDescription())
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }
        }
    }

    private void connectActuatorPopulation(Population first, Population actuatorPopulation) {
        int connectTo = first.getNeurons().size() > actuatorPopulation.getNeurons().size() ? (int)Math.ceil((double)first.getNeurons().size() / (double)actuatorPopulation.getNeurons().size() ): 1;
        for(Neuron secondNeuron: actuatorPopulation.getNeurons()) {
            for(int i = 0; i < connectTo; i++) {
                int firstNeuronIndex = random.nextInt(first.getNeurons().size());
                Neuron firstNeuron = first.getNeurons().get(firstNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .strength(first.getDescription().getSynapseDescription().getMaxStrength())
                        .description(first.getDescription().getSynapseDescription())
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }
        }
    }

    private void connectSensorPopulation(Population sensorPopulation, Population second) {
        int connectTo = second.getNeurons().size() > sensorPopulation.getNeurons().size() ? (int)Math.ceil((double)second.getNeurons().size() / (double)sensorPopulation.getNeurons().size() ): 1;
        for(Neuron firstNeuron: sensorPopulation.getNeurons()) {
            for(int i = 0; i < connectTo; i++) {
                int secondNeuronIndex = random.nextInt(second.getNeurons().size());
                Neuron secondNeuron = second.getNeurons().get(secondNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .strength(sensorPopulation.getDescription().getSynapseDescription().getMaxStrength())
                        .description(sensorPopulation.getDescription().getSynapseDescription())
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }
        }
    }
}