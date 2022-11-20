package me.edoardo.test.miniere;

import me.edoardo.test.miniere.ancestral.Ancestral;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum MineLevel {
    LEVEL1(
            1,
            new MiningBlock[]{MiningBlock.SOFT_STONE1},
            new int[]{100},
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2},
            new int[]{90,10},
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2},
            new int[]{80,20},
            5, 8, Ancestral.ANCESTRAL1),
    LEVEL2(
            2,
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2},
            new int[]{80,20},
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2},
            new int[]{60,40},
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2,MiningBlock.SOFT_STONE3},
            new int[]{30,55,15},
            7, 14, Ancestral.ANCESTRAL2),
    LEVEL3(
            3,
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2},
            new int[]{50,50},
            new MiningBlock[]{MiningBlock.SOFT_STONE1,MiningBlock.SOFT_STONE2,MiningBlock.SOFT_STONE3},
            new int[]{10,60,30},
            new MiningBlock[]{MiningBlock.SOFT_STONE2,MiningBlock.SOFT_STONE3},
            new int[]{50,50},
            10, 20, Ancestral.ANCESTRAL2);
//    LEVEL4(4),
//    LEVEL5(5),
//    LEVEL6(6),
//    Level7(7);

    private final List<String> randomNames = Arrays.asList(
            "La cavità erosa",
            "Il vuoto struggente",
            "La Grotta Perturbante",
            "La cavità violenta",
            "Avonbriand Sotterraneo",
            "Caverne di Shrewgrave",
            "Grotta Ladiaca",
            "Cavità terrestri",
            "Caverne di Votport",
            "Glasgan Hollow"
    );

    private final int levelInt;
    private final int ancestralsQuantity;
    private final int maxMineralQuantity;
    private final MineBlockSet mineBlockSet;
    private final Ancestral ancestral;

    MineLevel(int levelInt, MiningBlock[] firstLayerBlocks, int[] firstLayerPercentage, MiningBlock[] secondLayerBlocks, int[] secondLayerPercentage, MiningBlock[] thirdLayerBlocks, int[] thirdLayerPercentages, int ancestralsQuantity, int maxMineralQuantity, Ancestral ancestral){
        this.levelInt = levelInt;
        this.ancestralsQuantity = ancestralsQuantity;
        this.maxMineralQuantity = maxMineralQuantity;
        this.ancestral = ancestral;

        HashMap<MiningBlock,Integer> firstLayer = new HashMap<>();
        for(int i=0;i<firstLayerBlocks.length;i++) {
               firstLayer.put(firstLayerBlocks[i],firstLayerPercentage[i]);
        }
        HashMap<MiningBlock,Integer> secondLayer = new HashMap<>();
        for(int i=0;i<secondLayerBlocks.length;i++) {
            secondLayer.put(secondLayerBlocks[i],secondLayerPercentage[i]);
        }
        HashMap<MiningBlock,Integer> thirdLayer = new HashMap<>();
        for(int i=0;i<thirdLayerBlocks.length;i++) {
            thirdLayer.put(thirdLayerBlocks[i],thirdLayerPercentages[i]);
        }

        this.mineBlockSet = new MineBlockSet(firstLayer,secondLayer,thirdLayer);

    }

    public int getLevelInt() {
        return levelInt;
    }
    public MineBlockSet getMineBlockSet() {
        return mineBlockSet;
    }
    public int getAncestralsQuantity() {
        return ancestralsQuantity;
    }
    public int getMaxMineralQuantity() {
        return maxMineralQuantity;
    }

    public Ancestral getAncestral() {
        return ancestral;
    }
}
