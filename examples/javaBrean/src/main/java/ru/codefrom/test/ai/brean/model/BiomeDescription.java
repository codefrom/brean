package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

// biome of neurons description
@Data
@Builder
public class BiomeDescription {
    // name of biome
    String name;

    // list of neuron population descriptions inside biome
    List<PopulationDescription> populations;
}
