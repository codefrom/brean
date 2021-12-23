package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;

import java.util.List;

public class NeuronsCompactor extends AbstractTransformator<Biome> {
    public NeuronsCompactor(List<Biome> biomesList, long seed) {
        super(biomesList, seed);
    }

    @Override
    public void transform() {
        // TODO : move neurons closer to each other?
    }
}
