package me.edoardo.test.adminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestMine implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return false;
        }

        //Mine mine = new Mine(((Player) sender).getLocation().add(0,-((Player) sender).getLocation().getY()+56,0), miningBlockSet,(Player) sender);
        //MineSystem mineSystem = new MineSystem(miningBlockSet,(Player) sender, mineData);

        //mineSystem.createPhysicalMine();

        return true;
    }
}
