package me.edoardo.test.miniere;

import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.custom_blocks.CustomBlock;

public enum MiningBlock {


    SOFT_STONE1(CustomBlock.SOFT_STONE1,10,HardnessLevel.Level0, Storage.STONE, 10,null, 1),
    SOFT_STONE2(CustomBlock.SOFT_STONE2,50,HardnessLevel.Level1, Storage.STONE, 100,CustomBlock.SOFT_STONE1, 0.05),
    SOFT_STONE3(CustomBlock.SOFT_STONE3,100,HardnessLevel.Level2, Storage.STONE, 200,CustomBlock.SOFT_STONE2, 0.10),
    NORMAL_STONE1(CustomBlock.NORMAL_STONE1,200,HardnessLevel.Level3, Storage.STONE, 500,null, 0),
    NORMAL_STONE2(CustomBlock.NORMAL_STONE2,500,HardnessLevel.Level4, Storage.STONE, 1000,CustomBlock.NORMAL_STONE1, 0),
    NORMAL_STONE3(CustomBlock.NORMAL_STONE3,2000,HardnessLevel.Level5, Storage.STONE, 3000,CustomBlock.NORMAL_STONE2, 0),
    HARD_STONE1(CustomBlock.HARD_STONE1,5000,HardnessLevel.Level6, Storage.STONE, 7000,null, 0),
    HARD_STONE2(CustomBlock.HARD_STONE2,10000,HardnessLevel.Level7, Storage.STONE, 15000,CustomBlock.HARD_STONE1, 0),
    HARD_STONE3(CustomBlock.HARD_STONE3,50000,HardnessLevel.Level8, Storage.STONE, 100000,CustomBlock.HARD_STONE2, 0),
    IRON_MINERAL_ORE(CustomBlock.IRON_MINERAL_ORE,10,HardnessLevel.Level0, null,0,null, 0),
    GOLD_MINERAL_ORE(CustomBlock.GOLD_MINERAL_ORE,10,HardnessLevel.Level0, null,0,null, 0),
    DIAMOND_MINERAL_ORE(CustomBlock.DIAMOND_MINERAL_ORE,10,HardnessLevel.Level0, null,0,null, 0);


    private final Storage bagType;
    private final int dropQuantity;
    private final HardnessLevel hardnessLevel;
    //private Material material= null;
    private final CustomBlock customBlock;
    private final int lifePoints;
    private final CustomBlock nextBlock;
    private final double mineralSpawnRate;

    MiningBlock(CustomBlock customBlock, int lifePoints, HardnessLevel hardnessLevel, Storage bagType, int dropQuantity, CustomBlock nextBlock, double mineralSpawnRate){
        this.customBlock=customBlock;
        this.lifePoints= lifePoints;
        this.hardnessLevel= hardnessLevel;
        this.bagType= bagType;
        this.dropQuantity=dropQuantity;
        this.nextBlock=nextBlock;
        this.mineralSpawnRate = mineralSpawnRate;
    }

    public static MiningBlock getMiningBlockFromCustom(CustomBlock matchedBlock) {
        for(MiningBlock miningBlock:MiningBlock.values()){
            if(miningBlock.customBlock==matchedBlock) return miningBlock;
        }
        return null;
    }


    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    public Storage getBagType() {
        return bagType;
    }

    public HardnessLevel getHardnessLevel() {
        return hardnessLevel;
    }

    public int getDropQuantity() {
        return dropQuantity;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public CustomBlock getNextBlock() {
        return nextBlock;
    }

    public double getMineralSpawnRate() {
        return mineralSpawnRate;
    }
}
