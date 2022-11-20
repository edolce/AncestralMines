package me.edoardo.test.miniere;

import me.edoardo.test.custom_blocks.CustomBlock;
import me.edoardo.test.job.blacksmith.presets.MineralItem;
import me.edoardo.test.job.blacksmith.presets.MineralType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public enum Mineral{

    IRON(CustomBlock.IRON_MINERAL_ORE, MineralType.IRON),
    GOLD(CustomBlock.GOLD_MINERAL_ORE, MineralType.GOLD),
    DIAMOND(CustomBlock.DIAMOND_MINERAL_ORE, MineralType.DIAMOND);

    private final CustomBlock customBlock;
    private final MineralType mineralType;

    Mineral(CustomBlock customBlock, MineralType mineralType){

        this.customBlock = customBlock;
        this.mineralType = mineralType;
    }


    public MineralType getMineralType() {
        return mineralType;
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }
}
