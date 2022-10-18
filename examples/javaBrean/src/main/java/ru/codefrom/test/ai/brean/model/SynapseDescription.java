package ru.codefrom.test.ai.brean.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SynapseDescription {
    @Builder.Default
    int minStrength = 0;
    @Builder.Default
    int maxStrength = 15;
}
