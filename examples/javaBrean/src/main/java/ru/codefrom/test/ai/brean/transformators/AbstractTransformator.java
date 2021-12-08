package ru.codefrom.test.ai.brean.transformators;

import java.util.Random;

public class AbstractTransformator<T> {
    protected Random random;

    private AbstractTransformator() {
        random = new Random();
    }

    public AbstractTransformator(long seed) {
        random = new Random(seed);
    }
}
