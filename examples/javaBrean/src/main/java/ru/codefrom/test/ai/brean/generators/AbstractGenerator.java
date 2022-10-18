package ru.codefrom.test.ai.brean.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractGenerator<T> {
    protected Random random;
    protected long seed;

    private AbstractGenerator() {
        this(0);
    }

    public AbstractGenerator(long seed)
    {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public abstract T generateOne();

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

    // min is inclusive, max is exclusive
    protected final int randomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }


    protected final String randomString(int length) {
        return random.ints(97, 122 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    protected final String randomName(String prefix) {
//        UUID uid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("_");
        sb.append(randomString(10));
//        sb.append(uid);
        return sb.toString();
    }
}
