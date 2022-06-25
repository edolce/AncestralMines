package me.edoardo.test;


import me.edoardo.test.adminCommands.GiveCustomBag;
import me.edoardo.test.adminCommands.GiveCustomBlock;
import me.edoardo.test.adminCommands.GiveCustomPickaxe;
import me.edoardo.test.adminCommands.TestMine;
import me.edoardo.test.eventListners.BreakingResourceEvent;
import me.edoardo.test.eventListners.EventListener;
import me.edoardo.test.eventListners.PlaceCustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Test extends JavaPlugin {

    private static Test plugin;

    @Override
    public void onEnable() {
        plugin=this;
        System.out.println("Il plugin e stato abilitato");
        Bukkit.getPluginManager().registerEvents(new EventListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlaceCustomBlock(),this);
        Bukkit.getPluginManager().registerEvents(new BreakingResourceEvent(),this);
        getCommand("bgive").setExecutor(new GiveCustomBag());
        getCommand("cbgive").setExecutor(new GiveCustomBlock());
        getCommand("pickgive").setExecutor(new GiveCustomPickaxe());
        getCommand("mine").setExecutor(new TestMine());
    }

    @Override
    public void onDisable() {
        System.out.println("Il plugin e stato disabilitato");
    }

    public static Test getInstance(){
        return plugin;
    }
}
