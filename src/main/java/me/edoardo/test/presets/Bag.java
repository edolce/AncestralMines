package me.edoardo.test.presets;

import me.edoardo.test.ColorUtils;
import me.edoardo.test.Test;
import me.edoardo.test.base.presets.Storage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bag {

    private final BagType bagType;
    private Integer quantity;
    private Integer maxQuantity;

    public Bag(ItemStack bagItem) {
        bagType = BagType.getCorrespondingBagType(bagItem);
        ItemMeta itemMeta = bagItem.getItemMeta();


        quantity = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "quantity"), PersistentDataType.INTEGER);
        maxQuantity = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "maxQuantity"), PersistentDataType.INTEGER);


        System.out.println(bagType);
        System.out.println(bagItem.getItemMeta());
        System.out.println(bagItem.getType());
        System.out.println(quantity);
        System.out.println(maxQuantity);
    }

    public Bag(BagType bagType, int quantity, int maxQuantity) {
        this.bagType = bagType;
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
    }

    public void updateBag(PlayerInventory inventory) {
        for (ItemStack itemStack : inventory) {
            //Controlla se l'item é una candidata BAG
            if (!Bag.isBag(itemStack)) continue;
            BagType bagType = BagType.getCorrespondingBagType(itemStack);
            //Se lo storage type matcha allora return the bag
            if (bagType.getCompatibleStorage() == this.bagType.getCompatibleStorage()) {
                int bagInvPos = inventory.first(itemStack);
                System.out.println("BAG TROVATA");
                inventory.setItem(bagInvPos, this.getItemStack());
                return;
            }
        }
        System.out.println("[ERRORE] UPDATE NELLA BAG/BAG NON TROVATA");
    }

    public int getResources() {
        return quantity;
    }


    public BagType getBagType() {
        return bagType;
    }

    //Get proper bag from given inventory
    public static Bag getBagFromInventory(Storage bagTypeStorage, PlayerInventory inventory) {
        for (ItemStack itemStack : inventory) {
            //Controlla se l'item é una candidata BAG
            if (!Bag.isBag(itemStack)) continue;
            BagType bagType = BagType.getCorrespondingBagType(itemStack);
            //Se lo storage type matcha allora return the bag
            if (bagType.getCompatibleStorage() == bagTypeStorage) return new Bag(itemStack);
        }
        return null;
    }

    public static boolean isBag(ItemStack item) {
        return item.getType() == Material.LEATHER_HORSE_ARMOR & Arrays.asList(2, 3, 4, 5).contains(item.getItemMeta().getCustomModelData());
    }

    public boolean isFull() {
        return quantity == maxQuantity;
    }

    public boolean addResources(int toAdd) {

        int res = this.quantity;
        int maxRes = this.maxQuantity;

        int resultRes;

        if (res + toAdd >= maxRes) {
            resultRes = maxRes;
            this.quantity = resultRes;
            return true;
        } else {
            resultRes = res + toAdd;
            this.quantity = resultRes;
            return false;
        }
    }

    private List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(bagType.getLore().get(0));
        lore.add(bagType.getLore().get(1));
        lore.add(String.format(bagType.getLore().get(2), quantity));
        lore.add("");
        return lore;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(bagType.getMaterial(), 1);

        //ITEM META
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(bagType.getDisplayName());
        itemMeta.setCustomModelData(bagType.getCustomModelData());
        itemMeta.setLore(getLore());

        System.out.println(quantity);
        System.out.println(maxQuantity);

        //PERSIOSTENT DATA
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(), "quantity"), PersistentDataType.INTEGER, quantity);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Test.getInstance(), "maxQuantity"), PersistentDataType.INTEGER, maxQuantity);


        itemStack.setItemMeta(itemMeta);

        ItemMeta meta2 = itemStack.getItemMeta();

        Integer quantityTEST = meta2.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "quantity"), PersistentDataType.INTEGER);
        Integer maxQuantityTEST = meta2.getPersistentDataContainer().get(new NamespacedKey(Test.getInstance(), "maxQuantity"), PersistentDataType.INTEGER);

        System.out.println(quantityTEST);
        System.out.println(maxQuantityTEST);

        return itemStack;

    }

    public int getMaxResources() {
        return maxQuantity;
    }
}
