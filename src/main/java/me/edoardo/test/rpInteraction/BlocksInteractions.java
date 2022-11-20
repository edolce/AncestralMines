package me.edoardo.test.rpInteraction;

import me.edoardo.test.base.Base;
import me.edoardo.test.base.BaseMenu;
import me.edoardo.test.presets.Bag;
import me.edoardo.test.custom_blocks.CustomBlock;
import me.edoardo.test.presets.BagType;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;

public class BlocksInteractions implements Listener {


    @EventHandler
    public void onBlockInteraction(PlayerInteractEvent event) {
        if (event.getClickedBlock()==null) return;
        //Piccone
        if(event.getClickedBlock().getType().equals(Material.CRACKED_NETHER_BRICKS)) {
//            Pickaxe pickaxe1 = new Pickaxe(2,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE, hardnessLevel, 1,20,"#6b0000",
//                    new HashMap<Integer,Double>() {{
//                        put(1, 100.0);
//                        put(2, 250.0);
//                        put(3, 500.0);
//                        put(4, 800.0);
//                        put(5, 1000.0);
//                    }});
//            event.getPlayer().getInventory().addItem(pickaxe1.getItemStack());
        }
        //Bags
        if(Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.DRIED_KELP_BLOCK)) {
            int freeSlot=event.getPlayer().getInventory().firstEmpty();
//            event.getPlayer().getInventory().setItem(freeSlot, Bag.WOODBAG.getBlankBagItem());
//            event.getPlayer().getInventory().setItem(freeSlot+1, Bag.DIRTBAG.getBlankBagItem());
            event.getPlayer().getInventory().setItem(freeSlot, new Bag(BagType.STONEBAG,0,100).getItemStack());
        }
        //BASE CREATION AND TP
        if(Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.PRISMARINE)) {
            Base playerBase = new Base(event.getPlayer());
            event.getPlayer().teleport(playerBase.getSpawn());
        }
        //BASE MENU
        if(Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.RED_NETHER_BRICK_WALL)) {
            BaseMenu baseMenu=new BaseMenu(event.getPlayer());
            event.getPlayer().openInventory(baseMenu.getGui());
        }
    }
}
