package me.edoardo.test.adminCommands;

import me.edoardo.test.presets.CustomBlock;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCustomPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return false;
        }

        if (args.length<1){
            return false;
        }

        Pickaxe pickaxe1 = new Pickaxe(2,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE,1,0,"#6b0000");
        Pickaxe pickaxe2 = new Pickaxe(3,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE,1,0,"#6b0000");
        Pickaxe pickaxe3 = new Pickaxe(4,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE,1,0,"#6b0000");
        Pickaxe pickaxe4 = new Pickaxe(5,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE,1,0,"#6b0000");


        int freeSlot=((Player) sender).getInventory().firstEmpty();
        ((Player) sender).getInventory().setItem(freeSlot, pickaxe1.getItemStack());
        ((Player) sender).getInventory().setItem(freeSlot+1, pickaxe2.getItemStack());
        ((Player) sender).getInventory().setItem(freeSlot+2, pickaxe3.getItemStack());
        ((Player) sender).getInventory().setItem(freeSlot+3, pickaxe4.getItemStack());

        return true;
    }
}
