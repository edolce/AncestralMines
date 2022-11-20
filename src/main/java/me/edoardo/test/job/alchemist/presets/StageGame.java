package me.edoardo.test.job.alchemist.presets;

import me.edoardo.test.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StageGame {
    private final int totalSpaces;
    private final int occupiedSpaces;
    private final int freeSpaces;
    private final int defaultTimeMinutes;

    private final ItemStack deactivated = Util.createGuiItem(Material.GRAY_STAINED_GLASS_PANE,"Clicca Per Attivare");
    private final ItemStack activated = Util.createGuiItem(Material.GREEN_TERRACOTTA,"Clicca Per Disattivare");
    private final ItemStack none = Util.createGuiItem(Material.BARRIER,"[/]");

    private final List<Integer> gridFillOrder = Arrays.asList(
            21,22,23,30,31,32,20,29,24,33,12,13,14,39,40,41,11,15,38,42
    );

    public StageGame(int totalSpaces, int occupiedSpaces, int defaultTimeMinutes) {
        this.totalSpaces = totalSpaces;
        this.occupiedSpaces = occupiedSpaces;
        this.defaultTimeMinutes = defaultTimeMinutes;
        this.freeSpaces = totalSpaces-occupiedSpaces;
    }

    public HashMap<Integer, ItemStack> getHashMapGridForGUI(){
        HashMap<Integer,ItemStack> gridForGUI = new HashMap<>();
        int i=0;
        for(int guiPos:gridFillOrder){
            if(i<totalSpaces) gridForGUI.put(guiPos,deactivated);
            else gridForGUI.put(guiPos,none);
            i++;
        }
        return gridForGUI;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public int getDefaultTimeMinutes() {
        return defaultTimeMinutes;
    }

    public long getDefaultTimeMilliSeconds() {
        return (long) defaultTimeMinutes *60*1000;
    }


}
