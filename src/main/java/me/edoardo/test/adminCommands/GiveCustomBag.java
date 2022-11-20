package me.edoardo.test.adminCommands;

import me.edoardo.test.presets.Bag;
import me.edoardo.test.presets.BagType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCustomBag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return false;
        }

        if (args.length<1){
            return false;
        }

        try {
            int freeSlot=((Player) sender).getInventory().firstEmpty();
            ((Player) sender).getInventory().setItem(freeSlot, BagType.valueOf(args[0]).getBlankBagItem());
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
