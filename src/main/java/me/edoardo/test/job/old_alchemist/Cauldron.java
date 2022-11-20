package me.edoardo.test.job.old_alchemist;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.job.old_alchemist.presets.EssenceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cauldron {

    private final HashMap<EssenceType,Integer> essenceInsideCalderon;
    private final Player player;

    public Cauldron(Player player, HashMap<EssenceType, Integer> essencesInCauldron) {
        this.essenceInsideCalderon = essencesInCauldron;
        this.player = player;
    }



    public ItemStack getItemStack(){
        ItemStack cauldron = new ItemStack(Material.CAULDRON);

        ItemMeta itemMeta =cauldron.getItemMeta();
        itemMeta.setLore(getLore());
        itemMeta.setDisplayName("DROPPA LA TUA ESSENZA QUA");
        cauldron.setItemMeta(itemMeta);

        return cauldron;
    }


    private List<String> getLore(){

        List<String> lore= new ArrayList<>();

        lore.add("ESSENZE: ");

        for(EssenceType essence:essenceInsideCalderon.keySet()){

            lore.add(ColorUtils.translateColorCodes(String.format("%s:%s",essence,essenceInsideCalderon.get(essence))));

        }

        return lore;

    }


    //BASIC METHODS
    public HashMap<EssenceType, Integer> getEssenceInsideCalderon() {
        return essenceInsideCalderon;
    }

    public Player getPlayer() {
        return player;
    }
}
