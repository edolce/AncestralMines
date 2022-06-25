package me.edoardo.test.presets;

import org.bukkit.Material;

import java.util.HashMap;

public enum NewDrop {

    DIRT(new HashMap<Material,Integer>(){{
        put(Material.COARSE_DIRT,1);
        put(Material.DIRT,1);
        put(Material.GRASS,1);
        put(Material.GRASS_BLOCK,1);
    }}),
    STONE(new HashMap<Material,Integer>(){{
        put(Material.COARSE_DIRT,1);
        put(Material.DIRT,1);
        put(Material.GRASS,1);
        put(Material.GRASS_BLOCK,1);
    }}),
    WOOD(new HashMap<Material,Integer>(){{
        put(Material.COARSE_DIRT,1);
        put(Material.DIRT,1);
        put(Material.GRASS,1);
        put(Material.GRASS_BLOCK,1);
    }}),
    LEAF(new HashMap<Material,Integer>(){{
        put(Material.COARSE_DIRT,1);
        put(Material.DIRT,1);
        put(Material.GRASS,1);
        put(Material.GRASS_BLOCK,1);
    }});

    final private HashMap<Material,Integer> materials;

    NewDrop(HashMap<Material,Integer> materials){
        this.materials=materials;
    }

    public HashMap<Material, Integer> getMaterials() {
        return materials;
    }
}
