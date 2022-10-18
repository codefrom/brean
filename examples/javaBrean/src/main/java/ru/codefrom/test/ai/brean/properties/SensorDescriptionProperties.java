package ru.codefrom.test.ai.brean.properties;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.PopulationDescription;
import ru.codefrom.test.ai.brean.model.SensorType;

@Data
public class SensorDescriptionProperties {
    public String name;
    public SensorType type;
    public PopulationDescriptionProperties populationDescriptionProperties;
}
