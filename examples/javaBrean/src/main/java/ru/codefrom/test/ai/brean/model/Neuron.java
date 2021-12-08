package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Neuron {
    NeuronType type;
    List<Neuron> inputs;
    List<Neuron> outputs;
    Position position;

    public void rotateAroundPoint(Position origin, double[] rotation) {
        // TODO : move to new position
    }
}
