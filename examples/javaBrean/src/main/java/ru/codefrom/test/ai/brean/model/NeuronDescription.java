package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NeuronDescription {
    @Builder.Default
    int refractoryPeriod = 3;
    @Builder.Default
    int fireThreshold = 15;
}
