package me.edoardo.test.adminCommands;

import me.edoardo.test.custom_blocks.CustomBlock;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GiveCustomPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return false;
        }

        if (args.length<1){
            return false;
        }

//        Pickaxe pickaxe1 = new Pickaxe(2,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE, hardnessLevel, 1,20,"#6b0000",
//                new HashMap<Integer,Double>() {{
//                    put(1, 100.0);
//                    put(2, 250.0);
//                    put(3, 500.0);
//                    put(4, 800.0);
//                    put(5, 1000.0);
//        }});
//        Pickaxe pickaxe2 = new Pickaxe(3,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE, hardnessLevel, 1,100,"#6b0000",
//                new HashMap<Integer,Double>() {{
//            put(1, 100.0);
//            put(2, 250.0);
//            put(3, 500.0);
//            put(4, 800.0);
//            put(5, 1000.0);
//        }});
//        Pickaxe pickaxe3 = new Pickaxe(4,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE, hardnessLevel, 1,40,"#6b0000", new HashMap<Integer,Double>() {{
//            put(1, 100.0);
//            put(2, 250.0);
//            put(3, 500.0);
//            put(4, 800.0);
//            put(5, 1000.0);
//        }});
//        Pickaxe pickaxe4 = new Pickaxe(5,"Piccone Base","〶",new CustomBlock[]{},1, Material.WOODEN_PICKAXE, hardnessLevel, 1,70,"#6b0000", new HashMap<Integer,Double>() {{
//            put(1, 100.0);
//            put(2, 250.0);
//            put(3, 500.0);
//            put(4, 800.0);
//            put(5, 1000.0);
//        }});


        int freeSlot=((Player) sender).getInventory().firstEmpty();
//        ((Player) sender).getInventory().setItem(freeSlot, pickaxe1.getItemStack());
//        ((Player) sender).getInventory().setItem(freeSlot+1, pickaxe2.getItemStack());
//        ((Player) sender).getInventory().setItem(freeSlot+2, pickaxe3.getItemStack());
//        ((Player) sender).getInventory().setItem(freeSlot+3, pickaxe4.getItemStack());

        return true;
    }
}
