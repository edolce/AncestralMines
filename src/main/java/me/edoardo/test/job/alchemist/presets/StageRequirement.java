package me.edoardo.test.job.alchemist.presets;

public class StageRequirement {

    private final Ingredient ingredient;
    private final int stageNumberToBeUnlocked;


    public StageRequirement(Ingredient ingredient, int stageNumberToBeUnlocked) {
        this.ingredient = ingredient;
        this.stageNumberToBeUnlocked = stageNumberToBeUnlocked;
    }
}
