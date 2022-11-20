package me.edoardo.test.job.alchemist;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.alchemist.presets.Ingredient;
import me.edoardo.test.job.alchemist.presets.Stage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlchemistMenu implements Listener {

    private final Player player;
    private final ItemStack deactivated = Util.createGuiItem(Material.GRAY_STAINED_GLASS_PANE,"Clicca Per Attivare");
    private final ItemStack activated = Util.createGuiItem(Material.GREEN_TERRACOTTA,"Clicca Per Disattivare");
    private final ItemStack none = Util.createGuiItem(Material.BARRIER,"[/]");
    private Stage selectedStage;
    private Ingredient selectedIngredient;
    private Research onGoingResearch;
    private boolean keepListenerAlive = true;


    //[GUI] - Alchemist Menu GUI
    public AlchemistMenu(Player player){
        this.player=player;

        Inventory menu = Bukkit.createInventory(null,27,"menu dell'alchimista");

        //FILL BACKGROUND
        ItemStack[] background = new ItemStack[menu.getSize()];
        ItemStack backgroundItem = Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"");
        Arrays.fill(background, backgroundItem);
        menu.setContents(background);

        menu.setItem(10,Util.createGuiItem(Material.CAULDRON, ColorUtils.translateColorCodes("&lCera Recipe")));
        menu.setItem(11,Util.createGuiItem(Material.BLACK_STAINED_GLASS_PANE,""));
        menu.setItem(12,Util.createGuiItem(Material.PAPER, ColorUtils.translateColorCodes("&lAvvia Ricerca")));
        menu.setItem(13,Util.createGuiItem(Material.BLACK_STAINED_GLASS_PANE,""));
        menu.setItem(14,Util.createGuiItem(Material.PHANTOM_MEMBRANE, ColorUtils.translateColorCodes("&lLista Ingredienti")));
        menu.setItem(15,Util.createGuiItem(Material.BLACK_STAINED_GLASS_PANE,""));
        menu.setItem(16,Util.createGuiItem(Material.BOOK, ColorUtils.translateColorCodes("&lLista Recipe Create")));

        Bukkit.getPluginManager().registerEvents(this, Test.getInstance());


        keepListenerAlive = true;
        player.openInventory(menu);
    }

    //[HANDLER] - Handler for "Main Alchemist" GUI
    @EventHandler
    public void onMenuItemClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("menu dell'alchimista"))return;
        event.setCancelled(true);
        keepListenerAlive = false;

        //Check which item is clicked
        switch (event.getCurrentItem().getType().toString()){
            case "PAPER": {
                keepListenerAlive = true;
                player.openInventory(getIngredientSelectionGUI());
            };
                break;
            case "CAULDRON": startRecipeCreationClickHandler();
                break;
            case "PHANTOM_MEMBRANE": ingredientListClickHandler();
                break;
            case "BOOK": createdRecipeListClickHandler();
                break;
        }
    }




    //[GUI] - Ingredient Selection GUI
    private Inventory getIngredientSelectionGUI(){
        Inventory gui = Bukkit.createInventory(null,9,"Scegli Ingrediente");
        gui.setItem(0, Ingredient.PIETRA_STRANA.getAncestralDrop().getItemStack());
        gui.setItem(1, Ingredient.PIETRA_CREPUSCOLARE.getAncestralDrop().getItemStack());
        gui.setItem(2, Ingredient.PIETRA_MIRACOLOSA.getAncestralDrop().getItemStack());
        return gui;
    }

    //[HANDLER] - Handler for "Ingredient Selection" GUI
    @EventHandler
    public void ingredientSelectionClickHandler(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("Scegli Ingrediente")) return;
        event.setCancelled(true);
        keepListenerAlive = false;
        //Controlla quale ingrediente e stato cliccato
        //TEST: CLICCATO PIETRA STRANA
        selectedIngredient = Ingredient.PIETRA_STRANA;

        //controlla se ce una recipe in corso
        List<Research> researches= Mysql.getResearchesOfIngredient(player,selectedIngredient);


        for(Research res:researches){
            if(!res.isFinished()) {
                //recipe e in corso
                onGoingResearch=res;
                keepListenerAlive = true;
                player.openInventory(res.getProcessGui());
                return;
            }
        }


        keepListenerAlive = true;
        player.openInventory(selectedIngredient.getResearchGUI((Player)event.getWhoClicked()));
        selectedStage = Mysql.getCurrentIngredientStage(player,selectedIngredient);
    }

    //[HANDLER] - Handler for "Process For New Research" GUI
    @EventHandler
    public void researchInProcessClickHandler(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("ricerca in processo")) return;
        event.setCancelled(true);
        keepListenerAlive = false;
        if(event.getCurrentItem().getType().equals(Material.PAPER)){
            System.out.println("Aggiornamento...");
            onGoingResearch.finish();
            player.closeInventory();
        }
    }
    //[HANDLER] - Handler for "Create New Research" GUI
    @EventHandler
    public void researchClickHandler(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("ricerca")) return;
        event.setCancelled(true);
        keepListenerAlive = false;

        Material clickedMat =  event.getCurrentItem().getType();

        //CLICCATO UNO DISATTIVATO
        if(clickedMat.equals(deactivated.getType())){
            event.getClickedInventory().setItem(event.getSlot(),activated);
        }
        //CLICCATO UNO ATTIVATO
        if(clickedMat.equals(activated.getType())){
            event.getClickedInventory().setItem(event.getSlot(),deactivated);
        }

        //CLICCA AVVIO RICERCA
        if(clickedMat.equals(Material.PAPER)){
            startResearch(event.getInventory());
        }
    }

    //[UTIL] - Button To Start Research is clicked (Check validity and push if true)
    private void startResearch(Inventory inventory) {
        //PARSE COMBINATION WITH HASHMAP
        List<Integer> gridFillOrder = Arrays.asList(21,22,23,30,31,32,20,29,24,33,12,13,14,39,40,41,11,15,38,42);
        List<Integer> parsedCombination = new ArrayList<>();
        for(int guiPointer : gridFillOrder){
            if(inventory.getItem(guiPointer).equals(none)) break;
            if(inventory.getItem(guiPointer).equals(deactivated)) parsedCombination.add(0);
            if(inventory.getItem(guiPointer).equals(activated)) parsedCombination.add(1);
        }


        Research newResearch = new Research(player,selectedIngredient,selectedStage,parsedCombination, System.currentTimeMillis(),0,null);

        //CHECK VALIDITY OF DATA
        //ADD NEW RESEARCH IN DATABASE WITH FINISHED_DATE=NULL
        if(!newResearch.isCombinationValid()){
            player.sendMessage("HAI SBAGLIATO A METTERE I COSI, RIFAI LA COMBINAZIONE");
            return;
        }
        //CONTROLLA SE LA COMBINAZIONE E GIA STATA USATA
        if(newResearch.isDuplicated()){
            player.sendMessage("NE HAI GIA FATTA UNA UGUALE SVEGLIA CAZZO!");
            return;
        }

        //AVVIA LA RICERCA (PUSHA NEL DATABASE)
        newResearch.pushMysql();
        player.closeInventory();
        HandlerList.unregisterAll(this);
    }

    //START RECIPE CREATION
    private void startRecipeCreationClickHandler() {
         
    }

    //INGREDIENT LIST PAGE
    private void ingredientListClickHandler() {
    }

    //RECIPE CREATED LIST
    private void createdRecipeListClickHandler() {
    }

    @EventHandler
    private void onCloseInventory(InventoryCloseEvent event){
        if(keepListenerAlive) return;
        System.out.println("CLOSING EVENT HANDLER");
        HandlerList.unregisterAll(this);
    }

}
