package me.edoardo.test.job.old_alchemist.presets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem {

    private final String title;
    private final List<String> lore;
    private final int customModelData;
    private final Material material;
    private final List<CustomItemType> customItemType;
    private final Rarity rarity;
    private final CustomItemPreset customItemPreset;

    public CustomItem(String title, List<String> lore, int customModelData, Material material, List<CustomItemType> customItemType, Rarity rarity) {
        this.title = title;
        this.lore = lore;
        this.customModelData = customModelData;
        this.material = material;
        this.customItemType = customItemType;
        this.rarity = rarity;
        this.customItemPreset = CustomItemPreset.getCustomItemPreset(customModelData,material);
    }

    public CustomItem(CustomItemPreset customItemPreset) {
        this.customItemPreset = customItemPreset;
        this.title = customItemPreset.getTitle();
        this.lore = customItemPreset.getLore();
        this.customModelData = customItemPreset.getCustomModelData();
        this.material = customItemPreset.getMaterial();
        this.customItemType = customItemPreset.getTypes();
        this.rarity = customItemPreset.getRarity();
    }

    public CustomItem(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        this.title = meta.getDisplayName();
        this.lore = meta.getLore();
        this.customModelData = meta.getCustomModelData();
        this.material = itemStack.getType();
        this.customItemPreset = CustomItemPreset.getCustomItemPreset(customModelData,material);
        this.rarity = customItemPreset.getRarity();
        this.customItemType = customItemPreset.getTypes();
    }


    public ItemStack getItemStack(){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title);
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;

    }

    // METODI BASE (GET/SET)
    public List<CustomItemType> getCustomItemType() {
        return customItemType;
    }

    public EssenceType getEssenceType() {
        return customItemPreset.getEssenceType();
    }

    public double getEssenceTypePercentage() {
        return customItemPreset.getPercentage();
    }
}
