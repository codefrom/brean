package ru.codefrom.test.ai.brean.generators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class BiomeDescriptionGenerator extends AbstractGenerator<BiomeDescription> {
    int populationsInBiomeMaxCount;
    int neuronsInPopulationMaxCount;
    int synapseMaxCount;

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

    public BiomeDescriptionGenerator(long seed) {
        super(seed);
    }

    @Override
    public BiomeDescription generateOne() {
        String name = randomName("biome");

        // 2. generate populations
        // 2.1. generate populations count
        int populationsCount = randomCount(populationsInBiomeMaxCount);
        // 2.2. generate each population description
        List<PopulationDescription> populations = new ArrayList<>(populationsCount);
        for (int i = 0; i < populationsCount; i++) {
            populations.add(generatePopulation(neuronsInPopulationMaxCount));
        }

        // 3. build biome description
        return BiomeDescription.builder()
                .name(name)
                .populations(populations)
                .build();
    }

    private PopulationDescription generatePopulation(int volume) {
        String name = randomName("population");
        int neuronsCount = randomCount(volume);
        return PopulationDescription.builder()
                .name(name)
                .neuronType(NeuronType.INOUT)
                .neuronCount(neuronsCount)
                .synapsesCount(synapseMaxCount)
                .neuronDescription(NeuronDescription.builder()
                        .fireThreshold(neuronFireThreshold)
                        .refractoryPeriod(neuronRefractoryPeriod)
                        .build())
                .synapseDescription(SynapseDescription.builder()
                        .minStrength(synapseMinStrength)
                        .maxStrength(synapseMaxStrength)
                        .build())
                .build();
    }
}
