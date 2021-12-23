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
        throw new NullPointerException("NOT IMPLEMENTED");
    }

    public void moveByOrigin(Position origin, int[] position) {
        this.position.getCoordinatesXYZ()[0] = this.position.getCoordinatesXYZ()[0] + position[0] - origin.getCoordinatesXYZ()[0];
        this.position.getCoordinatesXYZ()[1] = this.position.getCoordinatesXYZ()[1] + position[1] - origin.getCoordinatesXYZ()[1];
        this.position.getCoordinatesXYZ()[2] = this.position.getCoordinatesXYZ()[2] + position[2] - origin.getCoordinatesXYZ()[2];
    }
}
