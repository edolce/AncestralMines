package me.edoardo.test.miniere;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class MineBlockSet {


    private final HashMap<MiningBlock,Integer> firstLayerSet;
    private final HashMap<MiningBlock,Integer> secondLayerSet;
    private final HashMap<MiningBlock,Integer> thirdLayerSet;

    private final Material firstLayerMat = Material.LIME_CONCRETE;
    private final Material secondLayerMat = Material.YELLOW_CONCRETE;
    private final Material thirdLayerMat = Material.RED_CONCRETE;

    public MineBlockSet(HashMap<MiningBlock, Integer> firstLayerSet,HashMap<MiningBlock, Integer> secondLayerSet,HashMap<MiningBlock, Integer> thirdLayerSet){
        this.firstLayerSet = firstLayerSet;
        this.secondLayerSet = secondLayerSet;
        this.thirdLayerSet = thirdLayerSet;
    }

    public HashMap<MiningBlock, Integer> getFirstLayerSet() {
        return firstLayerSet;
    }
    public HashMap<MiningBlock, Integer> getSecondLayerSet() {
        return secondLayerSet;
    }
    public HashMap<MiningBlock, Integer> getThirdLayerSet() {
        return thirdLayerSet;
    }
    public Material getFirstLayerMat() {
        return firstLayerMat;
    }
    public Material getSecondLayerMat() {
        return secondLayerMat;
    }
    public Material getThirdLayerMat() {
        return thirdLayerMat;
    }
    public HashMap<Material,HashMap<MiningBlock, Integer>> getWholeMap(){
        HashMap<Material,HashMap<MiningBlock, Integer>> wholeMap=new HashMap<>();
        wholeMap.put(firstLayerMat,firstLayerSet);
        wholeMap.put(secondLayerMat,secondLayerSet);
        wholeMap.put(thirdLayerMat,thirdLayerSet);
        return wholeMap;
    }

    //INPUT: LISTA DI VETTORI
    //OUTPUT: HASHMAP CONTENTE x% di Mining block a seconda della sua rarita (in ordine, ma in disordina a causa dell shuffle)
    public HashMap<Vector, MiningBlock> fakeRandomChoice(List<Vector> vectors,Material refMat) {

        HashMap<MiningBlock,Integer> miningBlocksRarity = getWholeMap().get(refMat);

        float size = vectors.size();
        HashMap<Vector,MiningBlock> hashMapFinal = new HashMap<>();
        float i=0;
        for (Vector vector:vectors){
            float k=0;
            for (MiningBlock miningBlock:miningBlocksRarity.keySet()){
                int percentage = miningBlocksRarity.get(miningBlock);
                if (i/size <= (k+percentage)/100.0) {
                    hashMapFinal.put(vector,miningBlock);
                    break;
                }
                k+=percentage;
            }
            i++;
        }
        return hashMapFinal;
    }

}
