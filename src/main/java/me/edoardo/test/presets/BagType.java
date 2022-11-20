package me.edoardo.test.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.base.presets.Storage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BagType {

    DIRTBAG(
            2,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.DIRT,
            new String[]{"Dirt","","dirt: %s",""},
            ColorUtils.translateColorCodes("&4&lDIRT BAG"),
    Storage.DIRT),
    STONEBAG(
            3,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.STONE,
            new String[]{"Stone","","stone: %s",""},
            ColorUtils.translateColorCodes("&4&lSTONE BAG"),
    Storage.STONE),
    WOODBAG(
            4,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.WOOD,
            new String[]{"Wood","","dirt: %s",""},
            ColorUtils.translateColorCodes("&4&lWOOD BAG"),
    Storage.WOOD),
    LEAFBAG(
            5,
            Material.LEATHER_HORSE_ARMOR,
            NewDrop.LEAF,
            new String[]{"Leaf","","dirt: %s",""},
            ColorUtils.translateColorCodes("&4&lLEAF BAG"),
    Storage.LEAF),;

    final private int customModelData;
    final private Material material;
    final private NewDrop containerType;
    final private ItemStack blankBagItem;
    final private Storage compatibleStorage;
    final private String displayName;
    final private List<String> lore;

    BagType(int customModelData, Material material, NewDrop containerType, String[] lore, String title, Storage compatibleStorage){
        this.customModelData=customModelData;
        this.material=material;
        this.containerType=containerType;
        this.blankBagItem= new ItemStack(material);
        this.compatibleStorage = compatibleStorage;
        this.displayName=title;
        this.lore= Arrays.asList(lore);
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



    public static BagType getCorrespondingBagType(ItemStack item){
        for(BagType bagType:BagType.values()){
            if(bagType.isThisBag(item.getItemMeta().getCustomModelData())){
                return bagType;
            }
        }
        return null;
    }

    public Storage getCompatibleStorage() {
        return compatibleStorage;
    }

    //TODO: RIMPIAZZARE CON UNA CLASSE BAG E TRADFORMARE QUESTA IN CLASSE BAGTYPE

    public static int getResources(ItemStack item){
        ItemMeta meta = item.getItemMeta();

        int resources = 0;

        List<String> lore= meta.getLore();
        for(int i=0;i<lore.size();i++){
            if (lore.get(i).contains(": ")){
                String[] data = lore.get(i).split(": ");
                resources = Integer.parseInt(data[1]);
            }
        }
        return resources;
    }


    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getBlankBagItem() {
        return blankBagItem;
    }

}
