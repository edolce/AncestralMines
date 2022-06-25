package me.edoardo.test.adminCommands;

import me.edoardo.test.miniere.Mine;
import me.edoardo.test.presets.CustomBlock;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.Material;
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

        Mine mine = new Mine();

        return true;
    }
}
