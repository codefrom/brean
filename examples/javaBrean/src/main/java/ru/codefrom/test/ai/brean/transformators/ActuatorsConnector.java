package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.List;

public class ActuatorsConnector extends AbstractTransformator<AbstractActuator> {
    List<Biome> biomes;

    public ActuatorsConnector(List<AbstractActuator> actuatorsList, List<Biome> biomesList, long seed) {
        super(actuatorsList, seed);
        biomes = biomesList;
    }

    @Override
    public void transform() {
        // connect actuators to other neurons

        // determine to which biome to connect
        int secondBiomeIndex = random.nextInt(biomes.size());
        // TODO : don't connect to sensor biome
        while(biomes.get(secondBiomeIndex).getName().contains("sensor")) {
            secondBiomeIndex = random.nextInt(biomes.size());
        }
        biomes.get(secondBiomeIndex).setName(biomes.get(secondBiomeIndex).getName() + "_actuator");

        for (AbstractActuator actuator : objects) {
            // connect each actuator neuron to biomes neurons
            for (Neuron secondNeuron : actuator.getNeurons()) {
                int firstNeuronIndex = random.nextInt(biomes.get(secondBiomeIndex).getNeurons().size());
                Neuron firstNeuron = biomes.get(secondBiomeIndex).getNeurons().get(firstNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }

            // create feedback neurons
            actuator.createFeedback();
            // connect feedback neurons
            for (Neuron firstNeuron : actuator.getFeedback()) {
                int secondNeuronIndex = random.nextInt(biomes.get(secondBiomeIndex).getNeurons().size());
                Neuron secondNeuron = biomes.get(secondBiomeIndex).getNeurons().get(secondNeuronIndex);

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
