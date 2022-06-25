package me.edoardo.test.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pickaxe {

    private String title;
    private final String symbol;
    private List<String> lore = new ArrayList<>();
    private final CustomBlock[] breakableBlocks;
    private final int speedLevel;
    private final Material material;
    private final int level;
    private final double xp;
    private final String mainColor;
    //#8a0000
    private String itemName;
    private int modelData;

    public Pickaxe(int modelData,String title, String symbol, CustomBlock[] breakableBlocks, int speedLevel, Material material, int level, double xp, String mainColor) {
        this.title = title;
        this.symbol = symbol;
        this.breakableBlocks = breakableBlocks;
        this.speedLevel = speedLevel;
        this.material = material;
        this.level = level;
        this.xp = xp;
        this.mainColor = mainColor;
        this.modelData = modelData;

        this.itemName = String.format("&#a3a3a3%s &#ffffff- &#3d3d3d[&%s%s&#3d3d3d]",title,mainColor,symbol);

        this.setLore();



    }



    public ItemStack getItemStack() {
        ItemStack itemStack=new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ColorUtils.translateColorCodes(itemName));
        meta.setLore(this.lore);
        meta.setCustomModelData(this.modelData);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(meta);



        return itemStack;
    }


    private void setLore(){

        String[] arrayS= new String[]{
                "",
                String.format("&#3d3d3d--==|&%sLivello&#3d3d3d|==--",mainColor),
                String.format("&#a3a3a3&l%s",level),
                "",
                String.format("&#3d3d3d--==|&%sUtilita&#3d3d3d|==--",mainColor),
                "&#a3a3a3Raccolta Minerali",
                "",
                String.format("&#3d3d3d--==|&%sRarita&#3d3d3d|==--",mainColor),
                "&#a3a3a3Comune",
                "",
                "&#3d3d3d#000001"
        };

        for (String s:this.centerLines(arrayS)){
            System.out.println(s);
            this.lore.add(ColorUtils.translateColorCodes(s));
        }
    }
    private List<String> centerLines(String[] toCenterA) {
        List<String> toCenter = new ArrayList<>(Arrays.asList(toCenterA));

        toCenter.add(itemName);

        List<String> newLore = Util.fixLines(toCenter);

        this.itemName = newLore.get(newLore.size()-1);
        newLore.remove(newLore.size()-1);

        return newLore;

    }
}
