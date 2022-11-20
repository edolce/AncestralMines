package me.edoardo.test.job.alchemist.presets;

import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.miniere.ancestral.AncestralDrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum Ingredient {
    //@TODO: AGGIUNGERE TENTATIVI PER SUGGERIMENTO
    PIETRA_STRANA(
            AncestralDrop.PIETRA_STRANA,
            Arrays.asList(
                    new Stage(10.0,90.0, 0, 10,0, new StageGame(6,1, 30)),
                    new Stage(10.0,90.0, 10, 10,1, new StageGame(6,2, 30)),
                    new Stage(10.0,90.0, 20, 10,2, new StageGame(6,3, 30)),
                    new Stage(10.0,90.0, 30, 10,3, new StageGame(8,3, 30)),
                    new Stage(10.0,90.0, 40, 10,4, new StageGame(8,4, 30)),
                    new Stage(10.0,90.0, 50, 10,5, new StageGame(10,4, 30)),
                    new Stage(10.0,90.0, 60, 10,6, new StageGame(10,5, 30)),
                    new Stage(10.0,90.0, 70, 10,7, new StageGame(13,5, 30)),
                    new Stage(10.0,90.0, 80, 10,8, new StageGame(13,6, 30)),
                    new Stage(10.0,90.0, 90, 10,9, new StageGame(16,7, 30)),
                    new Stage(10.0,90.0, 100, 10,10, new StageGame(16,8, 30))
            )
    ),
    PIETRA_CREPUSCOLARE(
            AncestralDrop.PIETRA_CREPUSCOLARE,
            Arrays.asList(
                    new Stage(10.0,90.0, 0, 10,0, new StageGame(6,1, 30)),
                    new Stage(10.0,90.0, 10, 10,1, new StageGame(6,2, 30)),
                    new Stage(10.0,90.0, 20, 10,2, new StageGame(6,3, 30)),
                    new Stage(10.0,90.0, 30, 10,3, new StageGame(8,3, 30)),
                    new Stage(10.0,90.0, 40, 10,4, new StageGame(8,4, 30)),
                    new Stage(10.0,90.0, 50, 10,5, new StageGame(10,4, 30)),
                    new Stage(10.0,90.0, 60, 10,6, new StageGame(10,5, 30)),
                    new Stage(10.0,90.0, 70, 10,7, new StageGame(13,5, 30)),
                    new Stage(10.0,90.0, 80, 10,8, new StageGame(13,6, 30)),
                    new Stage(10.0,90.0, 90, 10,9, new StageGame(16,7, 30)),
                    new Stage(10.0,90.0, 100, 10,10, new StageGame(16,8, 30))
            )
    ),
    PIETRA_MIRACOLOSA(
            AncestralDrop.PIETRA_MIRACOLOSA,
            Arrays.asList(
                    new Stage(10.0,90.0, 0, 10,0, new StageGame(6,1, 30)),
                    new Stage(10.0,90.0, 10, 10,1, new StageGame(6,2, 30)),
                    new Stage(10.0,90.0, 20, 10,2, new StageGame(6,3, 30)),
                    new Stage(10.0,90.0, 30, 10,3, new StageGame(8,3, 30)),
                    new Stage(10.0,90.0, 40, 10,4, new StageGame(8,4, 30)),
                    new Stage(10.0,90.0, 50, 10,5, new StageGame(10,4, 30)),
                    new Stage(10.0,90.0, 60, 10,6, new StageGame(10,5, 30)),
                    new Stage(10.0,90.0, 70, 10,7, new StageGame(13,5, 30)),
                    new Stage(10.0,90.0, 80, 10,8, new StageGame(13,6, 30)),
                    new Stage(10.0,90.0, 90, 10,9, new StageGame(16,7, 30)),
                    new Stage(10.0,90.0, 100, 10,10, new StageGame(16,8, 30))
            )
    );

    private final List<Stage> stages;
    private final AncestralDrop ancestralDrop;

    Ingredient(AncestralDrop ancestralDrop, List<Stage> stages) {
        this.ancestralDrop = ancestralDrop;
        this.stages=stages;
    }

    public AncestralDrop getAncestralDrop() {
        return ancestralDrop;
    }



    public Inventory getResearchGUI(Player player){
        Inventory gui = Bukkit.createInventory(null,54,"ricerca");
        Stage currentStage = Mysql.getCurrentIngredientStage(player,this);

        ItemStack backgroundItem = Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE," ");
        ItemStack goBackItem = Util.createGuiItem(Material.RED_STAINED_GLASS_PANE,"Torna Indietro");
        ItemStack displayInfoItem = Util.createGuiItem(ancestralDrop.getItemStack().getType(), "Info");
        ItemStack startResearchItem = Util.createGuiItem(Material.PAPER,"Start Research");

        for(int i=0;i<54;i++) gui.setItem(i,backgroundItem);

        //GO BACK
        gui.setItem(0,goBackItem);
        //DISPLAY INGREDIENT WITH INFO
        gui.setItem(4,displayInfoItem);
        //START RESEARCH BUTTON
        gui.setItem(8,startResearchItem);
        //AREA OF RESEARCH
        for(Map.Entry<Integer,ItemStack> entry: currentStage.getStageGame().getHashMapGridForGUI().entrySet()){
            gui.setItem(entry.getKey(),entry.getValue());
        }

        return gui;
    }

    public List<Stage> getStages() {
        return stages;
    }
}
