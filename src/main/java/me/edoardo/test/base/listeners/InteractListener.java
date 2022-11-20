package me.edoardo.test.base.listeners;

import me.edoardo.test.base.presets.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {
    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent event){
        //Controllo se é un armor stand
        Entity target = event.getRightClicked();
        if(target.getType() != EntityType.ARMOR_STAND){
            return;
        }

        //Controllo che armor stand é
        if (((ArmorStand) target).getEquipment()==null) return;
        ItemStack helmet = ((ArmorStand) target).getEquipment().getHelmet();

        Storage storage = Storage.detectStorageFromItem(helmet);


        System.out.println(storage);

        event.getPlayer().openInventory(storage.getGui());



    }

    @EventHandler
    public void preventDraggingItemInGui(InventoryClickEvent event){
        if(event.getView().getTitle().equals("Storage")) event.setCancelled(true);
    }





}
