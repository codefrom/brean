package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.List;

@Data
@Builder
public class Brean {
    public List<Biome> biomes;
    public List<AbstractSensor> sensors;
    public List<AbstractActuator> actuators;

}
