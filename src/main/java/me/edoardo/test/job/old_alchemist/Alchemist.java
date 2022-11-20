package me.edoardo.test.job.old_alchemist;

import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.Job;
import me.edoardo.test.job.old_alchemist.presets.CustomItem;
import me.edoardo.test.job.old_alchemist.presets.CustomItemType;
import me.edoardo.test.job.old_alchemist.presets.EssenceType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Alchemist implements Job, Listener {

    private Player player;
    private String menuTitle="Menu dell'alchimista";

    private Cauldron cauldron;


    public Alchemist(Player player){
        this.player=player;
        this.cauldron=Mysql.getPlayerCauldron(player);
    }
    public Alchemist(){
    }


    //Update data inside cauldron to prevent erroneous data insertions
    private void updateCauldron(Player player){
        this.cauldron=Mysql.getPlayerCauldron(player);
    }
    private void pushCauldronToDatabase(Player player){
        Mysql.pushCauldron(player,cauldron);
    }

    //MAIN MENU
    @Override
    public void openMainMenu() {
        Inventory gui = Bukkit.createInventory(player,9,menuTitle);
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i,Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"Accedi al tuo calderone",""));
        }
        gui.setItem(0,Util.createGuiItem(Material.CAULDRON,"Accedi al tuo calderone",""));
        player.openInventory(gui);
    }
    @EventHandler
    public void onCauldronClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(menuTitle)) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack CursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
        }
        if (clickedItem==null) return;
        if (clickedItem.getType()==Material.CAULDRON) openCauldronMenu((Player)event.getWhoClicked());
    }

    //CAULDRON MENU
    private void openCauldronMenu(Player player){
        Inventory gui = Bukkit.createInventory(player,9,menuTitle+" - Creazione Recipe");
        for(int i=0;i<gui.getSize();i++){
            gui.setItem(i,Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"Accedi al tuo calderone",""));
        }
        updateCauldron(player);
        gui.setItem(1,cauldron.getItemStack());
        gui.setItem(7,Recipe.getUndiscoveredItemStackDisplay());
        player.openInventory(gui);
    }
    @EventHandler
    public void onInsertingIngredient(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(menuTitle+" - Creazione Recipe")) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
        }

        if (clickedItem==null) return;
        if (cursorItem==null || cursorItem.getType()==Material.AIR) {
            if (clickedItem.getType()==Material.PAPER) handleGetRecipeInteraction(cursorItem, (Player) event.getWhoClicked(),event.getClickedInventory());
            return;
        }
        if (clickedItem.getType()==Material.PAPER) handleGetRecipeInteraction(cursorItem, (Player) event.getWhoClicked(),event.getClickedInventory());
        if (clickedItem.getType()==Material.CAULDRON) handleCauldronInteraction(cursorItem, (Player) event.getWhoClicked(),event.getClickedInventory());
    }
    private void handleCauldronInteraction(ItemStack cursorItem,Player player, Inventory inventory) {

        CustomItem customItem = new CustomItem(cursorItem);
        System.out.println("TEST");

        //Controllo se cursorItem é un ingrediente
        if(!customItem.getCustomItemType().contains(CustomItemType.INGREDIENTS)) return;
        System.out.println("TEST");

        //Posso Aggiungerlo
        EssenceType mainEssence = customItem.getEssenceType();
        double mainEssencePercentage = customItem.getEssenceTypePercentage();
        double secondaryEssencesPercentage = 1-customItem.getEssenceTypePercentage();
        System.out.println("TEST");


        Random random = new Random();
        random.nextInt(100);
        EssenceType chosenEssence = null;
        int randomProbability=random.nextInt(101);
        System.out.println("TEST");


        if (randomProbability<=mainEssencePercentage*100){
            //Main essence é stata scelta

            chosenEssence = mainEssence;
            System.out.println("TEST");

        }else{
            //Non é stata scelta la main essence quindi viene presa random una delle tre rimaste
            List<EssenceType> remainedEssences = new ArrayList<>(Arrays.asList(EssenceType.values().clone()));
            remainedEssences.remove(mainEssence);

            chosenEssence = remainedEssences.get(random.nextInt(remainedEssences.size()));
            System.out.println("TEST");

        }

        //// AGGIUNGERE IL BUFF ALLA FUTURA
        //HashMap<EssenceType, BuffType> typeLinkedBuff= Mysql.getEssenceTypeLinkedBuff(player);
        //BuffType chosenBuff = typeLinkedBuff.get(chosenEssence);

        //AGGIUNGERE ESSENZA AL CALDERONE
        updateCauldron(player);
        int oldEssenceQuantity = this.cauldron.getEssenceInsideCalderon().get(chosenEssence);
        cauldron.getEssenceInsideCalderon().replace(chosenEssence,oldEssenceQuantity+1);
        pushCauldronToDatabase(player);

        System.out.println(chosenEssence);
        System.out.println(oldEssenceQuantity+1);

        if(cursorItem.getAmount()>1){
            cursorItem.setAmount(cursorItem.getAmount()-1);
        }else{
            System.out.println("CANCELLO OGGETTO");
            cursorItem.setAmount(0);
        }

        //REFRESH INVENTORY TO UPDRADE CAULDRON VISUALY
        for(ItemStack item:inventory.getContents()){
            if(item.getType()==Material.CAULDRON){
                item.setItemMeta(cauldron.getItemStack().getItemMeta());
            }
        }
    }
    private void handleGetRecipeInteraction(ItemStack cursorItem,Player player, Inventory inventory){
        //Reset cauldron essence
        Mysql.resetCauldron(player);
        updateCauldron(player);

        //Give Recipe
        player.getInventory().addItem(Recipe.getFinalItemStack(cauldron));



        //REFRESH INVENTORY TO UPDRADE CAULDRON VISUALY
        for(ItemStack item:inventory.getContents()){
            if(item.getType()==Material.CAULDRON){
                item.setItemMeta(cauldron.getItemStack().getItemMeta());
            }
        }
    }
}
