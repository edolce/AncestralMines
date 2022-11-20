package me.edoardo.test.miniere;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
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
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MineMenu implements Listener {

    private final ItemStack scrollLevelUp= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Level Up"));
    private final ItemStack scrollLevelDown= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Level Down"));
    private final ItemStack level1= Util.createGuiItem(Material.COBBLESTONE, ColorUtils.translateColorCodes("&f&lLivello 1"));
    private final ItemStack level2= Util.createGuiItem(Material.ANDESITE, ColorUtils.translateColorCodes("&f&lLivello 2"));
    private final ItemStack level3= Util.createGuiItem(Material.STONE, ColorUtils.translateColorCodes("&f&lLivello 3"));
    private final ItemStack level4= Util.createGuiItem(Material.STONE_BRICKS, ColorUtils.translateColorCodes("&f&lLivello 4"));
    private final ItemStack level5= Util.createGuiItem(Material.NETHER_BRICKS, ColorUtils.translateColorCodes("&f&lLivello 5"));
    private final ItemStack level6= Util.createGuiItem(Material.BASALT, ColorUtils.translateColorCodes("&f&lLivello 6"));
    private final ItemStack notPossibleMine= Util.createGuiItem(Material.RED_STAINED_GLASS_PANE, ColorUtils.translateColorCodes("&4&l[/]"));
    private final ItemStack possibleMine= Util.createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, ColorUtils.translateColorCodes("&3&lPossibile Miniera"));
    private final ItemStack mine = Util.createGuiItem(Material.GRAY_SHULKER_BOX, ColorUtils.translateColorCodes("&1&lMiniera"));
    private final ItemStack tunnel= Util.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ColorUtils.translateColorCodes("&f&lTunnel"));
    private final ItemStack scrollPageUp1= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Up 1"));
    private final ItemStack scrollPageUp2= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Up 2"));
    private final ItemStack scrollPageUp5= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Up 5"));
    private final ItemStack scrollPageDown1= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Down 1"));
    private final ItemStack scrollPageDown2= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Down 2"));
    private final ItemStack scrollPageDown5= Util.createGuiItem(Material.ARROW, ColorUtils.translateColorCodes("&f&lScroll Down 5"));
    private final ItemStack border= Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE," ");
    private final Player owner;
    private final String mineSelectionTitle="Miniere";
    private final Inventory mineSelectionGui= Bukkit.createInventory(null,54,mineSelectionTitle);
    private final String selectedMineTitle="Selected Mine";
    private final Inventory selectedMineGui= Bukkit.createInventory(null,27,selectedMineTitle);
    private final ItemStack info= Util.createGuiItem(Material.PAPER," ");
    private final ItemStack goBack= Util.createGuiItem(Material.RED_TERRACOTTA,ColorUtils.translateColorCodes("&#a3a3a3&lTorna Indietro"));
    private final ItemStack startMine= Util.createGuiItem(Material.GREEN_TERRACOTTA,ColorUtils.translateColorCodes("&#a3a3a3&lEntra Nella Miniera"));
    private int levelScrollIndex=0;
    private int minePageScrollIndex=0;
    private int levelSelected=1;
    private boolean levelChanged=true;
    private List<Mine> mines;


    private Mine selectedMine=null;

    //Util Lists for handle click event
    private final List<ItemStack> levels = Arrays.asList(level1,level2,level3,level4,level5,level6);
    private final List<ItemStack> pageScrolls = Arrays.asList(scrollPageUp5,scrollPageUp2,scrollPageUp1,scrollPageDown1,scrollPageDown2,scrollPageDown5);

    public MineMenu(Player player) {
        this.owner=player;
        mines = Mysql.getSpecificLevelMines(player,MineLevel.LEVEL1);
    }




    //GUI SETUPS
    //GUI FOR MINE SELCTION
    public Inventory getMineSelectionGui(){
        Inventory gui=mineSelectionGui;
        while(gui.firstEmpty()!=-1) gui.addItem(Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));

        //SETUP FIRST COLUMN
        gui.setItem(0,this.scrollLevelUp);
        if(levelScrollIndex<0) levelScrollIndex=0;
        if(levelScrollIndex>2) levelScrollIndex=2;
        for(int i=0;i<4;i++){
            gui.setItem((i+1)*9,levels.get(levelScrollIndex+i));
        }
        gui.setItem(45,this.scrollLevelDown);

        //SETUP LAST COLUMN
        gui.setItem(8,this.scrollPageUp5);
        gui.setItem(9+8,this.scrollPageUp2);
        gui.setItem(18+8,this.scrollPageUp1);
        gui.setItem(27+8,this.scrollPageDown1);
        gui.setItem(36+8,this.scrollPageDown2);
        gui.setItem(45+8,this.scrollPageDown5);

        //SETUP MINES MAP
        if(levelChanged) mines=Mysql.getSpecificLevelMines(owner,MineLevel.valueOf("LEVEL"+levelSelected));
        levelChanged=false;
        ConcurrentHashMap<Vector, String> map3D=organize3DSpace();


        for(int x=0;x<5;x++) for(int y=0;y<6;y++){
            Vector mapPosition = new Vector(x,y+minePageScrollIndex,0);
            ItemStack item = map3D.containsKey(mapPosition) ? getItemFromString(map3D.get(mapPosition),mapPosition) : notPossibleMine;
            gui.setItem((y*9)+2+x,item);
        }

        return gui;
    }
    //GUI FOR SELECTED MINE
    private Inventory getSelectedMineGui(){
        //BACKGROUND
        for (int i=0;i<=1;i++) for (int y=0;y<=8;y++) selectedMineGui.setItem((i*18)+y,border);
        selectedMineGui.setItem(10,border);
        selectedMineGui.setItem(16,border);
        //INFO
        //INFO 1:
        ItemStack info1 = Util.createGuiItem(Material.PAPER,"&#a3a3a3Probabilità di scoperta",
                "&#3d3d3d--=[&#8a0000Probabilità Nuovo Piano&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getFindNewFloorProbability()),
                "&#3d3d3d--=[&#8a0000Probabilità Nuova Miniera&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getFindNewMineProbability()),
                "&#3d3d3d--=[&#8a0000Probabilità Prossimo Livello&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getFindNextLevelProbability())
        );
        ItemStack info2 = Util.createGuiItem(Material.PAPER,"&#a3a3a3Ancestrali e Minerali",
                "&#3d3d3d--=[&#8a0000Ancestrali Presenti&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getLevel().getAncestral()),
                String.format("&#a3a3a3%s",selectedMine.getLevel().getAncestralsQuantity()),
                "&#3d3d3d--=[&#8a0000Minerali Presenti&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getLevel().getMaxMineralQuantity())
        );
        ItemStack info3 = Util.createGuiItem(Material.PAPER,"&#a3a3a3Buff Totali",
                "&#3d3d3d--=[&#8a0000Buff Ancestrali&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getSecretType().getMonsterSpawnMultiplier()),
                "&#3d3d3d--=[&#8a0000Buff Minerali&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getSecretType().getMineralSpawnMultiplier()),
                "&#3d3d3d--=[&#8a0000Buff Stanze&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getSecretType().getRoomMultiplier()),
                "&#3d3d3d--=[&#8a0000Buff Tempo&#3d3d3d]=--",
                String.format("&#a3a3a3%s",selectedMine.getSecretType().getTimeMultiplier())
        );
        ItemStack info4 = Util.createGuiItem(Material.PAPER,"&#a3a3a3Informazioni Generali",
                "&#3d3d3d--=[&#8a0000Tempo A Disposizione&#3d3d3d]=--",
                String.format("&#a3a3a3%s","100 Secondi"),
                "&#3d3d3d--=[&#8a0000Stanze Disponibili&#3d3d3d]=--",
                String.format("&#a3a3a3%s","14 - 17")
        );

        Util.colorAndCenterLoreAndName(info1);
        Util.colorAndCenterLoreAndName(info2);
        Util.colorAndCenterLoreAndName(info3);
        Util.colorAndCenterLoreAndName(info4);

        selectedMineGui.setItem(11,info1);
        selectedMineGui.setItem(12,info2);
        selectedMineGui.setItem(13,info3);
        selectedMineGui.setItem(14,info4);
        selectedMineGui.setItem(15,notPossibleMine);
        //GO BACK
        selectedMineGui.setItem(9,goBack);
        //START MINE
        selectedMineGui.setItem(17,startMine);
        return selectedMineGui;
    }

    private ItemStack getItemFromString(String string,Vector mapPosition){
        switch (string){
            case "mine": {
                int intPos = mapPosition.getBlockX()+(mapPosition.getBlockY()*5);
                for(Mine mine:mines){
                    if(mine.getGuiPosition()==intPos) return mine.getItemGui();
                }
            }
            case "tunnel": return tunnel;
            case "possible": return possibleMine;
            case "notPossible": return notPossibleMine;
        }
        return null;
    }

    private ConcurrentHashMap<Vector, String> organize3DSpace(){
        ConcurrentHashMap<Vector,String> map3D = new ConcurrentHashMap<>();
        for(Mine mine:mines){
            int yPos = mine.getGuiPosition()/5;
            int xPos = mine.getGuiPosition()%5;
            Vector pos = new Vector(xPos,yPos,0);
            map3D.put(pos,"mine");
            if(mine.getTunnelPosition()== -1) continue;
            int yTunPos = mine.getTunnelPosition()/5;
            int xTunPos = mine.getTunnelPosition()%5;
            Vector tunPos = new Vector(xTunPos,yTunPos,0);
            map3D.put(tunPos,"tunnel");
        }

        for(Vector vector:map3D.keySet()){
            //Controlla se ci sono blocchi vicini liberi per un tunnel
            if(map3D.get(vector)=="mine"){
                for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++) {
                    if(Math.abs(x)==Math.abs(y)) continue;
                    Vector offset = new Vector(x,y,0);
                    if (!map3D.containsKey(vector.clone().add(offset))) {
                        //validate tunnel
                        Vector possibleMine = vector.clone().add(offset.clone().multiply(2));


                        if (!map3D.containsKey(possibleMine)){
                            if(possibleMine.getBlockX()<0 || possibleMine.getBlockX()>4) continue;
                            if(possibleMine.getBlockY()<0) continue;
                            //validate both points
                            map3D.put(vector.clone().add(offset),"possible");
                            map3D.put(possibleMine,"possible");

                        }

                    }
                }
            }
        }

        System.out.println(map3D);
        return map3D;

    }

    //MISE SELECTION MENU HANDLER
    @EventHandler
    public void onClickedInventoryMineSelection(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(mineSelectionTitle)) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        //OPEN SINGLE MINE MENU
        if(clickedItem.getType().equals(mine.getType())){

            int translatedPos = (5*minePageScrollIndex)+(event.getSlot()-(2+(4*(event.getSlot()/9))));

            for(Mine singleMine:mines){
                if(singleMine.getGuiPosition()==translatedPos) selectedMine=singleMine;
            }

            event.getWhoClicked().openInventory(getSelectedMineGui());
            return;
        }
        //CHANGE LEVEL
        int i=1;

        for(ItemStack level:levels){
            if (level.getType()==clickedItem.getType()){
                levelSelected= i;
                levelChanged=true;
                event.getWhoClicked().openInventory(getMineSelectionGui());
                return;
            }
            i++;
        }
        if(clickedItem.getItemMeta()==null) return;
        //SCROLL LEVELS
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Level Up")){
            System.out.println("CLICCATO");
            if(levelScrollIndex==0) return;
            levelScrollIndex--;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Level Down")){
            if(levelScrollIndex+4==levels.size()) return;
            levelScrollIndex++;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        //SCROLL PAGE
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Up 1")){
            if(minePageScrollIndex-1<0) return;
            minePageScrollIndex-=1;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Up 2")){
            if(minePageScrollIndex-2<0) {
                minePageScrollIndex=0;
                event.getWhoClicked().openInventory(getMineSelectionGui());
                return;
            }
            minePageScrollIndex-=2;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Up 5")){
            if(minePageScrollIndex-5<0) {
                minePageScrollIndex=0;
                event.getWhoClicked().openInventory(getMineSelectionGui());
                return;
            }
            minePageScrollIndex-=5;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Down 1")){
            minePageScrollIndex+=1;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Down 2")){
            minePageScrollIndex+=2;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
        if(clickedItem.getItemMeta().getDisplayName().contains("Scroll Down 5")){
            minePageScrollIndex+=5;
            event.getWhoClicked().openInventory(getMineSelectionGui());
            return;
        }
    }

    //SELECTED MINE MENU HANDLER
    @EventHandler
    public void onClickedInventorySelectedMine(InventoryClickEvent event){
        if(!event.getView().getTitle().equals(selectedMineTitle)) return;
        event.setCancelled(true);
        //START MINE
        if(event.getCurrentItem().getType().equals(startMine.getType())){
            if(selectedMine==null){
                event.getWhoClicked().sendMessage("ERRORE NON HAI SELZIONATO NESSUNA MINIERA");
                return;
            }
            MineSystem mineSystem = new MineSystem((Player) event.getWhoClicked(), selectedMine);
            Bukkit.getPluginManager().registerEvents(mineSystem, Test.getInstance());
            Test.getInstance().getCommand("exit").setExecutor(mineSystem);
            mineSystem.searchPlaceAndStart();
        }
        //GO BACK
        if(event.getCurrentItem().getType().equals(goBack.getType())){
            event.getWhoClicked().openInventory(getMineSelectionGui());
        }
    }

    //EXIT FROM THE INVENTORY -> RESET EVENT LISTENER
    @EventHandler
    public void onClickedInventorySelectedMine(InventoryCloseEvent event){
        if(!event.getView().getTitle().equals(selectedMineTitle)) return;
        HandlerList.unregisterAll(this);
    }



}
