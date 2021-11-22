package ru.codefrom.test.ai.brean.model;

import lombok.Data;

@Data
public class Position {
    Integer[] coordinatesXYZ = new Integer[3];
    Double[] rotationXYZ = new Double[3];
}
