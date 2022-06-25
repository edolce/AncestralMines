package me.edoardo.test.eventListners;

import me.edoardo.test.presets.CustomBlock;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakingResourceEvent implements Listener {

    @EventHandler
    public void onCustomResourceBreak(BlockBreakEvent event){
        if (event.getBlock().getType()!=Material.NOTE_BLOCK) return;
        event.getPlayer().sendMessage("Strumento: "+((NoteBlock)event.getBlock().getBlockData()).getInstrument().name());
        event.getPlayer().sendMessage("Nota: "+ ((NoteBlock)event.getBlock().getBlockData()).getNote());
        CustomBlock matchedBlock= getMatchedCustomBlock(event.getBlock());
        event.getPlayer().sendMessage("Match: "+ matchedBlock);
        assert matchedBlock != null;
        if (matchedBlock.getNextBlock()==null){
            return;
        }
        event.setCancelled(true);
        event.getBlock().setBlockData(prepareCustomBlock(event.getBlock(),matchedBlock.getNextBlock()));
        event.getPlayer().sendMessage("Stai cercando di rompere: "+matchedBlock.name());
    }


    private CustomBlock getMatchedCustomBlock(Block block){
        for (CustomBlock cBlock:CustomBlock.values()){
            NoteBlock blockData = (NoteBlock) block.getBlockData();
            Instrument blockBreakInstrument = blockData.getInstrument();
            Note.Tone blockBreakTone = blockData.getNote().getTone();
            int blockBreakOctave = blockData.getNote().getOctave();
            if(blockBreakOctave==cBlock.getNote().getOctave() & blockBreakTone==cBlock.getNote().getTone() & blockBreakInstrument==cBlock.getInstrument()) return cBlock;

        }
        return null;
    }

    private NoteBlock prepareCustomBlock(Block block,CustomBlock cBlock){

        BlockState blockstate = block.getState();
        NoteBlock noteblock = (NoteBlock) blockstate.getBlockData();
        noteblock.setNote(cBlock.getNote());
        noteblock.setInstrument(cBlock.getInstrument());
        block.setBlockData(noteblock);

        return noteblock;
    }
}
