package me.edoardo.test.job.old_alchemist.presets;

public enum BuffType {

    DAMAGE(1),
    LIFESTEAL(1),
    ATTACK_SPEED(1),
    HP(1),
    RESISTENCE(1),
    REGENERATION(1),
    SPEED(1);

    private final double incrementPerEssence;


    BuffType(double incrementPerEssence) {
        this.incrementPerEssence = incrementPerEssence;
    }

    public double getIncrementPerEssence() {
        return incrementPerEssence;
    }
}
