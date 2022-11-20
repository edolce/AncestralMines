package me.edoardo.test.job.blacksmith;

import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.old_alchemist.presets.Rarity;
import me.edoardo.test.job.blacksmith.presets.BlacksmithFurnace;
import me.edoardo.test.job.blacksmith.presets.MineralItem;
import me.edoardo.test.job.blacksmith.presets.MineralPurity;
import me.edoardo.test.job.blacksmith.presets.MineralType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PendingFurnacePlayerDecision implements Listener {

    private int minDurVal=0;
    private int maxDurVal=100;
    private int minTempVal=0;
    private int maxTempVal=100;

    @EventHandler
    private void addMineralItemToFurnace(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Fornace #")) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) event.setCancelled(true);
        if (clickedItem==null) return;
        if (cursorItem==null || cursorItem.getType()== Material.AIR) return;
        if (clickedItem.getType()==Material.GREEN_CONCRETE) addMineralToFurnaceInteraction(cursorItem, (Player) event.getWhoClicked(),event.getClickedInventory(), Integer.parseInt(event.getView().getTitle().split("#")[1]));

    }
    private void addMineralToFurnaceInteraction(ItemStack cursorItem, Player player, Inventory inventory, int furnaceId) {
        MineralType mineralType = MineralItem.getMineralTypeFromGeneralItem(cursorItem);

        //Controllo se cursorItem é un minerale
        if(mineralType==null) return;

        //Posso Aggiungerlo
        //Modifica Fornace
        BlacksmithFurnace furnace = Mysql.getFurnace(furnaceId);


        //ADD TO ALL PENDING HASMAP
        Test.getInstance().getPendingFurnace().put(player,furnace);
        Test.getInstance().getPendingItemStack().put(player, cursorItem);
        Test.getInstance().getPendingTempAndDuration().put(player,new Integer[]{0,0});

        //Get general mineral
        MineralItem mineralItem = mineralType.getMineralItem_0Rarity_0Purity();

        //Get and Apply Rarity and MineralPurity
        Rarity rarity = Rarity.getRarity(cursorItem);
        MineralPurity purity = MineralPurity.getMineralPurity(cursorItem);

        mineralItem.setRarity(rarity);
        mineralItem.setPurity(purity);

        //OpenGuiToSelect Furnace and
        Inventory gui= Bukkit.createInventory(player,45,"Scegli calore e durata");
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i, Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));
        }
        gui.setItem(4,furnace.getItemStackDisplay());
        //ADD TIME
        gui.setItem(10,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+10 Secondi","Aggiungi 10 secondi"));
        gui.setItem(11,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+30 Secondi","Aggiungi 30 secondi"));
        gui.setItem(12,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+1 Minuto","Aggiungi 1 minuto"));
        //REMOVE TIME
        gui.setItem(28,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-10 Secondi","Rimuovi 10 secondi"));
        gui.setItem(29,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-30 Secondi","Rimuovi 1 minuto"));
        gui.setItem(30,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-1 Minuto","Rimuovi 5 minuti"));

        //ADD HEAT
        gui.setItem(14,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+1 Grado","Aggiungi 1 grado"));
        gui.setItem(15,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+5 Gradi","Aggiungi 5 gradi"));
        gui.setItem(16,Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,"+10 Gradi","Aggiungi 10 gradi"));
        //REMOVE HEAT
        gui.setItem(32,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-1 Grado","Rimuovi 1 grado"));
        gui.setItem(33,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-5 Gradi","Rimuovi 5 gradi"));
        gui.setItem(34,Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"-10 Gradi","Rimuovi 10 gradi"));

        // Close/Confirm Buttons
        gui.setItem(18,Util.createGuiItem(Material.RED_SHULKER_BOX,"Close","Annulla l'inserimento del minerale"));
        gui.setItem(26,Util.createGuiItem(Material.GREEN_SHULKER_BOX,"Conferma","Conferma la tua scelta di durata e temperatura"));

        // Duration/Temperature Indicator
        gui.setItem(20,Util.createGuiItem(Material.CLOCK,"Durata","Durata: 30 minuti"));
        gui.setItem(24,Util.createGuiItem(Material.CAMPFIRE,"Temperatura","Calore: 30 gradi"));

        player.openInventory(gui);

        return;



        //Update database
        //Mysql.updateFurnace(furnace);
    }



    @EventHandler
    public void onPlayerLeftInventory(InventoryCloseEvent event){
        if(!event.getView().getTitle().contains("Scegli calore e durata")) return;

        Player player = (Player) event.getPlayer();

        Test.getInstance().getPendingItemStack().remove(player);
        Test.getInstance().getPendingTempAndDuration().remove(player);
        Test.getInstance().getPendingFurnace().remove(player);
    }

    @EventHandler
    public void temperatureAndDurationChoice(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Scegli calore e durata")) return;

        System.out.println(Test.getInstance().getPendingFurnace());

        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) event.setCancelled(true);
        if (clickedItem==null) return;



        HashMap<Player,Integer[]> pendingTempAndDuration =Test.getInstance().getPendingTempAndDuration();
        Player player = (Player)event.getWhoClicked();
        // Duration And temperature
        Integer[] DAT = pendingTempAndDuration.get(player);

        switch (clickedItem.getItemMeta().getDisplayName()){
            case "+10 Secondi": if (isOperationAllowed(1,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]+1,DAT[1]});
            break;
            case "+30 Secondi": if (isOperationAllowed(3,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]+3,DAT[1]});
            break;
            case "+1 Minuto": if (isOperationAllowed(6,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]+6,DAT[1]});
            break;
            case "-10 Secondi": if (isOperationAllowed(-1,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]-1,DAT[1]});
            break;
            case "-30 Secondi": if (isOperationAllowed(-3,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]-3,DAT[1]});
            break;
            case "-1 Minuto": if (isOperationAllowed(-6,DAT,0)) pendingTempAndDuration.put(player,new Integer[]{DAT[0]-6,DAT[1]});
            break;
            case "+1 Grado": if (isOperationAllowed(1,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]+1});
            break;
            case "+5 Gradi": if (isOperationAllowed(5,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]+5});
            break;
            case "+10 Gradi": if (isOperationAllowed(10,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]+10});
            break;
            case "-1 Grado": if (isOperationAllowed(-1,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]-1});
            break;
            case "-5 Gradi": if (isOperationAllowed(-5,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]-5});
            break;
            case "-10 Gradi": if (isOperationAllowed(-10,DAT,1)) pendingTempAndDuration.put(player,new Integer[]{DAT[0],DAT[1]-10});
            break;
            case "Close": {
                //CANCEL PENDING DATA
                player.closeInventory();
                return;
            }
            case "Conferma": {
                //CANCEL PENDING DATA
                //PUSH FINAL DATA TO STORAGE

                BlacksmithFurnace furnace = Test.getInstance().getPendingFurnace().get(player);


                MineralType mineralType = MineralItem.getMineralTypeFromGeneralItem(Test.getInstance().getPendingItemStack().get(player));

                //Get general mineral
                MineralItem mineralItem = mineralType.getMineralItem_0Rarity_0Purity();

                //Get and Apply Rarity and MineralPurity
                Rarity rarity = Rarity.getRarity(Test.getInstance().getPendingItemStack().get(player));
                MineralPurity purity = MineralPurity.getMineralPurity(Test.getInstance().getPendingItemStack().get(player));

                mineralItem.setRarity(rarity);
                mineralItem.setPurity(purity);

                furnace.startSmelting(mineralItem,DAT[0],DAT[1]);

                System.out.println(furnace.isSmelting());
                System.out.println(furnace.getDuration());
                System.out.println(furnace.getTemperature());
                System.out.println(furnace.getItemSmeltingJSON());

                Mysql.updateFurnace(Test.getInstance().getPendingFurnace().get(player));

                player.closeInventory();
                return;
            }
        }

        //UPDATE ITEMS TO SHOW CURRENT VALUE OF TEMPERATURE AND DURATION
        // Duration/Temperature Indicator
        DAT = pendingTempAndDuration.get(player);
        event.getClickedInventory().setItem(20,Util.createGuiItem(Material.CLOCK,"Durata",String.format("Durata: %s secondi",(DAT[0]*10)+1800)));
        event.getClickedInventory().setItem(24,Util.createGuiItem(Material.CAMPFIRE,"Temperatura",String.format("Calore: %s gradi",DAT[1]+30)));

    }


    private boolean isOperationAllowed(int amountToAdd, Integer[] DAT, int index){

        if(index==0){
            int startValue=DAT[0];
            int finalValue=startValue+amountToAdd;
            if(finalValue<minDurVal || finalValue>maxDurVal){
                return false;
            }else {
                return true;
            }
        }else {
            int startValue=DAT[1];
            int finalValue=startValue+amountToAdd;
            if(finalValue<minTempVal || finalValue>maxTempVal){
                return false;
            }else {
                return true;
            }
        }

    }



}
