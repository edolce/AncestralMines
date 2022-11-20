package me.edoardo.test.job.blacksmith;

import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.Job;
import me.edoardo.test.job.blacksmith.presets.BlacksmithFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Blacksmith implements Job, Listener {

    private Player player;
    private String menuTitle="Menu del fabbro";

    private List<BlacksmithFurnace> furnaces;

    public Blacksmith(Player player){
        this.player=player;
    }
    public Blacksmith(){
    }

    private void updateFurnaces(Player player){
        furnaces = Mysql.getFurnaces(player);
    }

    //MAIN MENU
    @Override
    public void openMainMenu() {
        Inventory gui = Bukkit.createInventory(player,9,menuTitle);
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i, Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));
        }
        gui.setItem(0,Util.createGuiItem(Material.FURNACE,"Accedi Alle Tuo Fornaci",""));
        player.openInventory(gui);
    }
    @EventHandler
    public void onFurnaceMenuClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(menuTitle)) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack CursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
        }
        if (clickedItem==null) return;
        if (clickedItem.getType()==Material.FURNACE) openFurnacesListMenu((Player)event.getWhoClicked());
    }

    //FURNACES MENU
    private void openFurnacesListMenu(Player player){
        Inventory gui = Bukkit.createInventory(player,9,menuTitle+" - Lista Fornaci");
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i,Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));
        }
        //UPDATE FURNACES LIST and GET THEM
        updateFurnaces(player);

        int i=0;
        for(BlacksmithFurnace furnace:furnaces){
            gui.setItem(i,furnace.getItemStackDisplay());
            i++;
        }

        player.openInventory(gui);
    }
    @EventHandler
    public void onFurnacesList(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(menuTitle+" - Lista Fornaci")) return;
        ItemStack clickedItem = event.getCurrentItem();

        //RESETTA IL CLINCK SE NON E NELL'INVENTARIO
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
        }else return;

        // Controllo cursorItem e clickedItem
        if (clickedItem==null) return;


        if (clickedItem.getType().equals(Material.FURNACE)){
            handleSingleFurnaceInteraction(clickedItem,(Player) event.getWhoClicked(),event.getClickedInventory());
        }

    }
    private void handleSingleFurnaceInteraction(ItemStack clickedItem,Player player, Inventory inventory){
        //Get singleFurnace data [isSmelting,itemInSmelting,startTime,durationTime,temperature]
        int furnaceId = Integer.parseInt(clickedItem.getItemMeta().getDisplayName().split("#")[1]);
        BlacksmithFurnace furnace = Mysql.getFurnace(furnaceId);
        //Open SingleFurnace Menu
        Inventory gui = Bukkit.createInventory(player,27,furnace.getStringTitle());
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i,Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));
        }
        if (furnace.isSmelting()) {
            furnaceIsOnInteraction(furnace, gui,player);
        } else {
            furnaceIsOffInteraction(furnace, gui);
        }

        player.openInventory(gui);
    }

    //FURNACE IS ON VARIANT
    private void furnaceIsOnInteraction(BlacksmithFurnace furnace,Inventory gui,Player player){
        furnace.update();
        System.out.println(furnace.getMineralItem());
        System.out.println(furnace.getMineralItem().getMineralType());
        gui.setItem(4,furnace.getItemStackDisplay());
        gui.setItem(10,furnace.getMineralItem().getItemStack());
        gui.setItem(16,furnace.getSmeltedItem(player));
        gui.setItem(22,Util.createGuiItem(Material.CAMPFIRE,"WIP",""));

        //ADD VISUAL TIMER
        setVisualTimer(furnace,gui);

    }

    //GET VISUAL TIMER
    private void setVisualTimer(BlacksmithFurnace furnace,Inventory gui) {
        double totalTime = furnace.getDuration() + 60 * 30;
        double timeLeft = furnace.getRemainingSeconds();


        for (double i = 0; i < 5; i++) {
            if ((timeLeft / totalTime) < (i * (1.0 / 5.0)))
                gui.setItem((int) (15 - i), Util.createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "", ""));
            else
                gui.setItem((int) (15 - i), Util.createGuiItem(Material.RED_STAINED_GLASS_PANE, "", ""));
        }
    }

    //FURNACE IS OFF VARIANT
    private void furnaceIsOffInteraction(BlacksmithFurnace furnace,Inventory gui){
        gui.setItem(5,furnace.getItemStackDisplay());
        gui.setItem(10,Util.createGuiItem(Material.GREEN_CONCRETE,"WIP",""));
        //gui.setItem(16,furnace.getSmeltedItem());
        gui.setItem(23,Util.createGuiItem(Material.CAMPFIRE,"WIP",""));
    }


    //GET FINAL MATERIAL
    @EventHandler
    private void clickFinalMineralListener(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Fornace #")) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) event.setCancelled(true);
        if (clickedItem==null) return;
        if (event.getClickedInventory().first(clickedItem)==16) handleGetFinalMineral((Player) event.getWhoClicked(),event.getView());

    }

    private void handleGetFinalMineral(Player player, InventoryView view) {
        int furnaceId = Integer.parseInt(view.getTitle().split("#")[1]);
        BlacksmithFurnace furnace = Mysql.getFurnace(furnaceId);
        ItemStack finalItem = furnace.getSmeltedItem(player);

        //RESER FURNACE
        Mysql.resetFurnace(furnaceId);

        //TODO: ITEM FULL CHECK

        player.closeInventory();
        player.getInventory().addItem(finalItem);




    }
}
