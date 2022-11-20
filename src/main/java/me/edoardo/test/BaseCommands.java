package me.edoardo.test;

import com.google.common.util.concurrent.Service;
import me.edoardo.test.base.Base;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BaseCommands implements Listener, CommandExecutor {

    private final ItemStack bedItem = Util.createGuiItem(Material.BLACK_BED,"GO TO BASE HOME");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {



        getMenuBaseGui((Player) sender);

        return false;
    }



    private void getMenuBaseGui(Player player){
        int size = 54;

        Inventory menuBase = Bukkit.createInventory(null,size,"Menu Base");

        //FILL BACKGROUND
        ItemStack[] background = new ItemStack[size];
        ItemStack backgroundItem = Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"");
        Arrays.fill(background, backgroundItem);
        menuBase.setContents(background);

        //SET INTERACTIONS ITEMS
        //BASE HOME
        menuBase.setItem(21,bedItem);

        player.openInventory(menuBase);

    }

    @EventHandler
    public void onBedClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("Menu Base")) return;
        event.setCancelled(true);
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(!event.getCurrentItem().getItemMeta().getDisplayName().equals(bedItem.getItemMeta().getDisplayName())) return;
        Base base = new Base((Player) event.getWhoClicked());
        base.teleportPlayerToBase();
    }

}
