package ru.codefrom.test.ai.brean.properties;

import lombok.Data;

import java.util.List;

@Data
public class AppProperties {
    public long seed;
    public List<SensorDescriptionProperties> sensorDescriptions;
    public List<ActuatorDescriptionProperties> actuatorDescriptions;
    public GenomeGeneratorProperties genomeGeneratorProperties;
    public LearningProperties learningProperties;
}
