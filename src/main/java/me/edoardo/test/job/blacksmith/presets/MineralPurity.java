package me.edoardo.test.job.blacksmith.presets;

import org.bukkit.inventory.ItemStack;

public enum MineralPurity {
    STAR5("&f★★★★★",120,101),
    STAR4("&f★★★★&#3d3d3d☆",100,81),
    STAR3("&f★★★&#3d3d3d☆☆",80,61),
    STAR2("&f★★&#3d3d3d☆☆☆",60,41),
    STAR1("&f★&#3d3d3d☆☆☆☆",40,21),
    STAR0("&#3d3d3d☆☆☆☆☆",20,0);

    final String display;
    final int rangeMin;
    final int rangeMax;

    MineralPurity(String display, int rangeMax, int rangeMin){
        this.display = display;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public static MineralPurity getMineralPurity(ItemStack cursorItem) {
            for(String line:cursorItem.getItemMeta().getLore()){
                for(MineralPurity mineralPurity:MineralPurity.values()){
                    if(line.contains(mineralPurity.display)) return mineralPurity;
                }
            }
            return STAR0;
    }
}
