package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Neuron population description
@Data
@Builder
public class PopulationDescription {
    // name of population
    String name;

    // neuron types in population
    NeuronType neuronType;

    // minimum count of neurons in population
    int neuronCount;

    // approximate count of synapses
    int synapsesCount;

    // description of population neurons properties
    NeuronDescription neuronDescription;

    // description of synapses to neuron population
    SynapseDescription synapseDescription;

    // list of connected populations (output)
    @ToString.Exclude
    @Builder.Default
    List<PopulationDescription> connectedTo = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(name, neuronType, neuronCount);
    }
}
