package me.edoardo.test.job.alchemist.presets;

import java.util.Arrays;
import java.util.List;

public class Stage {
    private final double successRate;
    private final double failRate;
    private final double discoveryRate;
    private final int essenceQuantity;
    private final int stageNumber;
    private final StageGame stageGame;
    private final List<StageRequirement> stageRequirements;

    public Stage(double successRate, double failRate, double discoveryRate, int essenceQuantity, int stageNumber,StageGame stageGame, StageRequirement... stageRequirements) {
        this.successRate = successRate;
        this.failRate = failRate;
        this.discoveryRate = discoveryRate;
        this.essenceQuantity = essenceQuantity;
        this.stageNumber = stageNumber;
        this.stageRequirements = Arrays.asList(stageRequirements);
        this.stageGame = stageGame;
    }

    //GETTER
    public double getFailRate() {
        return failRate;
    }
    public double getSuccessRate() {
        return successRate;
    }
    public int getEssenceQuantity() {
        return essenceQuantity;
    }
    public int getStageNumber() {
        return stageNumber;
    }

    public StageGame getStageGame() {
        return stageGame;
    }
}
