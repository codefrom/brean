package ru.codefrom.test.ai.brean.actuators;

import lombok.Builder;
import lombok.Data;
import ru.codefrom.test.ai.brean.model.Neuron;
import ru.codefrom.test.ai.brean.model.NeuronType;
import ru.codefrom.test.ai.brean.model.Position;
import ru.codefrom.test.ai.brean.model.Synapse;
import ru.codefrom.test.ai.brean.sensors.AbstractSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Data
public abstract class AbstractActuator {
    public List<Neuron> neurons = new ArrayList<>();;
    public List<Neuron> feedback = new ArrayList<>();;
    Consumer<AbstractActuator> onFire;

    public AbstractActuator() {
        initNeurons();
        for(Neuron neuron: neurons) {
            neuron.setOnFire(x -> fire(x));
        }
    }

    protected abstract void initNeurons();

    public void createFeedback() {
        for(Neuron neuron: neurons) {
            List<Synapse> feedbackOutputs = new ArrayList<>();
            for (Synapse synapse : neuron.getInputs()) {
                Synapse feedbackSynapse = Synapse.builder()
                        .from(synapse.getTo())
                        .to(synapse.getFrom())
                        .build();
                feedbackOutputs.add(feedbackSynapse);
            }
            // TODO : position
            Position feedbackNeuronPosition = Position.builder().build();
            Neuron feedbackNeuron = Neuron.builder()
                    .outputs(feedbackOutputs)
                    .position(feedbackNeuronPosition)
                    .type(NeuronType.IN)
                    .build();

            Synapse synapse = Synapse.builder()
                    .from(neuron)
                    .to(feedbackNeuron)
                    .build();

            neuron.addOutput(synapse);
            feedbackNeuron.addInput(synapse);

            feedback.add(feedbackNeuron);
        }
    }

    private void fire(Neuron neuron) {
        onFire(neuron);
        if (onFire != null) {
            onFire.accept(this);
        }
    }

    public abstract void onFire(Neuron neuron);

    public abstract void feedback();
}
