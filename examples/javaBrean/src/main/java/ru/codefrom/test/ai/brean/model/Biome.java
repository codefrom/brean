package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Biome {
    BiomeDescription baseDescription;
    List<Neuron> neurons;
    Position origin;

    public void rotateTo(double[] rotation) {
        origin.setRotationXYZ(rotation);
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).rotateAroundPoint(origin, rotation);
        }
    }
}
