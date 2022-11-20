package me.edoardo.test.job.old_alchemist.presets;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum CustomItemPreset {


    TYPE1_MINION_DROP(
            1,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            EssenceType.TYPE1,
            0.30,
            Arrays.asList(CustomItemType.INGREDIENTS),
            Arrays.asList("LORE1","LORE2"),
            "TYPE1_MINION_DROP"
    ),
    TYPE2_MINION_DROP(
            2,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            EssenceType.TYPE2,
            0.30,
            Arrays.asList(CustomItemType.INGREDIENTS),
            Arrays.asList("LORE1","LORE2"),
            "TYPE2_MINION_DROP"
    ),
    TYPE3_MINION_DROP(
            3,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            EssenceType.TYPE3,
            0.30,
            Arrays.asList(CustomItemType.INGREDIENTS),
            Arrays.asList("LORE1","LORE2"),
            "TYPE3_MINION_DROP"
    ),
    TYPE4_MINION_DROP(
            4,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            EssenceType.TYPE4,
            0.30,
            Arrays.asList(CustomItemType.INGREDIENTS),
            Arrays.asList("LORE1","LORE2"),
            "TYPE4_MINION_DROP"
    );

    private final int customModelData;
    private final Material material;
    private final Rarity rarity;
    private final EssenceType essenceType;
    private final double percentage;
    private final List<CustomItemType> types;
    private final List<String> lore;
    private final String title;


    CustomItemPreset(int customModelData, Material material, Rarity rarity, EssenceType essenceType, double percentage, List<CustomItemType> types, List<String> lore, String title) {
        this.customModelData = customModelData;
        this.material = material;
        this.rarity=rarity;
        this.essenceType = essenceType;
        this.percentage = percentage;
        this.types = types;
        this.lore = lore;
        this.title = title;
    }




    //Get CustomItem from customModelData and Material
    public static CustomItemPreset getCustomItemPreset(int cmd,Material mat){
        for(CustomItemPreset customItem:CustomItemPreset.values()){
            if(customItem.customModelData==cmd & customItem.material==mat){
                return customItem;
            }
        }
        return null;
    }

    //BASIC METODHS


    public int getCustomModelData() {
        return customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getTitle() {
        return title;
    }

    public Material getMaterial() {
        return material;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public List<CustomItemType> getTypes(){
        return types;
    }

    public EssenceType getEssenceType() {
        return essenceType;
    }

    public double getPercentage() {
        return percentage;
    }
}
