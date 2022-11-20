package me.edoardo.test.job.old_alchemist;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.old_alchemist.presets.BuffType;
import me.edoardo.test.job.old_alchemist.presets.EssenceType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Recipe {
    //LA RECIPE RIMANE SEGRETA FINCHE NON VIENE DISCOVERATA


    private HashMap<BuffType,Double> buffsInsideRecipe = new HashMap<>();

    Recipe(){

    }


    public static ItemStack getFinalItemStack(Cauldron cauldron){
        ItemStack recipe = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = recipe.getItemMeta();
        itemMeta.setDisplayName("Recipe");

        List<String> lore=new ArrayList<>();

        lore.add("");
        lore.add("BUFF:");


        HashMap<EssenceType,BuffType> linkedBuff=Mysql.getEssenceTypeLinkedBuff(cauldron.getPlayer());

        for (EssenceType essence:cauldron.getEssenceInsideCalderon().keySet()){
            BuffType buff = linkedBuff.get(essence);
            double increment = buff.getIncrementPerEssence();
            double rate = cauldron.getEssenceInsideCalderon().get(essence);

            lore.add(ColorUtils.translateColorCodes(String.format(
                    "%s: %s",buff,increment*rate
            )));

        }

        itemMeta.setLore(lore);

        recipe.setItemMeta(itemMeta);
        return recipe;
    }

    public static ItemStack getUndiscoveredItemStackDisplay(){
        ItemStack recipe = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = recipe.getItemMeta();
        itemMeta.setDisplayName("Schiaccia per discoverare la recipe");
        itemMeta.setLore(Arrays.asList(
                ColorUtils.translateColorCodes(String.format("")),
                ColorUtils.translateColorCodes(String.format(""))
        ));

        recipe.setItemMeta(itemMeta);

        return recipe;
    }


    public static ItemStack getDiscoveredItemStackDisplay(Cauldron cauldron){
        ItemStack recipe = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = recipe.getItemMeta();
        itemMeta.setDisplayName("Preview Of Recipe");

        List<String> lore=new ArrayList<>();

        lore.add("");
        lore.add("BUFF:");


        HashMap<EssenceType,BuffType> linkedBuff=Mysql.getEssenceTypeLinkedBuff(cauldron.getPlayer());

        for (EssenceType essence:cauldron.getEssenceInsideCalderon().keySet()){
            BuffType buff = linkedBuff.get(essence);
            double increment = buff.getIncrementPerEssence();
            double rate = cauldron.getEssenceInsideCalderon().get(essence);

            lore.add(ColorUtils.translateColorCodes(String.format(
                    "%s: %s",buff,increment*rate
            )));

        }

        itemMeta.setLore(lore);

        recipe.setItemMeta(itemMeta);
        return recipe;
    }

}
