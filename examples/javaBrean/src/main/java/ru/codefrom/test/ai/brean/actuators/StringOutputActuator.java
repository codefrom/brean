package ru.codefrom.test.ai.brean.actuators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.NeuronType;
import ru.codefrom.test.ai.brean.model.Position;

import java.util.ArrayList;
import java.util.function.Consumer;

// TODO: more like letter output
@Data
public class StringOutputActuator extends AbstractActuator {
    int signal;
    char output;

    @Override
    protected void initNeurons() {
        for (int i = 0; i < 255; i++) {
            Position position = Position.builder().build();
            Neuron neuron = Neuron.builder()
                    .type(NeuronType.ACTUATOR)
                    .build();

            neurons.add(neuron);
        }
    }

    @Override
    public void onFire(Neuron neuron) {
        output = (char)neurons.indexOf(neuron);
    }

    public void setSignal(String string) {
        signal = (int)string.charAt(0);
    }

    @Override
    public void feedback() {
        if (feedback == null || feedback.size() == 0) {
            createFeedback();
        }
        feedback.get(signal).input(10); // TODO : why strength is 10?
        feedback.get(signal).needTick();
    }
}
