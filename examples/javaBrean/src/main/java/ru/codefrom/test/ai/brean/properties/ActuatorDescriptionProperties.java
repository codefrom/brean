package ru.codefrom.test.ai.brean.properties;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.ActuatorType;

@Data
public class ActuatorDescriptionProperties {
    public String name;
    public ActuatorType type;
    public PopulationDescriptionProperties populationDescriptionProperties;
}
