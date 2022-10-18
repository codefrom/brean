package ru.codefrom.test.ai.brean.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HumanlikeGenomeFactory {
//    // Facts:
//    // Human brain weight is about 1300-1400g, volume is about 1200-1300 cm3
//    // contains about 86 000 000 000 neurons
//
//    // Hippocampus
//    // volume is about 3-3.5 cm3 ~0.3%
//    private static final String HIPPOCAMPUS_PREFIX = "Hippocampus_";
//    BiomeDescription CornuAmmonis1;
//    BiomeDescription CornuAmmonis2;
//    BiomeDescription CornuAmmonis3;
//    BiomeDescription DentateGyrus;
//    BiomeDescription Subiculum;
//    BiomeDescription EthoricalCortex;
//
//
//    public Genome createGenome(int volume) {
//        createRegions(volume);
//
//        createConnections();
//
//        List<BiomeDescription> biomes = new ArrayList<>();
//        return Genome.builder()
//                .biomeDescriptions(biomes)
//                .build();
//    }
//
//    private void createConnections() {
//        // INNER HIPPOCAMPUS CONNECTIONS
//        // CA1 is the first region in the hippocampal circuit,
//        // from which a major output pathway goes to layer V of the entorhinal cortex.
//        CornuAmmonis1.connectTo(EthoricalCortex);
//        // Another significant output is to the subiculum.
//        CornuAmmonis1.connectTo(Subiculum);
//
//        // CA2 is a small region located between CA1 and CA3.
//        // It receives some input from layer II of the entorhinal cortex via the perforant path.
//        EthoricalCortex.connectTo(CornuAmmonis2);
//        // Its pyramidal cells are more like those in CA3 than those in CA1. It is often ignored due to its small size.
//        // TODO: is it connected to CA1?
//        CornuAmmonis2.connectTo(CornuAmmonis1);
//
//        // CA3 receives input from the mossy fibers of the granule cells in the dentate gyrus
//        // The mossy fiber pathway ends in the stratum lucidum.
//        DentateGyrus.connectTo(CornuAmmonis3);
//        // and also from cells in the entorhinal cortex via the perforant path.
//        // The perforant path passes through the stratum lacunosum and ends in the stratum moleculare.
//        EthoricalCortex.connectTo(CornuAmmonis3);
//        // There are also inputs from the medial septum and from the diagonal band of Broca which terminate in the stratum radiatum,
//        // along with commisural connections from the other side of the hippocampus.
//        // The pyramidal cells in CA3 send some axons back to the dentate gyrus hilus,
//        CornuAmmonis3.connectTo(DentateGyrus);
//        // but they mostly project to regions CA2 and CA1 via the Schaffer collaterals.
//        CornuAmmonis3.connectTo(CornuAmmonis2);
//        CornuAmmonis3.connectTo(CornuAmmonis1);
//        // There are also a significant number of recurrent connections that terminate in CA3.
//        CornuAmmonis3.connectTo(CornuAmmonis3);
//        // Both the recurrent connections and the Schaffer collaterals terminate preferentially in the septal area in a dorsal direction from the originating cells.
//        // CA3 also sends a small set of output fibers to the lateral septum.
//        //The region is conventionally divided into three divisions.
//        // CA3a is the part of the cell band that is most distant from the dentate (and closest to CA1).
//        // CA3b is the middle part of the band nearest to the fimbria and fornix connection.
//        // CA3c is nearest to the dentate, inserting into the hilus.
//        // CA3 overall, has been considered to be the “pacemaker” of the hippocampus.
//
//        // CA4 is a misleading term introduced by Lorente de No (1934).
//        // He observed that the pyramidal layer of the CA3 was continuous with polymorphic layer of the dentate gyrus and
//        // that the "modified pyramids" (later known as mossy cells (Amaral, 1978)) had Schaffer collaterals similar to
//        // CA3 pyramdidal cells. Amaral (1978) showed that the mossy cells in the CA4 of Lorente de No did not have schaffer
//        // collaterals and that they in contrast to pyramidal cells project to the inner molecular layer of the DG and not to CA1.
//        // The same author thus concluded that the term CA4 should be abandoned and that the zone should be regarded as the polymorphic layer of the dentate gyrus
//        // (the area dentata of Blackstad (1956)). The polymoprhic layer is often called the hilus or hilar region (Amaral, 2007).
//        // The neurons in the polymorphic layer, including mossy cells and GABAergic interneurons, primarily receive inputs from the granule cells in the dentate gyrus
//        // in the form of mossy fibers and project to the inner molecular layer of the dentate gyrus via the associational/commissural projection .[2][3]
//        // They also receive a small number of connections from pyramidal cells in CA3. They, in turn, project back into the dentate gyrus at distant septotemporal levels.
//
//        EthoricalCortex.connectTo(DentateGyrus);
//        Subiculum.connectTo(CornuAmmonis1);
//        Subiculum.connectTo(EthoricalCortex);
//
//        // outer hippocampus connections
//        // TODO: connect Neurocortex to Ethorial cortex
//    }
//
//    private void createRegions(int volume) {
//        createHippocampus(volume);
//    }
//
//    private int getFraction(int volume, double part) {
//        return (int)Math.round((double)volume * part);
//    }
//
//    private void createHippocampus(int volume) {
//        int hippocampusVolume = getFraction(volume, 0.3);
//
//        // CA1
//        CornuAmmonis1 = createBiomeDescription(HIPPOCAMPUS_PREFIX + "CA1", getFraction(hippocampusVolume, 0.1));
//        // CA2
//        CornuAmmonis2 = createBiomeDescription(HIPPOCAMPUS_PREFIX + "CA2", getFraction(hippocampusVolume, 0.1));
//        // CA3
//        CornuAmmonis3 = createBiomeDescription(HIPPOCAMPUS_PREFIX + "CA3", getFraction(hippocampusVolume, 0.1));
//        // DG
//        DentateGyrus = createBiomeDescription(HIPPOCAMPUS_PREFIX + "DG", getFraction(hippocampusVolume, 0.2));
//        // Subiculum
//        Subiculum = createBiomeDescription(HIPPOCAMPUS_PREFIX + "Subiculum", getFraction(hippocampusVolume, 0.1));
//        // EthoricalCortex
//        EthoricalCortex = createBiomeDescription(HIPPOCAMPUS_PREFIX + "EthoricalCortex", getFraction(hippocampusVolume, 0.3));
//    }
//
//    private BiomeDescription createBiomeDescription(String name, int volume) {
//        return BiomeDescription.builder()
//                .name(name)
//                .populations(Arrays.asList(
//                        PopulationDescription.builder()
//                                .neuronType(NeuronType.INOUT)
//                                .count(volume)
//                                .build()
//                ))
//                .build();
//    }
//
//
}
