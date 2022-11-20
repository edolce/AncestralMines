package me.edoardo.test.base;

import me.edoardo.test.Util;
import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.presets.NewDrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BaseMenu implements Listener {

    private Inventory gui = Bukkit.createInventory(null, 9, "BaseMenu");

    private Player owner;

    public BaseMenu(Player owner){

        initGui();
        this.owner = owner;

    }

    public BaseMenu() {

    }

    private void initGui(){
        gui.setItem(0, Util.createGuiItem(Material.BLACK_CONCRETE,"Migliora Base Lavoro",""));
        gui.setItem(1, Util.createGuiItem(Material.WHITE_CONCRETE,"Migliora Sala Di Guerra",""));
        gui.setItem(2, Util.createGuiItem(Material.YELLOW_CONCRETE,"Migliora I Magazzini",""));
        gui.setItem(3, Util.createGuiItem(Material.GREEN_CONCRETE,"Migliora Officina",""));
    }

    //GuiClickHandler Gui:BaseMenu
    @EventHandler
    private void onGuiClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("BaseMenu"))return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();

        //Item Check
        switch (clickedItem.getItemMeta().getDisplayName()){
            case "Migliora I Magazzini": storageClickHandler((Player) event.getWhoClicked());
            break;
            case "Migliora Base Lavoro": jobsClickHandler((Player) event.getWhoClicked());
            break;
        }
    }



    private void jobsClickHandler(Player whoClicked){
        Inventory jobGui = Bukkit.createInventory(null, 9, "JobGui");
        jobGui.setItem(0, Util.createGuiItem(Material.BLACK_CONCRETE,"WIP",""));
        jobGui.setItem(1, Util.createGuiItem(Material.WHITE_CONCRETE,"WIP",""));
        jobGui.setItem(2, Util.createGuiItem(Material.YELLOW_CONCRETE,"WIP",""));
        jobGui.setItem(3, Util.createGuiItem(Material.GREEN_CONCRETE,"WIP",""));
        whoClicked.closeInventory();
        whoClicked.openInventory(jobGui);
    }



    //Storage GUI
    private void storageClickHandler(Player whoClicked){
        Inventory storageGui = Bukkit.createInventory(null, 9, "Storage Menu");
        storageGui.setItem(0, Util.createGuiItem(Material.BLACK_CONCRETE,"Potenzia Wood Storage",""));
        storageGui.setItem(1, Util.createGuiItem(Material.WHITE_CONCRETE,"Potenzia Stone Storage",""));
        storageGui.setItem(2, Util.createGuiItem(Material.YELLOW_CONCRETE,"Potenzia Dirt Storage",""));
        whoClicked.closeInventory();
        whoClicked.openInventory(storageGui);
    }

    @EventHandler
    private void onStorageGuiClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("Storage Menu"))return;
        event.setCancelled(true);

        ItemStack clickedItem =event.getCurrentItem();

        Player whoClicked = (Player) event.getWhoClicked();

        //Item Check
        switch (clickedItem.getItemMeta().getDisplayName()){
            case "Potenzia Wood Storage": {
                confirmStorageClickHandler(Storage.WOOD,whoClicked);
            }
            break;
            case "Potenzia Stone Storage":{
                confirmStorageClickHandler(Storage.STONE,whoClicked);
            }
            break;
            case "Potenzia Dirt Storage":{
                confirmStorageClickHandler(Storage.DIRT,whoClicked);
            }
        }
    }


    //Confirm Generic Storage Gui
    private void confirmStorageClickHandler(Storage storage, Player whoClicked){
        int storageLevel=Mysql.getStorageInfo(storage,whoClicked)[3];
        System.out.println(storageLevel);
        int stoneNeeded = storage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.STONE.name()));
        int woodNeeded = storage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.WOOD.name()));
        int dirtNeeded = storage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.DIRT.name()));
        Inventory storageGui = Bukkit.createInventory(null, 9, "Confirm "+storage);
        storageGui.setItem(0, Util.createGuiItem(Material.BLACK_CONCRETE,String.format("Vuoi upgradare %s Storage",storage),"Costo:","Stone: "+stoneNeeded,"Wood: "+woodNeeded,"Dirt: "+dirtNeeded));
        storageGui.setItem(1, Util.createGuiItem(Material.WHITE_CONCRETE,"Si",""));
        storageGui.setItem(2, Util.createGuiItem(Material.YELLOW_CONCRETE,"No",""));
        whoClicked.closeInventory();
        whoClicked.openInventory(storageGui);
    }

    //Confirm Generic Storage Event
    @EventHandler
    private void onConfirmStorageGuiClick(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Confirm"))return;
        event.setCancelled(true);

        ItemStack clickedItem =event.getCurrentItem();

        Storage selectedStorage = Storage.valueOf(event.getView().getTitle().split("Confirm ")[1]);

        //Item Check
        switch (clickedItem.getItemMeta().getDisplayName()){
            case "Si": {
                //Upgrade Check
                int[] storageInfo = Mysql.getStorageInfo(selectedStorage,(Player)event.getWhoClicked());

                //Controllo se l'utente ha abbastanza risorse
                int storageLevel=Mysql.getStorageInfo(selectedStorage, (Player) event.getWhoClicked())[3];
                int stoneNeeded = selectedStorage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.STONE.name()));
                int woodNeeded = selectedStorage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.WOOD.name()));
                int dirtNeeded = selectedStorage.getResourcesForLevelUP(storageLevel).get(NewDrop.valueOf(Storage.DIRT.name()));

                int[] stoneStorageInfo = Mysql.getStorageInfo(Storage.STONE, (Player) event.getWhoClicked());
                int[] woodStorageInfo = Mysql.getStorageInfo(Storage.WOOD, (Player) event.getWhoClicked());
                int[] dirtStorageInfo = Mysql.getStorageInfo(Storage.DIRT, (Player) event.getWhoClicked());

                if(
                        stoneStorageInfo[0]>=stoneNeeded &
                        woodStorageInfo[0]>=woodNeeded &
                        dirtStorageInfo[0]>=dirtNeeded
                ){
                    Mysql.updateStorageInfo(stoneStorageInfo[2],stoneStorageInfo[0]-stoneNeeded);
                    Mysql.updateStorageInfo(woodStorageInfo[2],woodStorageInfo[0]-woodNeeded);
                    Mysql.updateStorageInfo(dirtStorageInfo[2],dirtStorageInfo[0]-dirtNeeded);
                    event.getWhoClicked().sendMessage(String.format(
                            "Hai potenziato il magazzino di %s al livello %s",selectedStorage,storageLevel+1
                    ));
                    Mysql.levelUpStorage(storageInfo[2],selectedStorage.getLevelToMaxResources().get(storageInfo[3]+1),storageInfo[3]+1);
                    event.getWhoClicked().closeInventory();
                }else {
                    event.getWhoClicked().sendMessage("NON HAI ABBASTANZA RISORSE");
                    return;
                }


            }
            break;
            case "no":{
                event.getWhoClicked().closeInventory();
                //Upgrade Check
            }
        }
    }


    private void warClickHandler(){

    }

    private void robotClickHandler(){

    }

    public Inventory getGui() {
        return gui;
    }
}
