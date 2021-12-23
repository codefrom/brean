package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.general.Shape;

import java.util.List;

@Data
@Builder
public class BiomeDescription {
    Shape shape;
    int[] boundaries;
    List<BiomePopulationDescription> populations;
}
