package me.edoardo.test.job.alchemist;

import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.alchemist.presets.Ingredient;
import me.edoardo.test.job.alchemist.presets.Stage;
import me.edoardo.test.job.alchemist.presets.StageGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Research {

    private final Player player;
    private final Ingredient ingredient;
    private final Stage stage;
    private final List<Integer> combination;
    private boolean finished;
    private final long startTimestamp;
    //NULL VALUE IF NOT FINISHED
    private long finishTimestamp;
    private Boolean isSuccessful;


    public Research(Player player, Ingredient ingredient, Stage stage, List<Integer> combination, long startTimestamp, long finishTimestamp, Boolean isSuccessful) {
        this.player = player;
        this.ingredient = ingredient;
        this.stage = stage;
        this.combination = combination;
        this.finished = finishTimestamp != 0;
        this.startTimestamp = startTimestamp;
        this.finishTimestamp = finishTimestamp;
        this.isSuccessful = isSuccessful;
    }


    public void pushMysql() {
        Mysql.pushResearch(this);
    }

    public boolean isCombinationValid() {
        int activations = 0;

        for (int pointer : combination) {
            if (pointer == 1) activations++;
        }

        return activations == stage.getStageGame().getOccupiedSpaces();
    }

    public boolean isDuplicated() {

        List<Research> researches = Mysql.getResearchesOfIngredient(player, ingredient);
        for (Research res : researches) {
            if (res.getCombination() == combination) return true;
        }
        return false;
    }

    public List<Integer> getCombination() {
        return combination;
    }

    public Player getPlayer() {
        return player;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public long getFinishTimestamp() {
        return finishTimestamp;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public Stage getStage() {
        return stage;
    }

    public String getCombinationToString() {
        StringBuilder string = new StringBuilder();
        for (int i : combination) {
            string.append(i);
        }
        return string.toString();
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }

    public boolean isFinished() {
        return finished;
    }

    public Inventory getProcessGui() {
        Inventory gui = Bukkit.createInventory(null, 54,"ricerca in processo");

        List<Integer> gridFillOrder = Arrays.asList(21,22,23,30,31,32,20,29,24,33,12,13,14,39,40,41,11,15,38,42);
        ItemStack backgroundItem = Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ");
        ItemStack goBackItem = Util.createGuiItem(Material.RED_STAINED_GLASS_PANE, "Torna Indietro");
        ItemStack displayInfoItem = Util.createGuiItem(ingredient.getAncestralDrop().getItemStack().getType(), "Info");
        ItemStack startResearchItem = Util.createGuiItem(Material.BARRIER, "Research In Progress");
        ItemStack deactivated = Util.createGuiItem(Material.GRAY_STAINED_GLASS_PANE,"Clicca Per Attivare");
        ItemStack activated = Util.createGuiItem(Material.GREEN_TERRACOTTA,"Clicca Per Disattivare");

        enchant(activated);


        //Controllare se la recipe é pronta, se é pronta cambiare "startResearchItem"
        if(this.isReady()) startResearchItem = Util.createGuiItem(Material.PAPER, "Research Ready To Collect");


        for (int i = 0; i < 54; i++) gui.setItem(i, backgroundItem);

        //GO BACK
        gui.setItem(0, goBackItem);
        //DISPLAY INGREDIENT WITH INFO
        gui.setItem(4, displayInfoItem);
        //START RESEARCH BUTTON
        gui.setItem(8, startResearchItem);
        //AREA OF RESEARCH
        for (int i=0;i<combination.size();i++) {
            ItemStack toSet = combination.get(i)==0 ? deactivated : activated;
            gui.setItem(gridFillOrder.get(i),toSet);
        }

        return gui;
    }

    private boolean isReady() {
        return System.currentTimeMillis()>=calculateFinishTime();
    }

    private long calculateFinishTime() {
        return startTimestamp+stage.getStageGame().getDefaultTimeMilliSeconds();
    }


    private void enchant(ItemStack item){
        item.addUnsafeEnchantment(Enchantment.DURABILITY,1);;
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    public void finish() {
        finishTimestamp=calculateFinishTime();
        finished=true;
        isSuccessful=true;
        Mysql.updateResearch(this);
    }
}
