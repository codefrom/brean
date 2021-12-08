package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {
    @Builder.Default
    int[] coordinatesXYZ = new int[3];

    @Builder.Default
    double[] rotationXYZ = new double[3];
}
