package me.edoardo.test.job.old_alchemist.presets;

import org.bukkit.inventory.ItemStack;

public enum Rarity {
    COMMON("&f* &#3d3d3d[&fComune&#3d3d3d] &f*"),
    UNCOMMON("&f* &#3d3d3d[&fComune&#3d3d3d] &f*"),
    RARE("&9.* &#3d3d3d[&9Raro&#3d3d3d] &9*."),
    SUPER_RARE("&1* &#3d3d3d[&1Super Raro&#3d3d3d] &1*"),
    EPIC("&5* &#3d3d3d[&5Epico&#3d3d3d] &5*"),
    LEGGENDARY("&6* &#3d3d3d[&6Leggendario&#3d3d3d] &6*"),
    MYTHIC("&f* &#3d3d3d[&fMythic&#3d3d3d] &f*"),
    GODLY("&f* &#3d3d3d[&fGodly&#3d3d3d] &f*"),;

    final private String name;

    Rarity(String name){

        this.name = name;
    }
    public static Rarity getRarity(ItemStack cursorItem) {
        for(String line:cursorItem.getItemMeta().getLore()){
            for(Rarity rarity:Rarity.values()){
                if(line.contains(rarity.toString())) return rarity;
            }
        }
        return COMMON;
    }

    public String getName() {
        return name;
    }
}
