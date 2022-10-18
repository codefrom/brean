package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

// description of sensor
@Data
@Builder
public class SensorDescription {
    // name of sensor
    String name;

    SensorType type;

    // neuron population description
    PopulationDescription populationDescription;
}
