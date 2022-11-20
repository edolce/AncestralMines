package me.edoardo.test.miniere.ancestral;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Util;
import me.edoardo.test.job.old_alchemist.presets.CustomItemType;
import me.edoardo.test.job.old_alchemist.presets.EssenceType;
import me.edoardo.test.job.old_alchemist.presets.Rarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AncestralDrop {
    PIETRA_STRANA(
            1,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            "PIETRA_STRANA"
    ),
    PIETRA_CREPUSCOLARE(
            2,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            "TYPE2_MINION_DROP"
    ),
    PIETRA_MIRACOLOSA(
            3,
            Material.GOLD_INGOT,
            Rarity.COMMON,
            "TYPE3_MINION_DROP"
    );

    private final int customModelData;
    private final Material material;
    private final Rarity rarity;
    private final List<String> lore=new ArrayList<>();
    private String title;
    private final String mainColor = "#8a0000";


    AncestralDrop(int customModelData, Material material, Rarity rarity, String title) {
        this.customModelData = customModelData;
        this.material = material;
        this.rarity = rarity;
        this.title = String.format("&#a3a3a3%s &#ffffff- &#3d3d3d[&%s%s&#3d3d3d]", title, mainColor, "〶");
        initLore();
    }


    //LORE AND TITLE INDENTATION AND COLORATION
    private void initLore() {
        String[] arrayS = new String[]{
                "",
                String.format("&#3d3d3d--==|&%sMateriale Da Ricerca&#3d3d3d|==--", mainColor),
                "&#a3a3a3Utile All'Alchimista",
                "",
                String.format("&#3d3d3d--==|&%sUtilita&#3d3d3d|==--", mainColor),
                "&#a3a3a3Creazione Di Recipe",
                "",
                String.format("&#3d3d3d--==|&%sRarita&#3d3d3d|==--", mainColor),
                rarity.getName()
        };

        for (String s : this.centerLines(arrayS)) {
            System.out.println(s);
            lore.add(ColorUtils.translateColorCodes(s));
        }
    }
    private List<String> centerLines(String[] toCenterA) {
        List<String> toCenter = new ArrayList<>(Arrays.asList(toCenterA));
        toCenter.add(title);
        List<String> newLore = Util.fixLines(toCenter);
        this.title = newLore.get(newLore.size() - 1);
        newLore.remove(newLore.size() - 1);
        return newLore;

    }
    public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(ColorUtils.translateColorCodes(title));
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
