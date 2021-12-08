package ru.codefrom.test.ai.brean.generators;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class BiomeGenerator extends AbstractGenerator<Biome> {
    List<BiomeDescription> biomeDescriptionList;

    public BiomeGenerator(List<BiomeDescription> biomeDescription, long seed) {
        super(seed);
        this.biomeDescriptionList = biomeDescription;
    }

    @Override
    public List<Biome> generate(int maximumCount) {
        List<Biome> biomes = new ArrayList<>();
        for (BiomeDescription biomeDescription: biomeDescriptionList) {
            int generateBiomesCount = randomCount(maximumCount);
            for (int i = 0; i < generateBiomesCount; i++) {
                Biome biome = generateOne(biomeDescription);
                biomes.add(biome);
            }
        }
        return biomes;
    }

    protected Biome generateOne(BiomeDescription biomeDescription) {
        // generate neuron populations
        List<Neuron> neurons = new ArrayList<>();
        for (BiomePopulationDescription populationDescription: biomeDescription.getPopulations()) {
            int neuronsCount = randomCount(populationDescription.getCount());
            List<Neuron> neuronsPopulation = generateNeurons(biomeDescription, populationDescription, neuronsCount);
            neurons.addAll(neuronsPopulation);
        }

        // generate biome
        Biome biome = Biome.builder()
                .baseDescription(biomeDescription)
                .neurons(neurons)
                .build();
        return biome;
    }

    private List<Neuron> generateNeurons(BiomeDescription biomeDescription, BiomePopulationDescription populationDescription, int neuronsCount) {
        List<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < neuronsCount; i++) {
            int[] coordinates = getRandomBoundCoordinates(biomeDescription);
            Position position = Position.builder()
                    .coordinatesXYZ(coordinates)
                    .build();
            Neuron neuron = Neuron.builder()
                    .type(populationDescription.getNeuronType())
                    .position(position)
                    .build();
            neurons.add(neuron);
        }
        return neurons;
    }

    private int[] getRandomBoundCoordinates(BiomeDescription biomeDescription) {
        int[] coordinates = new int[3];
        switch (biomeDescription.getShape()) {
            case BOX:
                coordinates[0] = random.nextInt(biomeDescription.getBoundaries()[0]);
                coordinates[1] = random.nextInt(biomeDescription.getBoundaries()[1]);
                coordinates[2] = random.nextInt(biomeDescription.getBoundaries()[2]);
                break;
            case SPHERE:
                int distance1 = random.nextInt(biomeDescription.getBoundaries()[0]);
                double alpha1 = random.nextDouble() * Math.PI * 2.0;
                double betta1 = random.nextDouble() * Math.PI * 2.0;
                double gamma1 = random.nextDouble() * Math.PI * 2.0;
                coordinates[0] = (int)Math.round(Math.cos(alpha1) * (double)distance1);
                coordinates[1] = (int)Math.round(Math.cos(betta1) * (double)distance1);
                coordinates[2] = (int)Math.round(Math.cos(gamma1) * (double)distance1);
                break;
            case CYLINDER:
                int distance2 = random.nextInt(biomeDescription.getBoundaries()[1]);
                double alpha2 = random.nextDouble() * Math.PI * 2.0;
                double betta2 = random.nextDouble() * Math.PI * 2.0;
                coordinates[0] = (int)Math.round(Math.cos(alpha2) * (double)distance2);
                coordinates[1] = (int)Math.round(Math.cos(betta2) * (double)distance2);
                coordinates[2] = random.nextInt(biomeDescription.getBoundaries()[0]);
                break;
        }
        return coordinates;
    }

    @Override
    protected Biome generateOne() {
        return null;
    }
}
