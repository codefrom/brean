package ru.codefrom.test.ai.brean.properties;

import lombok.Data;

@Data
public class GenomeGeneratorProperties {
    public int biomesMaxCount;
    public int biomePopulationsMaxCount;
    public int biomePopulationNeuronMaxCount;
    public int biomeSynapseMaxCount;

    public int neuronFireThreshold;
    public int neuronRefractoryPeriod;

    public int synapseMinStrength;
    public int synapseMaxStrength;
}
