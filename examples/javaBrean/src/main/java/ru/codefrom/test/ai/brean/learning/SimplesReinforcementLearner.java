package ru.codefrom.test.ai.brean.learning;

import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.NeuronType;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimplesReinforcementLearner extends AbstractLearner {

    public SimplesReinforcementLearner(Brean inputModel, long seed) {
        super(inputModel, seed);
    }

    @Override
    public void adjust(int error, List<Synapse> firedSynapses) {
        if (error == 2) {
            for (Synapse fired : firedSynapses) {
                if (fired.getFrom().getType() != NeuronType.SENSOR && fired.getTo().getType() != NeuronType.ACTUATOR) {
                    fired.weaken();
                }
            }
        } else if (error > 0) {
            // try to rebalance connections
            List<Synapse> allSynapses = model.getBiomes().stream()
                    .flatMap(biome -> biome.getPopulations().stream())
                    .flatMap(population -> population.getNeurons().stream())
                    .flatMap(neuron -> neuron.getOutputs().stream())
                    .collect(Collectors.toList());

            int toStrengthen = (int) Math.floor((double) allSynapses.size() * 0.1);
            int toWeaken = (int) Math.floor((double) allSynapses.size() * 0.05);

            for(int i = 0; i < toStrengthen; i++) {
                int synapseRandomIndex = random.nextInt(allSynapses.size());
                int adjust = 1; //random.nextInt(5) + 1;
                Synapse synapse = allSynapses.get(synapseRandomIndex);
                if (synapse.getFrom().getType() != NeuronType.SENSOR && synapse.getTo().getType() != NeuronType.ACTUATOR)
                    synapse.strengthen(adjust);
            }
            for(int i = 0; i < toWeaken; i++){
                int synapseRandomIndex = random.nextInt(allSynapses.size());
                int adjust = 1; //random.nextInt(5) + 1;
                Synapse synapse = allSynapses.get(synapseRandomIndex);
                if (synapse.getFrom().getType() != NeuronType.SENSOR && synapse.getTo().getType() != NeuronType.ACTUATOR)
                    synapse.weaken(adjust);
            }
/*
            // try to grow connections
            int tries = random.nextInt(10); // TODO : why 10?
            List<Neuron> allNeurons = model.getBiomes().stream().flatMap(biome -> biome.getPopulations().stream()).flatMap(population -> population.getNeurons().stream()).collect(Collectors.toList());

            for (int i = 0; i < tries; i++) {
                int firstBiomeIndex = random.nextInt(model.getBiomes().size());
                int secondBiomeIndex = random.nextInt(model.getBiomes().size());
                int firstBiomePopulationIndex = random.nextInt(model.getBiomes().get(firstBiomeIndex).getPopulations().size());
                int secondBiomePopulationIndex = random.nextInt(model.getBiomes().get(secondBiomeIndex).getPopulations().size());
                int firstNeuronIndex = random.nextInt(model.getBiomes().get(firstBiomeIndex).getPopulations().get(firstBiomePopulationIndex).getNeurons().size());
                int secondNeuronIndex = random.nextInt(model.getBiomes().get(secondBiomeIndex).getPopulations().get(secondBiomePopulationIndex).getNeurons().size());
                Neuron firstNeuron = model.getBiomes().get(firstBiomeIndex).getPopulations().get(firstBiomePopulationIndex).getNeurons().get(firstNeuronIndex);
                Neuron secondNeuron = model.getBiomes().get(secondBiomeIndex).getPopulations().get(secondBiomePopulationIndex).getNeurons().get(secondNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }*/
        } else {
            for(Synapse fired: firedSynapses) {
                if (fired.getFrom().getType() != NeuronType.SENSOR)
                    fired.strengthen();
            }

//            // try to rebalance connections
//            List<Synapse> allSynapses = model.getBiomes().stream()
//                    .flatMap(biome -> biome.getPopulations().stream())
//                    .flatMap(population -> population.getNeurons().stream())
//                    .flatMap(neuron -> neuron.getOutputs().stream())
//                    .filter(synapse -> !firedSynapses.contains(synapse))
//                    .collect(Collectors.toList());
//            int toWeaken = (int) Math.floor((double) allSynapses.size() * 1.0);
//            for(int i = 0; i < toWeaken; i++){
//                int synapseRandomIndex = i;//random.nextInt(allSynapses.size());
//                Synapse synapse = allSynapses.get(synapseRandomIndex);
//                synapse.weaken();
//            }


//            int toStrengthen = (int) Math.floor((double) allSynapses.size() * 0.2);
//            for(int i = 0; i < toStrengthen; i++){
//                int synapseRandomIndex = random.nextInt(allSynapses.size());
//                Synapse synapse = allSynapses.get(synapseRandomIndex);
//                synapse.weaken();
//            }
        }
    }
}
