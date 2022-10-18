package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.List;

@Data
@Builder
public class Brean {
    Genome genome;
    String name;
    List<Biome> biomes;
    List<AbstractSensor> sensors;
    List<AbstractActuator> actuators;

}
