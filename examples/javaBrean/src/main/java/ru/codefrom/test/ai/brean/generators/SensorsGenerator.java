package ru.codefrom.test.ai.brean.generators;

import ru.codefrom.test.ai.brean.model.SensorDescription;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;
import ru.codefrom.test.ai.brean.sensors.MonochromeImageInputSensor;
import ru.codefrom.test.ai.brean.sensors.StringInputSensor;

import java.util.List;
import java.util.stream.Collectors;

public class SensorsGenerator {
    public static List<AbstractSensor> generate(List<SensorDescription> descriptions) {
        return descriptions.stream().map(description -> {
            switch (description.getType()) {
                case CHAR:
                    return new StringInputSensor(description);
                case IMAGE_MONOCHROME:
                    return new MonochromeImageInputSensor(description);
            }
            throw new IllegalArgumentException(String.format("Unknown sensor type: %s", description.getType()));
        }).collect(Collectors.toList());
    }
}
