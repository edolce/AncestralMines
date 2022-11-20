package me.edoardo.test.job.blacksmith.presets;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MineralItemRefined {

    private final MineralItem mineralItem;
    private final MineralRefinedQuality quality;

    public MineralItemRefined(MineralItem mineralItem, MineralRefinedQuality quality) {
        this.mineralItem = mineralItem;
        this.quality = quality;
    }

    ItemStack getItemStack(){
        ItemStack item = new ItemStack(mineralItem.getMineralType().getRefinedMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Refined - "+mineralItem.getTitle());
        meta.setLore(Arrays.asList(""));

        item.setItemMeta(meta);

        return item;
    }
}
