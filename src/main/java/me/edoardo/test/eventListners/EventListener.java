package me.edoardo.test.eventListners;

import me.edoardo.test.presets.Bag;
import me.edoardo.test.presets.NewDrop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EventListener implements Listener {

//    @EventHandler
//    public void blockBreakEvent(BlockBreakEvent event){
//
//        Block block=event.getBlock();
//        Player player=event.getPlayer();
//        event.setDropItems(false);
//        ItemStack bag = isBreakable(block,player);
//
//
//
//        if(bag==null){
//            return;
//        }
//
//
//        ItemMeta meta=bag.getItemMeta();
//        assert meta != null;
//        List<String> lore= meta.getLore();
//        for(int i=0;i<lore.size();i++){
//            if (lore.get(i).contains(": ")){
//                String[] data = lore.get(i).split(": ");
//                lore.set(i,data[0]+": "+(Integer.parseInt(data[1])+1));
//            }
//        }
//        meta.setLore(lore);
//        bag.setItemMeta(meta);
//
//
//    }
//
//    private ItemStack isBreakable(Block block,Player player){
//
//        ItemStack handItem= player.getInventory().getItemInMainHand();
//
//        NewDrop type = isNewDropBlock(block);
//
//        if(type!=null){
//            return isRightBagPresent(player.getInventory().getContents(), type);
//        }
//        return null;
//    }
//
//    //Controlla che l'oggetto rientri tra gli oggetti che droppano qualcosa
//    private NewDrop isNewDropBlock(Block block){
//        for(NewDrop newDrop:NewDrop.values()){
//            for(Material validMaterial:newDrop.getMaterials().keySet()){
//                if (validMaterial==block.getType()) return newDrop;
//            }
//        }
//        return null;
//    }
//
//    //Controlla se hai la bag giusta per raccogliere l'oggetto
//    private ItemStack isRightBagPresent(ItemStack[] inventory,NewDrop type){
//        for(ItemStack itemStack:inventory){
//            if (itemStack==null) continue;
//            int customModel = Objects.requireNonNull(itemStack.getItemMeta()).getCustomModelData();
//            for(Bag bag:Bag.values()){
//                if(bag.isThisBag(customModel) & bag.getContainerType()==type){
//                    return itemStack;
//                }
//            }
//        }
//        return null;
//    }


    @EventHandler
    public void onBlockPhysics(PlayerInteractEvent event) {
        if (event.getClickedBlock()==null) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) & Objects.requireNonNull(event.getClickedBlock()).getType()==Material.NOTE_BLOCK){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onNotePlayed(NotePlayEvent event){
        event.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block aboveBlock = event.getBlock().getLocation().add(0, 1, 0).getBlock();
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation());
            event.setCancelled(true);
            // aboveBlock.getState().update(true, true);
        }
        if (event.getBlock().getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);
        if (event.getBlock().getType().toString().toLowerCase().contains("sign"))
            return;
        event.getBlock().getState().update(true, false);

    }

    public void updateAndCheck(Location loc) {
        Block b = loc.add(0, 1, 0).getBlock();
        if (b.getType() == Material.NOTE_BLOCK)
            b.getState().update(true, true);
        Location nextBlock = b.getLocation().add(0, 1, 0);
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK)
            updateAndCheck(b.getLocation());
    }

}
