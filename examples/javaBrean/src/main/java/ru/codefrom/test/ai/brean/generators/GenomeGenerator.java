package ru.codefrom.test.ai.brean.generators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GenomeGenerator extends AbstractGenerator<Genome> {

    List<SensorDescription> sensorDescriptions;
    List<ActuatorDescription> actuatorDescriptions;

    // biomes properties
    int biomesMaxCount;
    int biomePopulationsMaxCount;
    int biomePopulationNeuronMaxCount;
    int biomeSynapseMaxCount;

    // neuron properties
    @Builder.Default
    int neuronFireThreshold = 15;
    @Builder.Default
    int neuronRefractoryPeriod = 3;

    // synapse properties
    @Builder.Default
    int synapseMinStrength = 0;
    @Builder.Default
    int synapseMaxStrength = 15;

    public GenomeGenerator(long seed) {
        super(seed);
    }

    public Genome generateOne() {
        String name = randomName("genome");

        // generate inner biomes
        BiomeDescriptionGenerator biomeDescriptionGenerator = new BiomeDescriptionGenerator(seed);
        biomeDescriptionGenerator.setSynapseMaxCount(biomeSynapseMaxCount);
        biomeDescriptionGenerator.setPopulationsInBiomeMaxCount(biomePopulationsMaxCount);
        biomeDescriptionGenerator.setNeuronsInPopulationMaxCount(biomePopulationNeuronMaxCount);

        List<BiomeDescription> biomeDescriptions = biomeDescriptionGenerator.generate(biomesMaxCount);

        // connect population to each other
        List<PopulationDescription> allPopulationDescriptions = biomeDescriptions.stream()
                .flatMap(biome -> biome.getPopulations().stream())
                .collect(Collectors.toList());

        //   connect all populations inside one biome
        for(BiomeDescription biomeDescription: biomeDescriptions) {
            for(PopulationDescription populationDescription: biomeDescription.getPopulations()) {
                populationDescription.setConnectedTo(
                        biomeDescription.getPopulations().stream()
                                .filter(p -> !p.equals(populationDescription))
                                .collect(Collectors.toList())
                );
            }
        }

        //   connect random populations in random biomes
        for(int i = 0; i < biomeDescriptions.size(); i++) {
            BiomeDescription biomeDescription1 =  biomeDescriptions.get(i);
            for(int j = i + 1; j < biomeDescriptions.size(); j++) {
                BiomeDescription biomeDescription2 =  biomeDescriptions.get(j);

                for(PopulationDescription population1: biomeDescription1.getPopulations()) {
                    for(PopulationDescription population2: biomeDescription2.getPopulations()) {
                        // randomly connect population1 to population2
                        if (random.nextBoolean()) {
                            population1.getConnectedTo().add(population2);
                        }

                        // randomly connect population2 to population1
                        if (random.nextBoolean()) {
                            population2.getConnectedTo().add(population1);
                        }
                    }
                }
            }
        }

        //   connect sensors each to some (only one for one sensor) population
        for(SensorDescription sensorDescription: sensorDescriptions) {
            // TODO: each neuron of sensor must be connected, maybe to separate neuron?
            int connectToPopulationIndex = randomNumber(0, allPopulationDescriptions.size());
            List<PopulationDescription> connectToPopulation = new ArrayList<>();
            connectToPopulation.add(allPopulationDescriptions.get(connectToPopulationIndex));
            sensorDescription.getPopulationDescription().setConnectedTo(connectToPopulation);
        }
        //   connect actuators each to some (only one for one sensor) population
        for(ActuatorDescription actuatorDescription: actuatorDescriptions) {
            // TODO: each neuron of actuator must be connected, maybe to separate neuron?
            int connectToPopulationIndex = randomNumber(0, allPopulationDescriptions.size());
            allPopulationDescriptions.get(connectToPopulationIndex).getConnectedTo().add(actuatorDescription.getPopulationDescription());
        }

        return Genome.builder()
                .name(name)
                .biomeDescriptions(biomeDescriptions)
                .sensorDescriptions(sensorDescriptions)
                .actuatorDescriptions(actuatorDescriptions)
                .build();
    }
}
