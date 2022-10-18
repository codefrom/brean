package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

// description of actuator
@Data
@Builder
public class ActuatorDescription {
    // name of actuator
    String name;

    ActuatorType type;

    // description of neuron population for actuator
    PopulationDescription populationDescription;
}
