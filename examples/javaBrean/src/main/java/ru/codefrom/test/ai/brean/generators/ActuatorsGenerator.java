package ru.codefrom.test.ai.brean.generators;

import ru.codefrom.test.ai.brean.actuators.AbstractActuator;
import ru.codefrom.test.ai.brean.actuators.StringOutputActuator;
import ru.codefrom.test.ai.brean.model.ActuatorDescription;
import java.util.List;
import java.util.stream.Collectors;

public class ActuatorsGenerator {
    public static List<AbstractActuator> generate(List<ActuatorDescription> descriptions) {
        return descriptions.stream().map(description -> {
            switch (description.getType()) {
                case CHAR:
                    return new StringOutputActuator(description);
            }
            throw new IllegalArgumentException(String.format("Unknown actuator type: %s", description.getType()));
        }).collect(Collectors.toList());
    }
}
