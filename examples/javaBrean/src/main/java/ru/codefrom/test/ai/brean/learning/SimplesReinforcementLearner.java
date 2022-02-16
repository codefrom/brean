package ru.codefrom.test.ai.brean.learning;

import ru.codefrom.test.ai.brean.model.Brean;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.Synapse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimplesReinforcementLearner extends AbstractLearner {
    private List<Synapse> goodSynapses = new ArrayList<>();

    public SimplesReinforcementLearner(Brean inputModel, long seed) {
        super(inputModel, seed);
    }

    public void findGoodSynapses(List<Synapse> firedSynapses, List<Neuron> targetsList) {
        List<Synapse> good = firedSynapses.stream().filter(x -> targetsList.contains(x.getTo())).collect(Collectors.toList());
        if (good.size() != 0) {
            goodSynapses.addAll(good);
            findGoodSynapses(firedSynapses.stream().filter(s -> !goodSynapses.contains(s)).collect(Collectors.toList()), good.stream().map(x -> x.getFrom()).collect(Collectors.toList()));
        }
    }

    @Override
    public void adjust(int error, List<Synapse> firedSynapses) {
        if (error > 0) {
            goodSynapses.clear();
            findGoodSynapses(firedSynapses, targets);
            /*goodSynapses.forEach(synapse -> {
              synapse.setStrength(Math.min(100, synapse.getStrength() + 10));
            });*/

            firedSynapses.stream().filter(s -> !goodSynapses.contains(s)).forEach(synapse -> {
                /*if (synapse.getStrength() > 10) {
                    synapse.setStrength(10);
                }*/
                //synapse.setStrength(Math.max(0, synapse.getStrength() - 1));
                synapse.setStrength(0);
                if (synapse.getStrength() == 0) {
                    synapse.getFrom().getOutputs().remove(synapse);
                    synapse.getTo().getInputs().remove(synapse);
                }
            });
        }

        /*if (error > 0) {
            // weaken synapses if error
            for(Synapse synapse : firedSynapses) {
                //synapse.setStrength(Math.min(0, synapse.getStrength() - 1));
                //synapse.setStrength(0);
                synapse.setStrength(5);
                //synapse.setStrength(Math.max(0, synapse.getStrength() - 1));
                if (synapse.getStrength() == 0) {
                    synapse.getFrom().getOutputs().remove(synapse);
                    synapse.getTo().getInputs().remove(synapse);
                }
            }
        } else {
            // strengthen synapses if error
            for(Synapse synapse : firedSynapses) {
                synapse.setStrength(Math.min(0, synapse.getStrength() + 1));
            }
        }*/

        // connect random neurons, based on error level
        /*if (error > 0) {
            int tries = random.nextInt(error * 10); // TODO : why 10?
            for (int i = 0; i < tries; i++) {
                int firstBiomeIndex = random.nextInt(model.getBiomes().size());
                int secondBiomeIndex = random.nextInt(model.getBiomes().size());
                int firstNeuronIndex = random.nextInt(model.getBiomes().get(firstBiomeIndex).getNeurons().size());
                int secondNeuronIndex = random.nextInt(model.getBiomes().get(secondBiomeIndex).getNeurons().size());
                Neuron firstNeuron = model.getBiomes().get(firstBiomeIndex).getNeurons().get(firstNeuronIndex);
                Neuron secondNeuron = model.getBiomes().get(secondBiomeIndex).getNeurons().get(secondNeuronIndex);

                Synapse synapse = Synapse.builder()
                        .from(firstNeuron)
                        .to(secondNeuron)
                        .build();

                firstNeuron.addOutput(synapse);
                secondNeuron.addInput(synapse);
            }
        }*/
    }
}
