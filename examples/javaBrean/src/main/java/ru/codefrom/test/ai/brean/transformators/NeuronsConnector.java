package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.List;

public class NeuronsConnector extends AbstractTransformator<Biome> {
    public NeuronsConnector(List<Biome> biomesList, long seed) {
        super(biomesList, seed);
    }

    @Override
    public void transform() {
        int neuronsCount = 0;
        for(Biome biome : objects) {
            neuronsCount += biome.getNeurons().size();
        }

        // connect neurons
        for(int i = 0; i < objects.size(); i++) {
            // connect neurons in biome between them
            int tries = random.nextInt(objects.get(i).getNeurons().size() * 10); // TODO : why 10?
            for(int k = 0; k < tries; k++) {
                int firstNeuronIndex = random.nextInt(objects.get(i).getNeurons().size());
                int secondNeuronIndex = random.nextInt(objects.get(i).getNeurons().size());

                Neuron firstNeuron = objects.get(i).getNeurons().get(firstNeuronIndex);
                Neuron secondNeuron = objects.get(i).getNeurons().get(secondNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }

            // connect neurons to other biomes
            int connectToBiomesCount = random.nextInt(objects.size());
            for (int j = 0; j < connectToBiomesCount; j++) {
                int secondBiomeIndex = random.nextInt(objects.size());
                while (secondBiomeIndex == i) {
                    secondBiomeIndex = random.nextInt(objects.size());
                }

                tries = random.nextInt(objects.get(i).getNeurons().size() * 10); // TODO : why 10?
                for(int k = 0; k < tries; k++) {
                    int firstNeuronIndex = random.nextInt(objects.get(i).getNeurons().size());
                    int secondNeuronIndex = random.nextInt(objects.get(secondBiomeIndex).getNeurons().size());

                    Neuron firstNeuron = objects.get(i).getNeurons().get(firstNeuronIndex);
                    Neuron secondNeuron = objects.get(secondBiomeIndex).getNeurons().get(secondNeuronIndex);

                    Synapse synapse = Synapse.builder()
                            .from(firstNeuron)
                            .to(secondNeuron)
                            .build();

                    firstNeuron.addOutput(synapse);
                    secondNeuron.addInput(synapse);

                }
            }
        }
    }
}
