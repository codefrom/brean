package ru.codefrom.test.ai.brean.transformators;

import lombok.Data;
import ru.codefrom.test.ai.brean.model.Biome;

import java.util.List;

@Data
public class BiomesCompactor extends AbstractTransformator<Biome> {
    private List<Biome> biomes;

    public void compact() {
        // 1. randomize rotations
        for (int i = 0; i < biomes.size(); i++) {
            double[] rotation = new double[3];
            rotation[0] = random.nextDouble() * Math.PI;
            rotation[1] = random.nextDouble() * Math.PI;
            rotation[2] = random.nextDouble() * Math.PI;

            biomes.get(i).rotateTo(rotation);
        }

        // 2. explode shapes

        // 3. implode shapes

    }
}
