package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BiomePopulationDescription {
    NeuronType neuronType;
    Integer count;
}
