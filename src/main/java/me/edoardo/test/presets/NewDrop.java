package me.edoardo.test.presets;

import me.edoardo.test.custom_blocks.CustomBlock;

import java.util.HashMap;

public enum NewDrop {

    STONE(new HashMap<CustomBlock,Integer>(){{
        put(CustomBlock.SOFT_STONE1,5);
        put(CustomBlock.SOFT_STONE2,10);
        put(CustomBlock.SOFT_STONE3,20);
//        put(CustomBlock.NORMAL_STONE1,50);
//        put(CustomBlock.NORMAL_STONE2,100);
//        put(CustomBlock.NORMAL_STONE3,200);
//        put(CustomBlock.HARD_STONE1,350);
//        put(CustomBlock.HARD_STONE2,600);
//        put(CustomBlock.HARD_STONE3,1000);
    }}),
    DIRT(new HashMap<CustomBlock,Integer>(){{
//        put(CustomBlock.SOFT_STONE1,5);
//        put(CustomBlock.SOFT_STONE2,10);
//        put(CustomBlock.SOFT_STONE3,20);
        put(CustomBlock.NORMAL_STONE1,50);
        put(CustomBlock.NORMAL_STONE2,100);
        put(CustomBlock.NORMAL_STONE3,200);
//        put(CustomBlock.HARD_STONE1,350);
//        put(CustomBlock.HARD_STONE2,600);
//        put(CustomBlock.HARD_STONE3,1000);
    }}),
    LEAF(new HashMap<CustomBlock,Integer>(){{
//        put(CustomBlock.SOFT_STONE1,5);
//        put(CustomBlock.SOFT_STONE2,10);
//        put(CustomBlock.SOFT_STONE3,20);
//        put(CustomBlock.NORMAL_STONE1,50);
//        put(CustomBlock.NORMAL_STONE2,100);
//        put(CustomBlock.NORMAL_STONE3,200);
//        put(CustomBlock.HARD_STONE1,350);
//        put(CustomBlock.HARD_STONE2,600);
//        put(CustomBlock.HARD_STONE3,1000);
    }}),
    WOOD(new HashMap<CustomBlock,Integer>(){{
//        put(CustomBlock.SOFT_STONE1,5);
//        put(CustomBlock.SOFT_STONE2,10);
//        put(CustomBlock.SOFT_STONE3,20);
//        put(CustomBlock.NORMAL_STONE1,50);
//        put(CustomBlock.NORMAL_STONE2,100);
//        put(CustomBlock.NORMAL_STONE3,200);
        put(CustomBlock.HARD_STONE1,350);
        put(CustomBlock.HARD_STONE2,600);
        put(CustomBlock.HARD_STONE3,1000);
    }});

    final private HashMap<CustomBlock, Integer> materials;

    NewDrop(HashMap<CustomBlock, Integer> materials){
        this.materials=materials;
    }

    public HashMap<CustomBlock, Integer> getMaterials() {
        return materials;
    }
}
