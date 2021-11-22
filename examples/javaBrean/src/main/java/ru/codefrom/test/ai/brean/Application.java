package ru.codefrom.test.ai.brean;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.generators.BiomeDescriptionGenerator;
import ru.codefrom.test.ai.brean.model.BiomeDescription;

import java.util.List;

public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        long seed = 123L;
        // 1. generate biome descriptions
        BiomeDescriptionGenerator biomeDescriptionGenerator = new BiomeDescriptionGenerator(seed);
        List<BiomeDescription> biomeDescriptionList = biomeDescriptionGenerator.generate(10);

        String biomeDescriptions = StringUtils.join(biomeDescriptionList, "\n");

        logger.info("============= Biome Descriptions ============");
        logger.info(biomeDescriptions);

        // 2. generate biomes

        // 3. position biomes in space

        // 4. compact neurons

        // 5. connect neurons (at random to nearest ones)
    }
}
