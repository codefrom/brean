package ru.codefrom.test.ai.brean.properties;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.NeuronDescription;
import ru.codefrom.test.ai.brean.model.NeuronType;
import ru.codefrom.test.ai.brean.model.SynapseDescription;

@Data
public class PopulationDescriptionProperties {
    public String name;
    public NeuronType neuronType;
    public int neuronCount;
    public NeuronDescriptionProperties neuronDescriptionProperties;
    public SynapseDescriptionProperties synapseDescriptionProperties;
}
