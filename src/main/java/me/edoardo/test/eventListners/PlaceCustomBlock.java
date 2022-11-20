package me.edoardo.test.eventListners;

import me.edoardo.test.custom_blocks.CustomBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceCustomBlock implements Listener {

    @EventHandler
    public void onCustomBlockPlaced(BlockPlaceEvent event){
        //Controllo che il blocco sia un noteblock
        if(event.getBlockPlaced().getType() != Material.NOTE_BLOCK) return;

        event.getPlayer().sendMessage("You are trying to place ");

        CustomBlock newBlock=CustomBlock.valueOf(event.getItemInHand().getItemMeta().getDisplayName());

        event.getPlayer().sendMessage("You are trying to place: "+newBlock.name());


        BlockState blockstate = event.getBlockPlaced().getState();
        NoteBlock noteblock = (NoteBlock) blockstate.getBlockData();
        noteblock.setNote(newBlock.getNote());
        noteblock.setInstrument(newBlock.getInstrument());
        event.getBlockPlaced().setBlockData(noteblock);

        event.getPlayer().sendMessage("Block Placed");

    }

}
