package me.edoardo.test.base.presets;

import me.edoardo.test.database.Mysql;
import me.edoardo.test.presets.Bag;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class StorageGuiListener implements Listener {

    @EventHandler
    private void onItemGuiClick(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Storage ")) return;

        if (event.getSlot() < event.getView().getTopInventory().getSize()) {
            event.setCancelled(true);
        }

        if(event.getCurrentItem()==null || event.getCurrentItem().getItemMeta()==null) return;
        if(event.getCursor().getType()== Material.AIR || event.getCurrentItem().getItemMeta()==null) return;


        if(event.getCurrentItem().getType()!=Material.YELLOW_STAINED_GLASS_PANE) return;

        ItemStack itemCursor = event.getCursor();


        //Check if is depositing material with right bag
        if(!Bag.isBag(itemCursor))return;

        //check which bag is
        Bag bagCursor=new Bag(itemCursor);

        //get which storage are
        Storage storage = Storage.detectStorageFromView(event.getView());

        if(!storage.equals(bagCursor.getBagType().getCompatibleStorage())){
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage("Non stai mettendo la bag giusta!");
            event.getWhoClicked().sendMessage("Storage: "+storage.toString());
            event.getWhoClicked().sendMessage("Cursor: "+bagCursor.toString());
            return;
        }

        //-Start Deposit
        //Remove resources from bag
        int bagResources = bagCursor.getResources();

        int[] storageInfo = Mysql.getStorageInfo(storage,(Player)event.getWhoClicked());

        int storageResources = storageInfo[0];
        int maxStorageResources = storageInfo[1];

        //se lo storage è pieno rifiuta a prescindere
        if(storageResources==maxStorageResources){
            event.getWhoClicked().sendMessage("Il tuo storage é pieno potenzialo alla gestione base");
        }

        //se lo store non ha abbastanza spazio riempire lo store e svuotare di quanto basta la bag
        if(storageResources+bagResources>maxStorageResources){
            int removedResources = maxStorageResources-storageResources;
            bagCursor.addResources(-removedResources);
            itemCursor.setItemMeta(bagCursor.getItemStack().getItemMeta());
            Mysql.updateStorageInfo(storageInfo[2],maxStorageResources);
            event.getWhoClicked().sendMessage(String.format("Sono state depositate %s risorse",removedResources));
        }else{
            //svuotaBag
            bagCursor.addResources(-bagResources);
            itemCursor.setItemMeta(bagCursor.getItemStack().getItemMeta());

            //Aggiungi a database
            Mysql.updateStorageInfo(storageInfo[2],bagResources+storageResources);
            event.getWhoClicked().sendMessage(String.format("Sono state depositate %s risorse",bagResources));
        }



    }

}
