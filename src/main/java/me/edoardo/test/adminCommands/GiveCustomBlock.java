package me.edoardo.test.adminCommands;

import me.edoardo.test.presets.CustomBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCustomBlock implements CommandExecutor {
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
            ((Player) sender).getInventory().setItem(freeSlot, CustomBlock.valueOf(args[0]).getItem());
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
