package me.edoardo.test.miniere.ancestral;

import java.util.Random;

public enum SecretType {
    NORMAL(1, 1, 1, 1, 1, 1, 1, 1, 1),
    DUNGEON(1.5, 0.5, 1, 1, 1, 1, 1, 1, 1),
    MINERAL_RAIN(0.5, 1.5, 1, 1, 1, 1, 1, 1, 1);

    private final double monsterSpawnMultiplier;
    //MAX MINERALS TO FIND IN CAVES
    private final double mineralSpawnMultiplier;
    private final double roomMultiplier;
    private final double timeMultiplier;
    private final double stoneMultiplier;
    private final double woodMultiplier;
    private final double bronzeMultiplier;
    private final double ironMultiplier;
    private final double goldMultiplier;

    SecretType(double monsterSpawnMultiplier, double mineralSpawnMultiplier, double roomMultiplier, double timeMultiplier, double stoneMultiplier, double woodMultiplier, double bronzeMultiplier, double ironMultiplier, double goldMultiplier){
        this.monsterSpawnMultiplier = monsterSpawnMultiplier;
        this.mineralSpawnMultiplier = mineralSpawnMultiplier;
        this.roomMultiplier = roomMultiplier;
        this.timeMultiplier = timeMultiplier;
        this.stoneMultiplier = stoneMultiplier;
        this.woodMultiplier = woodMultiplier;
        this.bronzeMultiplier = bronzeMultiplier;
        this.ironMultiplier = ironMultiplier;
        this.goldMultiplier = goldMultiplier;
    }

    public static SecretType getRandom(Random rand) {
        return SecretType.values()[rand.nextInt(SecretType.values().length)];
    }

    public double getBronzeMultiplier() {
        return bronzeMultiplier;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }

    public double getIronMultiplier() {
        return ironMultiplier;
    }

    public double getMineralSpawnMultiplier() {
        return mineralSpawnMultiplier;
    }

    public double getMonsterSpawnMultiplier() {
        return monsterSpawnMultiplier;
    }

    public double getRoomMultiplier() {
        return roomMultiplier;
    }

    public double getStoneMultiplier() {
        return stoneMultiplier;
    }

    public double getTimeMultiplier() {
        return timeMultiplier;
    }

    public double getWoodMultiplier() {
        return woodMultiplier;
    }
}
