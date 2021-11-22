package ru.codefrom.test.ai.brean.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGenerator<T> {
    protected Random random;

    private AbstractGenerator() {
        this(0);
    }

    public AbstractGenerator(long seed) {
        this.random = new Random(seed);
    }

    protected abstract T generateOne();

    public List<T> generate(int maximumCount) {
        int realCount = randomCount(maximumCount);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < realCount; i++) {
            result.add(generateOne());
        }
        return result;
    }


    // helper functions
    protected final <E extends Enum<?>> E randomEnum(Class<E> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    protected final int randomCount(int maxCount) {
        return random.nextInt(maxCount) + 1;
    }
}
