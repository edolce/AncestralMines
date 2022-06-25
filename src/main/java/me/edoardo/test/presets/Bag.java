package me.edoardo.test.presets;

import me.edoardo.test.ColorUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Bag {
    DIRTBAG(
            2,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.DIRT,
            new String[]{"Dirt","","dirt: 0",""},
            ColorUtils.translateColorCodes("&4&lDIRT BAG")),
    STONEBAG(
            3,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.STONE,
            new String[]{"Stone","","dirt: 0",""},
            ColorUtils.translateColorCodes("&4&lSTONE BAG")),
    WOODBAG(
            4,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.WOOD,
            new String[]{"Wood","","dirt: 0",""},
            ColorUtils.translateColorCodes("&4&lWOOD BAG")),
    LEAFBAG(
            5,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.LEAF,
            new String[]{"Leaf","","dirt: 0",""},
            ColorUtils.translateColorCodes("&4&lLEAF BAG")),;

    final private int customModelData;
    final private Material material;

    final private NewDrop containerType;

    final private ItemStack blankBagItem;

    Bag(int customModelData, Material material, NewDrop containerType,String[] lore,String title){
        this.customModelData=customModelData;
        this.material=material;
        this.containerType=containerType;

        this.blankBagItem= new ItemStack(material);
        ItemMeta meta = blankBagItem.getItemMeta();

        assert meta != null;
        meta.setCustomModelData(customModelData);
        meta.setDisplayName(title);

        List<String> realLore = new ArrayList<>();

        realLore.add("");
        realLore.add(lore[0]);
        realLore.add(lore[1]);
        realLore.add(lore[2]);
        realLore.add(lore[3]);

        meta.setLore(realLore);
        blankBagItem.setItemMeta(meta);
    }

    public NewDrop getContainerType() {
        return containerType;
    }

    public boolean isThisBag(int customModelData) {
        return this.customModelData==customModelData;
    }


    public ItemStack getBlankBagItem() {
        return blankBagItem;
    }
}
