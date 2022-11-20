package me.edoardo.test.miniere.ancestral;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Ancestral {

    ANCESTRAL1(
            "Spawn",
            new ProbabilityDrop(AncestralDrop.PIETRA_STRANA,1,6,20),
            new ProbabilityDrop(AncestralDrop.PIETRA_CREPUSCOLARE,0.2,2,6)
    ),
    ANCESTRAL2(
            "AncestralMinion2",
            new ProbabilityDrop(AncestralDrop.PIETRA_STRANA,1,12,30),
            new ProbabilityDrop(AncestralDrop.PIETRA_CREPUSCOLARE,0.8,9,20),
            new ProbabilityDrop(AncestralDrop.PIETRA_MIRACOLOSA,0.5,2,8)
    );


    private final MythicMob mythicMob;
    private final List<ProbabilityDrop> probabilityDropList;
    private final Random random = new Random();

    Ancestral(String mmName,ProbabilityDrop... probabilityDrop) {
        this.mythicMob = MythicBukkit.inst().getMobManager().getMythicMob(mmName).orElse(null);
        this.probabilityDropList = Arrays.asList(probabilityDrop);
    }

    public ActiveMob spawnAtLoc(Location location){
        if(mythicMob != null){
            // spawns mob
            return mythicMob.spawn(BukkitAdapter.adapt(location),1);
        }
        return null;
    }

    public String getInternalName() {
        return mythicMob.getInternalName();
    }

    public List<ItemStack> getDrops() {
        List<ItemStack> drops=new ArrayList<>();
        for(ProbabilityDrop probabilityDrop:probabilityDropList){
            ItemStack drop=probabilityDrop.getDrop().getItemStack();
            int qnt = random.nextInt(probabilityDrop.getMaxQnt()-probabilityDrop.getMinQnt())+probabilityDrop.getMinQnt();
            drop.setAmount(qnt);
            drops.add(drop);
        }
        return drops;
    }
}
