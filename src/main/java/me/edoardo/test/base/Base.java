package me.edoardo.test.base;

import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.database.Mysql;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Base {

    private Location spawn;
    private Player owner;
    private final World baseWorld = Bukkit.getWorld("base_world");
    private final Vector firstCoordsSchematicBase = new Vector(8,63,8);
    private final Vector secondCoordsSchematicBase = new Vector(-8,69,-8);
    private final Vector centerCoords = new Vector(0,65,0);
    private final Vector centerOffset = firstCoordsSchematicBase.clone().subtract(centerCoords);

    public Base(Player player){
        owner=player;
        player.getUniqueId();
        //Get base info from database
        if(Mysql.doesPlayerHasBase(player)) {
            this.spawn=Mysql.getPlayerBase(player);
        }
        else createBase(player);
    }

    private void createBase(Player player){
        Location newBaseLoc = Mysql.createBase(player);
        this.spawn = newBaseLoc;

        for (int x=0;x>=secondCoordsSchematicBase.getBlockX()-firstCoordsSchematicBase.getBlockX();x--) for (int y=0;y<=secondCoordsSchematicBase.getBlockY()-firstCoordsSchematicBase.getBlockY();y++) for (int z=0;z>=secondCoordsSchematicBase.getBlockZ()-firstCoordsSchematicBase.getBlockZ();z--){
            Vector cursorOffset = new Vector(x,y,z);
            //COPY BLOCK
            Location cursorCopyLocation = new Location(baseWorld,0,0,0).add(firstCoordsSchematicBase.clone().add(cursorOffset));
            Block cursorCopyBlock = baseWorld.getBlockAt(cursorCopyLocation);
            //PASTE BLOCK
            Location cursorPasteLocation = new Location(baseWorld,0,0,0).add(newBaseLoc.clone().add(cursorOffset.clone().add(centerOffset)));
            Block cursorPasteBlock = baseWorld.getBlockAt(cursorPasteLocation);
            cursorPasteBlock.setType(cursorCopyBlock.getType());
        }

        //ADD EXTRA BLOCK (MINIERE/STORAGE ECC...)
        //STORAGES
        Location stoneStorage = spawn.clone().add(0.5,0,6);
        stoneStorage.setYaw(-180);
        Location dirtStorage = spawn.clone().add(2,0,5);
        dirtStorage.setYaw(170);
        Location woodStorage = spawn.clone().add(-1,0,5);
        woodStorage.setYaw(-170);
        Storage.STONE.spawnStorage(stoneStorage,player);
        Storage.DIRT.spawnStorage(dirtStorage,player);
        Storage.WOOD.spawnStorage(woodStorage,player);

    }

    public Location getSpawn() {
        return spawn;
    }


    public void teleportPlayerToBase(){
        owner.teleport(spawn);
    }
}
