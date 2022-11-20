package me.edoardo.test.job.blacksmith.presets;

import me.edoardo.test.miniere.MiningBlock;

public enum MineralBlock {

    IRON_MINERAL_ORE(MiningBlock.IRON_MINERAL_ORE,MiningBlock.SOFT_STONE1),
    GOLD_MINERAL_ORE(MiningBlock.GOLD_MINERAL_ORE,MiningBlock.SOFT_STONE2),
    DIAMOND_MINERAL_ORE(MiningBlock.DIAMOND_MINERAL_ORE,MiningBlock.SOFT_STONE3);

    private final MiningBlock miningBlock;
    private final MiningBlock dropFrom;

    MineralBlock(MiningBlock miningBlock,MiningBlock dropFrom){
        this.miningBlock=miningBlock;
        this.dropFrom=dropFrom;
    }

    public MiningBlock getMiningBlock() {
        return miningBlock;
    }

    public MiningBlock getDropFrom() {
        return dropFrom;
    }
}
