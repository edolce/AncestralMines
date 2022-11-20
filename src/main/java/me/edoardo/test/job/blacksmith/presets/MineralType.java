package me.edoardo.test.job.blacksmith.presets;

import me.edoardo.test.job.old_alchemist.presets.Rarity;
import org.bukkit.Material;

public enum MineralType {
    NONE(Material.AIR,Material.AIR, 0, ""),
    IRON(Material.DIAMOND_ORE,Material.DIAMOND, 1, "Ferro Del Sottosopra"),
    GOLD(Material.DIAMOND_ORE,Material.DIAMOND, 2, "Oro Del Re In Rovina"),
    DIAMOND(Material.DIAMOND_ORE,Material.DIAMOND, 3, "Gemma Antica Del Demonio");

    final private Material material;
    final private Material refinedMaterial;
    final private int customModelData;
    final private String name;

    MineralType(Material material, Material refinedMaterial, int customModelData, String name){
        this.material = material;
        this.refinedMaterial = refinedMaterial;
        this.customModelData = customModelData;
        this.name = name;
    }

    //BASIC METHOD GET/SET
    public int getCustomModelData() {
        return customModelData;
    }
    public Material getMaterial() {
        return material;
    }
    public Material getRefinedMaterial() {
        return refinedMaterial;
    }

    public String getName() {
        return name;
    }

    //EXTRA METHOD
    public MineralItem getMineralItem_0Rarity_0Purity(){
        return new MineralItem(this,this.toString(), Rarity.COMMON,MineralPurity.STAR0);
    }
}
