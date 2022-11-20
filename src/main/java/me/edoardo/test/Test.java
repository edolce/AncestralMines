package me.edoardo.test;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.edoardo.test.adminCommands.*;
import me.edoardo.test.base.BaseMenu;
import me.edoardo.test.base.listeners.InteractListener;
import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.base.presets.StorageGuiListener;
import me.edoardo.test.custom_blocks.CustomBlockBreakListener;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.eventListners.EventListener;
import me.edoardo.test.eventListners.PlaceCustomBlock;
import me.edoardo.test.job.old_alchemist.Alchemist;
import me.edoardo.test.job.blacksmith.Blacksmith;
import me.edoardo.test.job.blacksmith.PendingFurnacePlayerDecision;
import me.edoardo.test.job.blacksmith.presets.BlacksmithFurnace;
import me.edoardo.test.miniere.MineSystem;
import me.edoardo.test.rpInteraction.BlocksInteractions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class Test extends JavaPlugin {

    private static Test plugin;
    private final HashMap<Player, ItemStack> pendingItemStack = new HashMap<>();
    private final HashMap<Player, Integer[]> pendingTempAndDuration = new HashMap<>();
    private final HashMap<Player, BlacksmithFurnace> pendingFurnace = new HashMap<>();
    private static HologramsAPI hologramsAPI;
    private final HashMap<Player, HashMap<Vector,Integer>> blockInMiningState = new HashMap<>();

    //List to see available mineplaces:
    private final HashMap<Location, MineSystem> activeMines = new HashMap<>();


    @Override
    public void onEnable() {
        plugin=this;
        System.out.println("Il plugin e stato abilitato");
        loadStorageHolograms();
        Bukkit.getPluginManager().registerEvents(new EventListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlaceCustomBlock(),this);
        //Bukkit.getPluginManager().registerEvents(new BreakingResourceEvent(),this);
        Bukkit.getPluginManager().registerEvents(new CustomBlockBreakListener(),this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(),this);
        Bukkit.getPluginManager().registerEvents(new BaseMenu(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(),this);
        Bukkit.getPluginManager().registerEvents(new StorageGuiListener(),this);
        Bukkit.getPluginManager().registerEvents(new Alchemist(),this);
        Bukkit.getPluginManager().registerEvents(new Blacksmith(),this);
        Bukkit.getPluginManager().registerEvents(new PendingFurnacePlayerDecision(),this);
        Bukkit.getPluginManager().registerEvents(new BlocksInteractions(),this);
        Bukkit.getPluginManager().registerEvents(new BaseCommands(),this);
        getCommand("base").setExecutor(new BaseCommands());
        getCommand("cbgive").setExecutor(new GiveCustomBlock());
        getCommand("pickgive").setExecutor(new GiveCustomPickaxe());
        getCommand("mine").setExecutor(new TestMine());
        getCommand("testgui").setExecutor(new TestGui());

        ResultSet rs = Mysql.getData("SELECT * from user");

        while (true){
            try {
                if (!rs.next()) break;
                System.out.println("User Id: "+rs.getString("user_id"));
                System.out.println("Stone Storage Id: "+rs.getString("stone_storage_id"));
                System.out.println("Dirt Storage Id: "+rs.getString("dirt_storage_id"));
                System.out.println("Wood Storage Id: "+rs.getString("wood_storage_id"));
                System.out.println("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }

    @Override
    public void onDisable() {
        System.out.println("Il plugin e stato disabilitato");
    }

    public static Test getInstance(){
        return plugin;
    }

    public HashMap<Player, ItemStack> getPendingItemStack() {
        return pendingItemStack;
    }

    public HashMap<Player, Integer[]> getPendingTempAndDuration() {
        return pendingTempAndDuration;
    }

    public HashMap<Player, BlacksmithFurnace> getPendingFurnace() {
        return pendingFurnace;
    }

    public HashMap<Location, MineSystem> getActiveMines() {
        return activeMines;
    }

    public HashMap<Player, HashMap<Vector, Integer>> getBlockInMiningState() {
        return blockInMiningState;
    }

    private void loadStorageHolograms(){
        File file = new File( Test.getInstance().getDataFolder() + "/storageHologramsDatabase.yml");
        FileConfiguration holoStorage = YamlConfiguration.loadConfiguration(file);
        System.out.println(Test.getInstance().getDataFolder().getPath());
        System.out.println(file.getPath());

        for (String key:holoStorage.getKeys(false)){
            System.out.println(key);

            World world = Bukkit.getWorld(holoStorage.getString(key+".location").split(", ")[0]);
            double xPos = Double.parseDouble(holoStorage.getString(key+".location").split(", ")[1]);
            double yPos = Double.parseDouble(holoStorage.getString(key+".location").split(", ")[2]);
            double zPos = Double.parseDouble(holoStorage.getString(key+".location").split(", ")[3]);
            Location location = new Location(world,xPos,yPos,zPos);
            Player player = Bukkit.getPlayer(UUID.fromString(key.split("\\$")[0]));
            Storage storage = Storage.valueOf(key.split("\\$")[1]);

            //Create Hologram
            Test plugin = Test.getInstance();
            Hologram hologram = HologramsAPI.createHologram(plugin,location);
            hologram.appendTextLine(String.format("Magazzino di %s",storage));
            hologram.setAllowPlaceholders(true);
            int currentStorage = Mysql.getStorageInfo(storage,player)[0];
            int maxStorage = Mysql.getStorageInfo(storage,player)[1];
            HologramsAPI.registerPlaceholder(plugin, "%"+player.getUniqueId()+"-current-"+this+"-resources%", .5, () -> Integer.toString(Mysql.getStorageInfo(storage,player)[0]));
            hologram.appendTextLine("%"+player.getUniqueId()+"-current-"+this+"-resources%");
        }
    }
}
