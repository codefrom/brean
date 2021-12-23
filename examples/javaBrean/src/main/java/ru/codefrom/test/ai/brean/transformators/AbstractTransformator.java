package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbstractTransformator<T> {
    protected List<Biome> biomes;
    protected Random random;

    private AbstractTransformator() {
        biomes = new ArrayList<Biome>();
        random = new Random();
    }

    public AbstractTransformator(List<Biome> biomesList, long seed) {
        biomes = biomesList;
        random = new Random(seed);
    }

    public void transform() {};
}
