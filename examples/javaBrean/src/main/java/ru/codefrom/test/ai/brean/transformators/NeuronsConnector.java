package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;

import java.util.List;

public class NeuronsConnector extends AbstractTransformator<Biome> {
    public NeuronsConnector(List<Biome> biomesList, long seed) {
        super(biomesList, seed);
    }

    @Override
    public void transform() {
        // TODO: connect neurons
    }
}
