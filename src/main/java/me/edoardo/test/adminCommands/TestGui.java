package me.edoardo.test.adminCommands;

import me.edoardo.test.Test;
import me.edoardo.test.base.BaseMenu;
import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.job.alchemist.AlchemistMenu;
import me.edoardo.test.job.old_alchemist.Alchemist;
import me.edoardo.test.job.old_alchemist.presets.CustomItem;
import me.edoardo.test.job.old_alchemist.presets.CustomItemPreset;
import me.edoardo.test.job.old_alchemist.presets.Rarity;
import me.edoardo.test.job.blacksmith.Blacksmith;
import me.edoardo.test.job.blacksmith.presets.MineralItem;
import me.edoardo.test.job.blacksmith.presets.MineralPurity;
import me.edoardo.test.job.blacksmith.presets.MineralType;
import me.edoardo.test.miniere.*;
import me.edoardo.test.presets.Bag;
import me.edoardo.test.presets.BagType;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.*;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TestGui implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        switch (args[0]){
            case "base":{
                BaseMenu baseMenu=new BaseMenu((Player) sender);
                ((Player) sender).openInventory(baseMenu.getGui());
            }
            break;
            case "storage":{
                if (args.length<2) return false;
                Storage storage=Storage.valueOf(args[1]);
                storage.spawnStorage((Player)sender);
            }
            break;
            case "alchemist":{
                Alchemist alchemist = new Alchemist(((Player) sender).getPlayer());
                alchemist.openMainMenu();
            }
            break;
            case "blacksmith":{
                Blacksmith blacksmith = new Blacksmith(((Player) sender).getPlayer());
                blacksmith.openMainMenu();
            }
            break;
            case "giveCustomItem":{
                if (args.length<2) return false;
                CustomItem customItem = new CustomItem(CustomItemPreset.valueOf(args[1]));
                int freeSlot=((Player) sender).getInventory().firstEmpty();
                ((Player) sender).getInventory().setItem(freeSlot, customItem.getItemStack());
            }
            break;
            case "giveMineralItem":{
                if (args.length<2) return false;
                MineralItem mineralItem = new MineralItem(MineralType.valueOf(args[1]),MineralType.valueOf(args[1]).toString(), Rarity.COMMON, MineralPurity.STAR0);
                int freeSlot=((Player) sender).getInventory().firstEmpty();
                ((Player) sender).getInventory().setItem(freeSlot, mineralItem.getItemStack());
            }
            break;
            case "miniere":{
//              MineSystem mineSystem = new MineSystem(miningBlockSet,(Player) sender, mineData);
//              Bukkit.getPluginManager().registerEvents(mineSystem, Test.getInstance());
//              Test.getInstance().getCommand("exit").setExecutor(mineSystem);
                MineMenu mineMenu= new MineMenu((Player)sender);
                Bukkit.getPluginManager().registerEvents(mineMenu, Test.getInstance());
                ((Player) sender).openInventory(mineMenu.getMineSelectionGui());
            }
            break;
            case "scoreboard":{
//                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
//                Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy","restr");
//
//                objective.setDisplayName("MINE INFO");
//                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//
//                Score remainingTimeScore= objective.getScore(String.format(ColorUtils.translateColorCodes("Tempo Rimasto: %s"),3));
//                Score obtainedResourcesScore = objective.getScore(String.format(ColorUtils.translateColorCodes("Risorse Ottenute: %s"),2));
//                Score roomNumberScore = objective.getScore(String.format(ColorUtils.translateColorCodes("Stanze Nella Miniera: %s"),1));
//                Score roomExploredScore = objective.getScore(ColorUtils.translateColorCodes("Stanze Esplorate: (WIP V0.2)"));
//                Score fossilsPresentScore = objective.getScore(ColorUtils.translateColorCodes("Ancestrali Presenti: (WIP V0.2)"));
//
//                remainingTimeScore.setScore(4);
//                obtainedResourcesScore.setScore(3);
//                roomNumberScore.setScore(2);
//                roomExploredScore.setScore(1);
//                fossilsPresentScore.setScore(0);
//
//                Team team = scoreboard.registerNewTeam("team");
//
//                team.addEntry("§r");
//                team.setPrefix("Some text.");

                Scoreboard board =  Bukkit.getScoreboardManager().getNewScoreboard();

                Objective obj = board.registerNewObjective("dummy", "title","rerewewer");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName("TITLE TEST");

                Team line1 = board.registerNewTeam("line1");
                line1.addEntry("§1");
                line1.setPrefix("line 1 info");

                obj.getScore("§1").setScore(1);




                ((Player) sender).setScoreboard(board);

                Bukkit.getScheduler().scheduleSyncRepeatingTask(Test.getInstance(), () -> {
                    board.getTeam("line1").setPrefix("updated line 1 info");
                },20, 60);
            }
            break;
            case "pick": {
                Pickaxe pickaxe = new Pickaxe(3,"TEST PICK",Material.WOODEN_PICKAXE, HardnessLevel.Level8,1,200,5);
                ((Player) sender).getInventory().addItem(pickaxe.getItemStack());
            }
            break;
            case "picktestclone": {
                Pickaxe pickaxe = new Pickaxe(3,"TEST PICK",Material.WOODEN_PICKAXE, HardnessLevel.Level8,1,200,8);
                Pickaxe pick2 = Pickaxe.getPickaxeFromItemStack(pickaxe.getItemStack());
                ((Player) sender).getInventory().addItem(pick2.getItemStack());
            }
            break;
            case "stonebag": {
                Bag stoneBag = new Bag(BagType.STONEBAG,0,2000);
                ((Player) sender).getInventory().addItem(stoneBag.getItemStack());
            }
            break;
            case "newalc": {
                AlchemistMenu menu = new AlchemistMenu((Player) sender);
            }
            break;
        }

        return true;
    }
}
