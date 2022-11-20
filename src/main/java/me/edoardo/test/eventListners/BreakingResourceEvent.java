//package me.edoardo.test.eventListners;
//
//import me.edoardo.test.presets.Bag;
//import me.edoardo.test.custom_blocks.CustomBlock;
//import me.edoardo.test.presets.NewDrop;
//import org.bukkit.Instrument;
//import org.bukkit.Material;
//import org.bukkit.Note;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.block.data.type.NoteBlock;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.Objects;
//
//public class BreakingResourceEvent implements Listener {
//
//    @EventHandler
//    public void onCustomBlockBreak(BlockBreakEvent event){
//        if (event.getBlock().getType()!=Material.NOTE_BLOCK) return;
//        event.getPlayer().sendMessage("Strumento: "+((NoteBlock)event.getBlock().getBlockData()).getInstrument().name());
//        event.getPlayer().sendMessage("Nota: "+ ((NoteBlock)event.getBlock().getBlockData()).getNote());
//        CustomBlock matchedBlock= getMatchedCustomBlock(event.getBlock());
//        event.getPlayer().sendMessage("Match: "+ matchedBlock);
//
//        //Control if you have proper bag for the block
//        Player player=event.getPlayer();
//        event.setDropItems(false);
//
//
//        NewDrop type = isNewDropBlock(matchedBlock);
//
//
//        ItemStack bag = isBreakable(matchedBlock,player,type);
//        if(bag!=null){
//
//            ItemStack bagToCopyMeta = Bag.addResources(bag,type.getMaterials().get(matchedBlock));
//            bag.setItemMeta(bagToCopyMeta.getItemMeta());
//
//        }
//
//        assert matchedBlock != null;
//        if (matchedBlock.getNextBlock()==null){
//            return;
//        }
//        event.setCancelled(true);
//        event.getBlock().setBlockData(prepareCustomBlock(event.getBlock(),matchedBlock.getNextBlock()));
//        event.getPlayer().sendMessage("Stai cercando di rompere: "+matchedBlock.name());
//    }
//
//    private ItemStack isBreakable(CustomBlock customBlock,Player player, NewDrop type){
//        ItemStack handItem= player.getInventory().getItemInMainHand();
//        if(type!=null){
//            return isRightBagPresent(player.getInventory().getContents(), type);
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
//
//
//    private NewDrop isNewDropBlock(CustomBlock customBlock){
//        for(NewDrop newDrop:NewDrop.values()){
//            for(CustomBlock validCBlock:newDrop.getMaterials().keySet()){
//                if (validCBlock==customBlock) return newDrop;
//            }
//        }
//        return null;
//    }
//
//
//    private CustomBlock getMatchedCustomBlock(Block block){
//        for (CustomBlock cBlock:CustomBlock.values()){
//            NoteBlock blockData = (NoteBlock) block.getBlockData();
//            Instrument blockBreakInstrument = blockData.getInstrument();
//            Note.Tone blockBreakTone = blockData.getNote().getTone();
//            int blockBreakOctave = blockData.getNote().getOctave();
//            if(blockBreakOctave==cBlock.getNote().getOctave() & blockBreakTone==cBlock.getNote().getTone() & blockBreakInstrument==cBlock.getInstrument()) return cBlock;
//
//        }
//        return null;
//    }
//
//    private NoteBlock prepareCustomBlock(Block block,CustomBlock cBlock){
//
//        BlockState blockstate = block.getState();
//        NoteBlock noteblock = (NoteBlock) blockstate.getBlockData();
//        noteblock.setNote(cBlock.getNote());
//        noteblock.setInstrument(cBlock.getInstrument());
//        block.setBlockData(noteblock);
//
//        return noteblock;
//    }
//}
