package ru.codefrom.test.ai.brean.transformators;

import lombok.Data;
import ru.codefrom.test.ai.brean.general.Constants;
import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.general.Shape;

import java.util.List;

@Data
public class BiomesCompactor extends AbstractTransformator<Biome> {

    public BiomesCompactor(List<Biome> biomesList, long seed) {
        super(biomesList, seed);
    }

    public void transform() {
        // 1. explode shapes
        int currentHeight = 0;
        for (int i = 0; i < objects.size(); i++) {
            int[] position = new int[3];
            int height = getBoundingBoxSize(objects.get(i).getBaseDescription().getShape(), objects.get(i).getBaseDescription().getBoundaries());
            position[0] = currentHeight;
            position[1] = random.nextInt(Constants.MAX_EXPLODE_SIZE);
            position[2] = random.nextInt(Constants.MAX_EXPLODE_SIZE);
            objects.get(i).moveTo(position);
            currentHeight += height;
        }

        // 2. randomize rotations
        for (int i = 0; i < objects.size(); i++) {
            double[] rotation = new double[3];
            rotation[0] = random.nextDouble() * Math.PI;
            rotation[1] = random.nextDouble() * Math.PI;
            rotation[2] = random.nextDouble() * Math.PI;
            objects.get(i).rotateTo(rotation);
        }

        // 3. implode shapes
        // TODO : implode shapes
    }

    private int getBoundingBoxSize(Shape shape, int[] boundaries) {
        // TODO: boundary box now is very approximate
        switch (shape) {
            case BOX:
                return Math.max(Math.max(boundaries[0], boundaries[1]), boundaries[2]);
            case SPHERE:
                return 2 * boundaries[0];
            case CYLINDER:
                return Math.max(boundaries[0], boundaries[1]);
        }
        return 0;
    }
}
