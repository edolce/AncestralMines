package me.edoardo.test.job.blacksmith.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Util;
import me.edoardo.test.job.old_alchemist.presets.Rarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineralItem {

    private final MineralType mineralType;
    private String title;
    private List<String> lore;
    private Rarity rarity;
    private MineralPurity purity;
    private final String mainColor="#8a0000";
    //#8a0000

    public MineralItem(MineralType mineralType, String title, Rarity rarity, MineralPurity purity) {
        this.mineralType = mineralType;
        this.rarity = rarity;
        this.purity = purity;
        lore= new ArrayList<>(Arrays.asList(rarity.toString(),purity.display));
        this.title = String.format("&#a3a3a3%s &#ffffff- &#3d3d3d[&%s%s&#3d3d3d]",mineralType.getName(),mainColor,"〶");
    }




    public ItemStack getItemStack(){
        ItemStack mineralItem = new ItemStack(mineralType.getMaterial());
        ItemMeta itemMeta = mineralItem.getItemMeta();
        itemMeta.setLore(normalLore());
        itemMeta.setDisplayName(ColorUtils.translateColorCodes(title));
        mineralItem.setItemMeta(itemMeta);

        return mineralItem;
    }


    //Create item lore
    private List<String> normalLore(){
        List<String> lore = new ArrayList<>();

        String[] arrayS= new String[]{
                "",
                String.format("&#3d3d3d--==|&%sMinerale Da Forgiare&#3d3d3d|==--",mainColor),
                String.format("&#a3a3a3Utile Al Fabbro"),
                "",
                String.format("&#3d3d3d--==|&%sUtilita&#3d3d3d|==--",mainColor),
                "&#a3a3a3Fabbricazione Armi",
                "",
                String.format("&#3d3d3d--==|&%sRarita&#3d3d3d|==--",mainColor),
                rarity.getName(),
                "",
                String.format("&#3d3d3d--==|&%sPurezza&#3d3d3d|==--",mainColor),
                String.format("%s",purity.display),
                "",
                "&#3d3d3d#003241"
        };

        for (String s:this.centerLines(arrayS)){
            System.out.println(s);
            lore.add(ColorUtils.translateColorCodes(s));
        }
        return lore;
    }

    //Create smelted item lore
    private List<String> smeltedLore(String quality){
        List<String> lore = new ArrayList<>();

        String[] arrayS= new String[]{
                "",
                String.format("&#3d3d3d--==|&%sMinerale &lForgiato&r&#3d3d3d|==--",mainColor),
                String.format("&#a3a3a3Utile Al Fabbro"),
                "",
                String.format("&#3d3d3d--==|&%sUtilita&#3d3d3d|==--",mainColor),
                "&#a3a3a3Fabbricazione Armi",
                "",
                String.format("&#3d3d3d--==|&%sRarita&#3d3d3d|==--",mainColor),
                rarity.getName(),
                "",
                String.format("&#3d3d3d--==|&%sPurezza&#3d3d3d|==--",mainColor),
                String.format("%s",purity.display),
                "",
                String.format("&#3d3d3d--==|&%sQualita&#3d3d3d|==--",mainColor),
                String.format("&l&f%s &#3d3d3d%%",quality),
                "",
                "&#3d3d3d#003241"
        };

        for (String s:this.centerLines(arrayS)){
            System.out.println(s);
            lore.add(ColorUtils.translateColorCodes(s));
        }
        return lore;
    }

    private List<String> centerLines(String[] toCenterA) {
        List<String> toCenter = new ArrayList<>(Arrays.asList(toCenterA));

        toCenter.add(title);

        List<String> newLore = Util.fixLines(toCenter);

        this.title = newLore.get(newLore.size()-1);
        newLore.remove(newLore.size()-1);

        return newLore;

    }

    public MineralType getMineralType() {
        return mineralType;
    }

    public String getTitle() {
        return title;
    }

    public void setPurity(MineralPurity purity) {
        this.purity = purity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public static MineralType getMineralTypeFromGeneralItem(ItemStack itemStack) {
        for(MineralType type:MineralType.values()){
            String title = itemStack.getItemMeta().getDisplayName();
            if(type.toString().equals(title)){
                return type;
            }
        }
        return null;
    }

    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mineral_type",mineralType.toString());
        jsonObject.put("title",title);
        jsonObject.put("rarity",rarity.toString());
        jsonObject.put("purity",purity.toString());

        return jsonObject.toString();
    }

    public ItemStack getSmeltedItem(double finalAccuracy) {

        ItemStack mineralItem = new ItemStack(mineralType.getMaterial());
        ItemMeta itemMeta = mineralItem.getItemMeta();


        itemMeta.setLore(smeltedLore(String.valueOf(finalAccuracy)));
        itemMeta.setDisplayName(title);

        mineralItem.setItemMeta(itemMeta);

        return mineralItem;

    }
}
