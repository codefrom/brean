package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.List;

public class SensorsConnector extends AbstractTransformator<AbstractSensor> {
    List<Biome> biomes;

    public SensorsConnector(List<AbstractSensor> sensorsList, List<Biome> biomesList, long seed) {
        super(sensorsList, seed);
        biomes = biomesList;
    }

    @Override
    public void transform() {
        // connect sensors to other neurons
        for(AbstractSensor sensor : objects) {
            // select biome to connect to
            int secondBiomeIndex = random.nextInt(biomes.size());
            // TODO : don't connect to actuator biome
            while(biomes.get(secondBiomeIndex).getName().contains("actuator")) {
                secondBiomeIndex = random.nextInt(biomes.size());
            }
            biomes.get(secondBiomeIndex).setName(biomes.get(secondBiomeIndex).getName() + "_sensor");

            // connect each sensor neuron to other neuron in biome
            for (Neuron firstNeuron : sensor.getNeurons()) {
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
