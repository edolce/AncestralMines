package me.edoardo.test;

import me.edoardo.test.database.Mysql;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {


    //Control if player is in database
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();


        //Check if player is in database
        if(Mysql.isPlayerInDB(uuid)) return;

        //Insert New PLayer in database
        System.out.println("Sto Aggiungendo il file");
        Mysql.insertNewUser(uuid);
    }
}
