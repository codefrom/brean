package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Biome {
    String name;
    BiomeDescription description;
    List<Population> populations;
}
