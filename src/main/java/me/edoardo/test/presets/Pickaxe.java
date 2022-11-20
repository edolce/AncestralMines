package me.edoardo.test.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.custom_blocks.CustomBlock;
import me.edoardo.test.miniere.HardnessLevel;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Pickaxe {

    private final List<String> lore = new ArrayList<>();
    private final Material material;
    private final HardnessLevel hardnessLevel;
    private final int level;
    private final double xp;
    private final int modelData;
    private final String displayName;
    //Efficienza del piccone stimata tramite valore di danno arrecato a rottura
    private final int blockDamage;


    public Pickaxe(int modelData, String displayName, Material material, HardnessLevel hardnessLevel, int level, double xp, int blockDamage) {
        this.material = material;
        this.hardnessLevel = hardnessLevel;
        this.level = level;
        this.xp = xp;
        this.modelData = modelData;
        this.displayName = displayName;
        this.blockDamage = blockDamage;
        //this.itemName = String.format("&#a3a3a3%s &#ffffff- &#3d3d3d[&%s%s&#3d3d3d]",title,mainColor,symbol);
        //this.itemName = String.format("&#a3a3a3%s &#ffffff- &#3d3d3d[&#3d3d3d]",title);
        //this.setLore();
    }



    public ItemStack getItemStack(){
        ItemStack returnItem = new ItemStack(material,1);
        ItemMeta itemMeta = returnItem.getItemMeta();
        //SET ITEM META
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(Double.toString(xp),Double.toString(level),hardnessLevel.toString(),Integer.toString(blockDamage)));
        itemMeta.setCustomModelData(modelData);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(),"xp"),PersistentDataType.DOUBLE,xp);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(),"level"),PersistentDataType.INTEGER,level);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(),"hardnessLevel"),PersistentDataType.STRING,hardnessLevel.toString());
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(),"customItem"),PersistentDataType.STRING,"pickaxe");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(),"blockDamage"),PersistentDataType.INTEGER,blockDamage);

        returnItem.setItemMeta(itemMeta);
        //RETURN ITEM
        return returnItem;
    }

//    public ItemStack getItemStack() {
//        ItemStack itemStack=new ItemStack(material);
//
//        ItemMeta meta = itemStack.getItemMeta();
//
//        meta.setDisplayName(ColorUtils.translateColorCodes(itemName));
//        meta.setLore(this.lore);
//        meta.setCustomModelData(this.modelData);
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//
//        itemStack.setItemMeta(meta);
//
//
//
//        return itemStack;
//    }
//    private void setLore(){
//
//        //SETUP LEVEL BAR
//        StringBuilder filledLevelBar= new StringBuilder();
//        StringBuilder notFilledLevelBar= new StringBuilder();
//
//        double v,x;
//        if (level == 1) {
//            v = xp;
//            x = v/(levelStage.get(level)-0);
//        }else {
//            v = xp - levelStage.get(level-1);
//            x = v/(levelStage.get(level)-levelStage.get(level-1));
//        }
//
//        int filledXpSquares = (int) (x/0.10);
//        int notFilledXpSquares = 10-filledXpSquares;
//
//        System.out.println("v: "+v);
//        System.out.println("x: "+x);
//
//        for (int i=0;i<filledXpSquares;i++){
//            filledLevelBar.append("■");
//        }
//
//        for (int k=0;k<notFilledXpSquares;k++){
//            notFilledLevelBar.append("□");
//        }
//
//
//        String mainColor = "#3d3d3d";
//        String[] arrayS= new String[]{
//                "",
//                String.format("&#3d3d3d--==|&%sLivello&#3d3d3d|==--", mainColor),
//                String.format("&#a3a3a3&l%s",level),
//                String.format("&#dbdbdb&l%s&#6b6a6a%s",filledLevelBar,notFilledLevelBar),
//                "",
//                String.format("&#3d3d3d--==|&%sUtilita&#3d3d3d|==--", mainColor),
//                "&#a3a3a3Raccolta Minerali",
//                "",
//                String.format("&#3d3d3d--==|&%sRarita&#3d3d3d|==--", mainColor),
//                "&#a3a3a3Comune",
//                "",
//                "&#3d3d3d#000001"
//        };
//
//        for (String s:this.centerLines(arrayS)){
//            System.out.println(s);
//            this.lore.add(ColorUtils.translateColorCodes(s));
//        }
//    }
//    private List<String> centerLines(String[] toCenterA) {
//        List<String> toCenter = new ArrayList<>(Arrays.asList(toCenterA));
//
//        toCenter.add(itemName);
//
//        List<String> newLore = Util.fixLines(toCenter);
//
//        this.itemName = newLore.get(newLore.size()-1);
//        newLore.remove(newLore.size()-1);
//
//        return newLore;
//
//    }
    public HardnessLevel getHardnessLevel() {
        return hardnessLevel;
    }

    public int getBlockDamage() {return blockDamage;}

    public static Pickaxe getPickaxeFromItemStack(ItemStack itemStack) {
        NamespacedKey customItemKey = new NamespacedKey(Test.getInstance(), "customItem");
        ItemMeta meta = itemStack.getItemMeta();
        if(meta==null) return null;
        String itemType= meta.getPersistentDataContainer().get(customItemKey, PersistentDataType.STRING);
        if(!Objects.equals(itemType, "pickaxe")) return null;

        //Get all info to create a pickaxe object
        int modelData = meta.getCustomModelData();
        String displayName = meta.getDisplayName();

        Integer level = meta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "level"), PersistentDataType.INTEGER);
        Double xp = meta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "xp"), PersistentDataType.DOUBLE);
        HardnessLevel hardnessLevel = HardnessLevel.valueOf(meta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "hardnessLevel"), PersistentDataType.STRING));
        Integer blockDamage = meta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "blockDamage"), PersistentDataType.INTEGER);



        return new Pickaxe(
                modelData,
                displayName,
                itemStack.getType(),
                hardnessLevel,
                level,
                xp,
                blockDamage
        );
    }
}
