package me.edoardo.test.miniere;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Util;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.miniere.ancestral.Nature;
import me.edoardo.test.miniere.ancestral.SecretType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;



//CLASSE CHE RACCHIUDE INFORMAZIONI DI UNA SINGOLA MINIERA
public class Mine {


    private final Player player;
    private final MineLevel level;
    private final int floors;
    private final int guiPosition;
    private final int tunnelPosition;
    private final Nature nature;
    private final SecretType secretType;
    private MineBlockSet mineBlockSet;

    private final String costante = "Costante";

    public Mine(Player player, MineLevel level, int floors, int guiPosition, int tunnelPosition, Nature nature, SecretType secretType) {
        this.player = player;
        this.level = level;
        this.floors = floors;
        this.guiPosition = guiPosition;
        this.tunnelPosition = tunnelPosition;
        this.nature = nature;
        this.secretType = secretType;
        this.mineBlockSet=level.getMineBlockSet();
    }


    public Player getPlayer() {
        return player;
    }

    public int getFloors() {
        return floors;
    }

    public MineLevel getLevel() {
        return level;
    }

    public Nature getNature() {
        return nature;
    }

    public SecretType getSecretType() {
        return secretType;
    }

    public int getGuiPosition() {
        return guiPosition;
    }

    public int getTunnelPosition() {
        return tunnelPosition;
    }


    public MineBlockSet getMineBlockSet() {
        return mineBlockSet;
    }

    //La probabilita di trovare un nuovo floor della miniera:
    //La probabilita e inversamente proporzionale al numero di floor scoperti
    //y={x^{2}}/{x^{2}+0.5x}
    public double getFindNewFloorProbability(){
        double x = floors;
        return 1-((x*x)/((x*x)+(0.5*x)));
    }

    //La probabilita di trovare una nuova miniera dello stesso livello:
    //Inversamente proporzionale al numero di miniere scoperte dello stesso livello
    public double getFindNewMineProbability(){
        double x = Mysql.getSpecificLevelMines(player,level).size();
        return 1-((x*x)/((x*x)+(2*x)));
    }

    //La probabilita di sbloccare il livello successivo
    //Proporzionale al livello di miniere sbloccate
    public double getFindNextLevelProbability(){
        //Controlla se il livello dopo e gia sbloccato
        if(Arrays.asList(MineLevel.values()).contains(MineLevel.valueOf("LEVEL"+level.getLevelInt()))) return 0;
        if(Mysql.getSpecificLevelMines(player, MineLevel.valueOf("LEVEL"+level.getLevelInt())).size()!=0) return 0;


        double x = Mysql.getSpecificLevelMines(player,level).size();
        return (x*x)/((x*x)+(1000*x));
    }

    //Controlla se ci sono posti disponibili per nuove miniere, se si allora mettili in una lista
    private List<int[]> getPossibleConnectedMines(){
        List<Mine> mines = Mysql.getSpecificLevelMines(player,level);

        //Create list of possible position regardless
        List<int[]> possibleRegardlessPositions=new ArrayList<>();

        //Transform int to vector
        Vector center = new Vector(guiPosition%5,guiPosition/5,0);

        possibleRegardlessPositions.add(new int[]{vectorToInt(center.clone().add(new Vector(-2,0,0))),vectorToInt(center.clone().add(new Vector(-1,0,0)))});
        possibleRegardlessPositions.add(new int[]{vectorToInt(center.clone().add(new Vector(2,0,0))),vectorToInt(center.clone().add(new Vector(1,0,0)))});
        possibleRegardlessPositions.add(new int[]{vectorToInt(center.clone().add(new Vector(0,-2,0))),vectorToInt(center.clone().add(new Vector(0,-1,0)))});
        possibleRegardlessPositions.add(new int[]{vectorToInt(center.clone().add(new Vector(0,2,0))),vectorToInt(center.clone().add(new Vector(0,1,0)))});

        for (int[] pos :possibleRegardlessPositions){
            for(Mine mine:mines){
                if(mine.getGuiPosition()==pos[0]) {
                    possibleRegardlessPositions.remove(pos);
                    break;
                }
            }
        }


        return possibleRegardlessPositions;
    }


    //Vector to int in a 5x? space
    private int vectorToInt(Vector vector){
        return (vector.getBlockY() * 5) + vector.getBlockX();
    }

    public int[] getGuiPositions(Random rand){
        List<int[]> p = getPossibleConnectedMines();
        return p.get(rand.nextInt(p.size()));
    }

    public ItemStack getItemGui() {


        //TODO: AGGIUNGERE NOME RANDOMICO
        String displayName = String.format("&#a3a3a3Covo Dei Corvi &#ffffff- %s",("&"+nature.getColor()+nature.getSymbol()));
        List<String> lore = Arrays.asList(
                "&#3d3d3d--=[&#8a0000Unlocked Floors&#3d3d3d]=--",
                String.format("&#a3a3a3%s",floors),
                "&#3d3d3d--=[&#8a0000Secret Type&#3d3d3d]=--",
                String.format("&#a3a3a3%s",secretType)
        );


        ItemStack item = new ItemStack(Material.GRAY_SHULKER_BOX,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);



        Util.colorAndCenterLoreAndName(item);

        return item;
    }
}
