package me.edoardo.test.job.blacksmith.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.blacksmith.Blacksmith;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlacksmithFurnace {

    private final int furnaceId;
    private boolean isSmelting;
    private Timestamp startTime;
    private int duration;
    private int temperature;

    private MineralItem mineralItem;


    public BlacksmithFurnace(int furnaceId) {
        this.furnaceId = furnaceId;
        BlacksmithFurnace furnaceToCopy= Mysql.getFurnace(furnaceId);
        this.isSmelting=furnaceToCopy.isSmelting;
        this.startTime=furnaceToCopy.startTime;
        this.duration=furnaceToCopy.duration;
        this.temperature=furnaceToCopy.temperature;
        this.mineralItem = furnaceToCopy.mineralItem;
    }


    public BlacksmithFurnace(int furnaceId, boolean isSmelting, Timestamp start_time, int duration, int temperature, MineralItem mineralItem) {
        this.furnaceId=furnaceId;
        this.isSmelting=isSmelting;
        this.startTime=start_time;
        this.duration=duration;
        this.temperature=temperature;
        this.mineralItem = mineralItem;
    }

    public void update(){
        Mysql.getFurnace(furnaceId);
    }


    //TODO: ADD UPDATE EVERY SECOND TIMER

    public ItemStack getItemStackDisplay() {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(String.format("Fornace #%s",furnaceId));

        //Grafica se sta smeltando
        if(isSmelting){

            List<String> lore = new ArrayList<>(Arrays.asList(
                    ColorUtils.translateColorCodes(String.format("Sta Smeltando: Si")),
                    ColorUtils.translateColorCodes(String.format("Tempo Scelto: %s",timeToString((duration + (30 * 60))))),
                    ColorUtils.translateColorCodes(String.format("Temperatura: %s gradi",temperature))
            ));


            if(getRemainingSeconds()>=0)lore.add(ColorUtils.translateColorCodes(String.format("Tempo Rimanente: %s",timeToString(getRemainingSeconds()))));
            else lore.add(ColorUtils.translateColorCodes("Il minerale é pronto per essere prelevato"));

            meta.setLore(lore);
        }else{
            meta.setLore(Arrays.asList(
                    ColorUtils.translateColorCodes(String.format("Sta Smeltando: No"))
            ));
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    private String timeToString(int timeInSecond){

        int minuti = timeInSecond/60;

        int secondi = timeInSecond % 60;

        System.out.println(minuti);
        System.out.println(secondi);

        return String.format("%s minuti e %s secondi",minuti,secondi);

    }

    public ItemStack getSmeltedItem(Player player) {
        if(!isSmeltingOver()){
            return new ItemStack(Material.AIR);
        }
        //Create Smelted Item


        //Calcola Quanto è buono il risultato (Qualità)
        int[] durTemp = Mysql.getMaxQuality(player,mineralItem.getMineralType());


        double durationAccuracy = 100-Math.abs(durTemp[0] - duration);
        double temperatureAccuracy = 100-Math.abs(durTemp[1] - temperature);

        double finalAccuracy = Math.round((durationAccuracy+temperatureAccuracy)/2);

        return mineralItem.getSmeltedItem(finalAccuracy);
    }

    //CONTROLLO SE LO SMELTINF É FINITO
    public boolean isSmeltingOver(){
        return startTime.getTime() + (duration + (30 * 60 * 1000)) < Timestamp.from(Instant.now()).getTime();
    }
    

    public int getRemainingSeconds(){
        long milliNow = Timestamp.from(Instant.now()).getTime();
        long milliPassed = milliNow-startTime.getTime();
        long milliRemaining = (duration + (30 * 60 * 1000)) - milliPassed;

        return (int) (milliRemaining/1000);
    }


    // GET/SET METHODS

    public MineralItem getMineralItem() {
        return mineralItem;
    }

    public boolean isSmelting() {
        return isSmelting;
    }
    public String getStringTitle(){
        return String.format("Fornace #%s",furnaceId);
    }

    public int getDuration() {
        return duration;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getId() {
        return furnaceId;
    }

    public String getItemSmeltingJSON() {
        return mineralItem.getJSON();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void startSmelting(MineralItem mineralItem, int duration, int temperature) {
        isSmelting=true;
        this.mineralItem=mineralItem;
        startTime = Timestamp.from(Instant.now());
        this.duration = duration;
        this.temperature = temperature;
    }
}
