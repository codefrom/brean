package ru.codefrom.test.ai.brean.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

// Description of how to generate brean
@Data
@Builder
public class Genome {
    // genome name
    String name;

    // biome descriptions
    List<BiomeDescription> biomeDescriptions;

    // sensors descriptions
    List<SensorDescription> sensorDescriptions;

    // actuators descriptions
    List<ActuatorDescription> actuatorDescriptions;
}
