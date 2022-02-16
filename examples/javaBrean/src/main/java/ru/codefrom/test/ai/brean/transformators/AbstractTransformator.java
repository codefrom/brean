package ru.codefrom.test.ai.brean.transformators;

import ru.codefrom.test.ai.brean.model.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbstractTransformator<T> {
    protected List<T> objects;
    protected Random random;

    private AbstractTransformator() {
        objects = new ArrayList<T>();
        random = new Random();
    }

    public AbstractTransformator(List<T> objectsList, long seed) {
        objects = objectsList;
        random = new Random(seed);
    }

    public void transform() {};
}
