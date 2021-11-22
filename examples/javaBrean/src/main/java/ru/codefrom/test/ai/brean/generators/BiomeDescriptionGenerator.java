package ru.codefrom.test.ai.brean.generators;

import ru.codefrom.test.ai.brean.model.BiomeDescription;
import ru.codefrom.test.ai.brean.model.BiomePopulationDescription;
import ru.codefrom.test.ai.brean.model.BiomeShape;
import ru.codefrom.test.ai.brean.model.NeuronType;

import java.util.ArrayList;
import java.util.List;

public class BiomeDescriptionGenerator extends AbstractGenerator<BiomeDescription> {
    public static int populationsMaxCount = 10;
    public static int volumeMaxSize = 1000;

    public BiomeDescriptionGenerator(long seed) {
        super(seed);
    }

    @Override
    protected BiomeDescription generateOne() {
        // 1. generate shape
        // 1.1 choose shape type
        BiomeShape shape = randomEnum(BiomeShape.class);
        // 1.2 generate boundaries
        int volume = randomCount(volumeMaxSize);
        int[] boundaries = generateBoundaries(shape, volume);
        int realVolume = getVolume(shape, boundaries);
        // 2. generate populations
        // 2.1. generate populations count
        int populationsCount = randomCount(populationsMaxCount);
        // 2.2. generate each population description
        List<BiomePopulationDescription> populations = new ArrayList<>(populationsCount);
        for (int i = 0; i < populationsCount; i++) {
            populations.add(generatePopulation(realVolume));
        }

        // 3. build biome description
        return BiomeDescription.builder()
                .shape(shape)
                .boundaries(boundaries)
                .populations(populations)
                .build();
    }

    private BiomePopulationDescription generatePopulation(int volume) {
        NeuronType type = randomEnum(NeuronType.class);
        int neuronsCount = randomCount(volume);
        return BiomePopulationDescription.builder()
                .neuronType(type)
                .count(neuronsCount)
                .build();
    }

    private int[] generateBoundaries(BiomeShape shape, int volume) {
        switch (shape) {
            case BOX:
                // volume = width * height * depth
                int width = randomCount(volume);
                int height = randomCount((volume / width));
                int depth = randomCount(volume / (width * height));
                return new int[]{width, height, depth};
            case SPHERE:
                // volume = 4/3 * Pi * radius ^ 3
                // radius = cbrt( 3/4 * volume / Pi )
                int maxRadius = (int)Math.round(Math.cbrt((3.0 * (double) volume)) / (4.0 * Math.PI));
                maxRadius = (maxRadius == 0) ? 1 : maxRadius;
                int radius = randomCount(maxRadius);
                return new int[]{radius};
            case CYLINDER:
                // volume = height * Pi * radius ^ 2
                int heightCyl = randomCount(volume);
                // radius = sqrt(volume / (height * Pi))
                int maxRadiusCyl = (int)Math.round(Math.sqrt((double)volume / ((double)heightCyl * Math.PI)));
                maxRadiusCyl = (maxRadiusCyl == 0) ? 1 : maxRadiusCyl;
                int radiusCyl = randomCount(maxRadiusCyl);
                return new int[]{radiusCyl, heightCyl};
            default:
                return new int[0];
        }
    }

    private int getVolume(BiomeShape shape, int[] boundaries) {
        switch (shape) {
            case BOX:
                // volume = width * height * depth
                return boundaries[0] * boundaries[1] * boundaries[2];
            case SPHERE:
                // volume = 4/3 * Pi * radius ^ 3
                return (int)Math.round((4.0 * Math.PI * Math.pow(boundaries[0], 3.0) / 3.0));
            case CYLINDER:
                // volume = height * Pi * radius ^ 2
                return boundaries[1] * (int)Math.round(Math.PI * Math.pow(boundaries[0], 2.0));
            default:
                return -1;
        }
    }
}
