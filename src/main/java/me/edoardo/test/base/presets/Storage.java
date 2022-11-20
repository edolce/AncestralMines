package me.edoardo.test.base.presets;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.UtilHolo;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.presets.NewDrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Storage{



    WOOD(3,Arrays.asList(1000,2500,7000,15000,50000),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300)),
    STONE(2,Arrays.asList(1000,2500,7000,15000,50000),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300)),
    DIRT(4,Arrays.asList(1000,2500,7000,15000,50000),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300)),
    LEAF(5,Arrays.asList(1000,2500,7000,15000,50000),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300),
            Arrays.asList(300,300,300));

    private final HashMap<Integer,Integer> levelToMaxResources = new HashMap<>();
    private final HashMap<Integer,HashMap<NewDrop,Integer>> levelToResourcesToUpgrade = new HashMap<>();
    private int customModelData;
    private String title="Storage "+this.toString();
    private final Inventory gui=Bukkit.createInventory(null, 9, title);

    Storage(int modelData, List<Integer> maxResourcesPerLevel, List<Integer>... levelToResourceToUpgrade){
        initializeItems();
        this.customModelData = modelData;

        for(int i=0;i<maxResourcesPerLevel.size();i++){
            levelToMaxResources.put(i+1, maxResourcesPerLevel.get(i));
        }


        for(int i=0;i<levelToResourceToUpgrade.length;i++){
            int finalI = i;
            levelToResourcesToUpgrade.put(
                    i+1,
                    new HashMap<NewDrop,Integer>(){{
                        put(NewDrop.STONE,levelToResourceToUpgrade[finalI].get(0));
                        put(NewDrop.WOOD,levelToResourceToUpgrade[finalI].get(1));
                        put(NewDrop.DIRT,levelToResourceToUpgrade[finalI].get(2));
                    }});
        }

        System.out.println(levelToResourcesToUpgrade);
    }

    //GETTER
    public HashMap<Integer, Integer> getLevelToMaxResources() {
        return levelToMaxResources;
    }
    public Inventory getGui() {
        return gui;
    }
    public int getCustomModelData() {
        return customModelData;
    }
    public HashMap<NewDrop, Integer> getResourcesForLevelUP(int level) {
        return levelToResourcesToUpgrade.get(level);
    }

    //GUI ITEMS
    public void initializeItems() {

        for (int i=0;i<gui.getSize();i++){
            gui.setItem(i,Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE, " ", ""));
        }

        gui.setItem(4,Util.createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Clicca qua con la tua bag in mano", ""));
    }

    //Custom Item Storage
    private ItemStack getCustomItem(){
        ItemStack customItem = new ItemStack(Material.IRON_INGOT);

        ItemMeta itemMeta = customItem.getItemMeta();
        itemMeta.setCustomModelData(customModelData);

        customItem.setItemMeta(itemMeta);
        return customItem;

    }

    //Spawn Storage
    public void spawnStorage(Player player){
        spawnStorage(player.getLocation(),player);
    }

    public static Storage detectStorageFromView(InventoryView view) {

        for (Storage storage:Storage.values()){
            if(view.getTitle().equals(storage.title)){
                return storage;
            }
        }
        return null;
    }
    public static Storage detectStorageFromItem(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        for (Storage storage:Storage.values()){
            if(storage.getCustomModelData()==meta.getCustomModelData()){
                return storage;
            }
        }
        return null;
    }

    public void spawnStorage(Location location,Player player) {

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.getEquipment().setHelmet(getCustomItem());
        armorStand.setInvisible(true);
        armorStand.setRotation(location.getYaw(),location.getPitch());

        //Create Hologram
        Test plugin = Test.getInstance();
        Hologram hologram = HologramsAPI.createHologram(plugin,location.add(0,2,0));
        hologram.appendTextLine(String.format("Magazzino di %s",this));

        hologram.setAllowPlaceholders(true);

        int currentStorage = Mysql.getStorageInfo(this,player)[0];
        int maxStorage = Mysql.getStorageInfo(this,player)[1];

        HologramsAPI.registerPlaceholder(plugin, "%"+player.getUniqueId()+"-current-"+this+"-resources%", .5, () -> Integer.toString(Mysql.getStorageInfo(this,player)[0]));



        //hologram.appendTextLine(String.format(String.format("%s / %s",currentStorage,maxStorage)));
        hologram.appendTextLine("%"+player.getUniqueId()+"-current-"+this+"-resources%");

        UtilHolo utilHolo = new UtilHolo(player.getUniqueId().toString()+"$"+this,hologram);
        utilHolo.writeValue();
    }
}
