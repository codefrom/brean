package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Population {
    String name;
    PopulationDescription description;
    List<Neuron> neurons;
}
