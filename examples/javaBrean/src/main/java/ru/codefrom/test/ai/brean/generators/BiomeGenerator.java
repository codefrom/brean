package ru.codefrom.test.ai.brean.generators;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class BiomeGenerator extends AbstractGenerator<Biome> {
    BiomeDescription biomeDescription;
    Genome genome;

    public BiomeGenerator(long seed) {
        super(seed);
    }

    @Override
    public Biome generateOne() {
        // generate populations
        ArrayList<Population> populations = new ArrayList<>();
        for(PopulationDescription populationDescription: biomeDescription.getPopulations()) {
            List<Neuron> neurons = generateNeurons(populationDescription);
            Population population = Population.builder()
                    .name(populationDescription.getName())
                    .description(populationDescription)
                    .neurons(neurons)
                    .build();
            populations.add(population);
        }

        return Biome.builder()
                .name(biomeDescription.getName())
                .description(biomeDescription)
                .populations(populations)
                .build();
    }

    private List<Neuron> generateNeurons(PopulationDescription populationDescription) {
        List<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < populationDescription.getNeuronCount(); i++) {
            Neuron neuron = Neuron.builder()
                    .type(populationDescription.getNeuronType())
                    .description(populationDescription.getNeuronDescription())
                    .build();
            neurons.add(neuron);
        }
        return neurons;
    }


}
