package ru.codefrom.test.ai.brean.model;

import lombok.Data;

import java.util.List;

@Data
public class Neuron {
    NeuronType type;
    List<Neuron> inputs;
    List<Neuron> outputs;
    Position position;
}
