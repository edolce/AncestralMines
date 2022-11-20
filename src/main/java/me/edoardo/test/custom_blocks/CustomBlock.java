package me.edoardo.test.custom_blocks;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomBlock {
    //Mine Blocks
    HARD_STONE1( 1, Instrument.BANJO, new Note(0, Note.Tone.A,false), "HARD_STONE1", null),
    HARD_STONE2( 2, Instrument.BANJO, new Note(0, Note.Tone.B,false), "HARD_STONE2", HARD_STONE1),
    HARD_STONE3( 3, Instrument.BANJO, new Note(0, Note.Tone.C,false), "HARD_STONE3", HARD_STONE2),
    NORMAL_STONE1(4, Instrument.BANJO, new Note(0, Note.Tone.D,false), "NORMAL_STONE1", null),
    NORMAL_STONE2( 5, Instrument.BANJO, new Note(0, Note.Tone.E,false), "NORMAL_STONE2", NORMAL_STONE1),
    NORMAL_STONE3( 6, Instrument.BANJO, new Note(0, Note.Tone.F,false), "NORMAL_STONE3", NORMAL_STONE2),
    SOFT_STONE1( 7, Instrument.BANJO, new Note(0, Note.Tone.G,false), "SOFT_STONE1", null),
    SOFT_STONE2( 8, Instrument.BANJO, new Note(1, Note.Tone.A,false), "SOFT_STONE2", SOFT_STONE1),
    SOFT_STONE3( 9, Instrument.BANJO, new Note(1, Note.Tone.B,false), "SOFT_STONE3", SOFT_STONE2),
    //Minerals
    IRON_MINERAL_ORE(10,Instrument.BANJO, new Note(1, Note.Tone.C,false), "IRON_MINERAL_ORE", null),
    GOLD_MINERAL_ORE(11,Instrument.BANJO, new Note(1, Note.Tone.D,false), "GOLD_MINERAL_ORE", null),
    DIAMOND_MINERAL_ORE(12,Instrument.BANJO, new Note(1, Note.Tone.E,false), "DIAMOND_MINERAL_ORE", null);

    final private int modelData;
    final private Instrument instrument;
    final private Note note;
    final private String title;

    final private CustomBlock nextBlock;

    CustomBlock( int modelData, Instrument instrument, Note note, String title, CustomBlock nextBlock){
        this.modelData = modelData;
        this.instrument = instrument;
        this.note = note;
        this.title = title;
        this.nextBlock = nextBlock;
    }




    public ItemStack getItem() {
        ItemStack itemStack= new ItemStack(Material.NOTE_BLOCK);
        ItemMeta meta = itemStack.getItemMeta();

        assert meta != null;
        meta.setCustomModelData(modelData);
        meta.setDisplayName(title);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Note getNote() {
        return note;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public CustomBlock getNextBlock() {
        return nextBlock;
    }

    public void placeBlock(Block block){
        CustomBlock newBlock=this;
        block.setType(Material.NOTE_BLOCK);
        BlockState blockstate = block.getState();
        NoteBlock noteblock = (NoteBlock) blockstate.getBlockData();
        noteblock.setNote(newBlock.getNote());
        noteblock.setInstrument(newBlock.getInstrument());
        block.setBlockData(noteblock);
    }

    public static CustomBlock getCustomBlockFromBlock(Block block){
        for(CustomBlock customBlock:CustomBlock.values()){
            NoteBlock blockData = (NoteBlock) block.getBlockData();
            Instrument blockBreakInstrument = blockData.getInstrument();
            Note.Tone blockBreakTone = blockData.getNote().getTone();
            int blockBreakOctave = blockData.getNote().getOctave();
            if(blockBreakOctave==customBlock.getNote().getOctave()
               & blockBreakTone==customBlock.getNote().getTone()
               & blockBreakInstrument==customBlock.getInstrument())
                return customBlock;
        }
        return null;
    }
}
