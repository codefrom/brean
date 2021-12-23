package ru.codefrom.test.ai.brean;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.codefrom.test.ai.brean.generators.BiomeDescriptionGenerator;
import ru.codefrom.test.ai.brean.generators.BiomeGenerator;
import ru.codefrom.test.ai.brean.model.Biome;
import ru.codefrom.test.ai.brean.model.BiomeDescription;
import ru.codefrom.test.ai.brean.transformators.BiomesCompactor;
import ru.codefrom.test.ai.brean.transformators.NeuronsCompactor;
import ru.codefrom.test.ai.brean.transformators.NeuronsConnector;

import java.util.List;

public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        long seed = 123L;
        int biomeDescriptionMaximumCount = 10;
        int biomesMaximumCount = 5;

        // ------------------------------------------------
        // --- I. Generating random brain -----------------
        // ------------------------------------------------

        // 1. generate biome descriptions
        BiomeDescriptionGenerator biomeDescriptionGenerator = new BiomeDescriptionGenerator(seed);
        List<BiomeDescription> biomeDescriptionList = biomeDescriptionGenerator.generate(biomeDescriptionMaximumCount);

        String biomeDescriptions = StringUtils.join(biomeDescriptionList, "\n");

        logger.info("============= Biome Descriptions ============");
        logger.info(biomeDescriptions);

        // 2. generate biomes
        BiomeGenerator biomeGenerator = new BiomeGenerator(biomeDescriptionList, seed);
        List<Biome> biomesList = biomeGenerator.generate(biomesMaximumCount);

        String biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Biomes ========================");
        logger.info(biomes);

        // 3. position biomes in space
        BiomesCompactor biomesCompactor = new BiomesCompactor(biomesList, seed);
        biomesCompactor.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Biomes compacted ==============");
        logger.info(biomes);

        // 4. compact neurons
        NeuronsCompactor neuronsCompactor = new NeuronsCompactor(biomesList, seed);
        neuronsCompactor.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Neurons compacted =============");
        logger.info(biomes);

        // 5. connect neurons (at random to nearest ones)
        NeuronsConnector neuronsConnector = new NeuronsConnector(biomesList, seed);
        neuronsConnector.transform();

        biomes = StringUtils.join(biomesList, "\n");

        logger.info("============= Neurons connected =============");
        logger.info(biomes);

        // 6. Create sensors
        
        // 7. Connect sensors

        // 8. Create actuators

        // 9. Connect actuators

    }
}
