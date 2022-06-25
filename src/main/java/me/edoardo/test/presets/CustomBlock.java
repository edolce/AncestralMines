package me.edoardo.test.presets;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomBlock {
    HARD_STONE1(BreachLevel.AS_NEW, 1, Instrument.BANJO, new Note(0, Note.Tone.A,false), "HARD_STONE1", null),
    HARD_STONE2(BreachLevel.MID_LEVEL, 2, Instrument.BANJO, new Note(0, Note.Tone.B,false), "HARD_STONE2", HARD_STONE1),
    HARD_STONE3(BreachLevel.CLOSE_TO_BREAK, 3, Instrument.BANJO, new Note(0, Note.Tone.C,false), "HARD_STONE3", HARD_STONE2),
    NORMAL_STONE1(BreachLevel.AS_NEW, 4, Instrument.BANJO, new Note(0, Note.Tone.D,false), "NORMAL_STONE1", null),
    NORMAL_STONE2(BreachLevel.MID_LEVEL, 5, Instrument.BANJO, new Note(0, Note.Tone.E,false), "NORMAL_STONE2", NORMAL_STONE1),
    NORMAL_STONE3(BreachLevel.CLOSE_TO_BREAK, 6, Instrument.BANJO, new Note(0, Note.Tone.F,false), "NORMAL_STONE3", NORMAL_STONE2),
    SOFT_STONE1(BreachLevel.AS_NEW, 7, Instrument.BANJO, new Note(0, Note.Tone.G,false), "SOFT_STONE1", null),
    SOFT_STONE2(BreachLevel.MID_LEVEL, 8, Instrument.BANJO, new Note(1, Note.Tone.A,false), "SOFT_STONE2", SOFT_STONE1),
    SOFT_STONE3(BreachLevel.CLOSE_TO_BREAK, 9, Instrument.BANJO, new Note(1, Note.Tone.B,false), "SOFT_STONE3", SOFT_STONE2);

    final private BreachLevel breachLevel;
    final private int modelData;
    final private Instrument instrument;
    final private Note note;
    final private String title;

    final private CustomBlock nextBlock;

    CustomBlock(BreachLevel breachLevel, int modelData, Instrument instrument, Note note, String title, CustomBlock nextBlock){
        this.breachLevel=breachLevel;
        this.modelData = modelData;
        this.instrument = instrument;
        this.note = note;
        this.title = title;
        this.nextBlock = nextBlock;
    }


    public BreachLevel getBreachLevel() {
        return breachLevel;
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
}
