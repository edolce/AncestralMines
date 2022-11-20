package me.edoardo.test;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Util {

    private static final HashMap<Character,Integer> keys= new HashMap<Character,Integer>() {{
        put('A',5);
        put ('a',4);
        put ('á',4);
        put ('à',4);
        put ('B',5);
        put ('b',5);
        put ('C',5);
        put ('c',5);
        put ('D',5);
        put ('d',5);
        put ('E',5);
        put ('e',5);
        put ('F',5);
        put ('f',4);
        put ('G',5);
        put ('g',5);
        put ('H',5);
        put ('h',5);
        put ('I',3);
        put ('i',1);
        put ('J',5);
        put ('j',5);
        put ('K',5);
        put ('k',4);
        put ('L',5);
        put ('l',1);
        put ('M',5);
        put ('m',5);
        put ('N',5);
        put ('n',5);
        put ('O',5);
        put ('o',5);
        put ('P',5);
        put ('p',5);
        put ('Q',5);
        put ('q',5);
        put ('R',5);
        put ('r',5);
        put ('S',5);
        put ('s',5);
        put ('T',5);
        put ('t',4);
        put ('U',5);
        put ('u',5);
        put ('V',5);
        put ('v',5);
        put ('W',5);
        put ('w',5);
        put ('X',5);
        put ('x',5);
        put ('Y',5);
        put ('y',5);
        put ('Z',5);
        put ('z',5);
        put ('1',5);
        put ('2',5);
        put ('3',5);
        put ('4',5);
        put ('5',5);
        put ('6',5);
        put ('7',5);
        put ('8',5);
        put ('9',5);
        put ('0',5);
        put ('!',1);
        put ('@',6);
        put ('#',5);
        put ('$',5);
        put ('%',5);
        put ('^',5);
        put ('&',5);
        put ('*',5);
        put ('(',4);
        put (')',4);
        put ('-',5);
        put ('_',5);
        put ('+',5);
        put ('=',5);
        put ('{',4);
        put ('}',4);
        put ('[',3);
        put (']',3);
        put (':',1);
        put (';',1);
        put ('"',3);
        put ('\'',1);
        put ('<',4);
        put ('>',4);
        put ('?',5);
        put ('/',5);
        put ('\\',5);
        put ('|',1);
        put ('~',5);
        put ('`',2);
        put ('.',1);
        put (',',1);
        put (' ',3);
        put ('〶', 5);
        put ('㊀', 5);
        put ('㊅', 5);
        put ('㊇', 5);
        put ('㊂', 5);
        put ('º', 1);
        put ('■', 5);
        put ('★', 7);
        put ('☆', 7);
    }};


    public static List<String> fixLines(List<String> strings){

        int width = maxWidth(strings);
        System.out.println("width max: " + width);
        int line_length = 0;
        List<String> newStrings=new ArrayList<>();

        for (String s:strings){
            System.out.println(s);
            for (int k=0;k<s.toCharArray().length;k++){
                System.out.println(s.toCharArray()[k]);
                line_length += keys.get(s.toCharArray()[k]);
            }

            //Tolgo la qunatitá di valori &#xxxxxx che verranno tolti
            for (int j=1;j<s.split("&#").length;j++){

                line_length -= keys.get('&')+keys.get('#');
                for (int k=0;k<6;k++){
                    line_length -= keys.get(s.split("&#")[j].toCharArray()[k]);
                }
            }

            //Tolgo la qunatitá di valori &l che verranno tolti
            line_length -= (keys.get('&')+keys.get('l'))*(s.split("&l").length-1);


            //Tolgo la qunatitá di valori &l che verranno tolti
            line_length -= (keys.get('&')+keys.get('f'))*(s.split("&f").length-1);
            //Tolgo la qunatitá di valori &l che verranno tolti
            line_length -= (keys.get('&')+keys.get('r'))*(s.split("&r").length-1);

            int gap = (width - line_length)/2;
            int spacesNeeded = gap/keys.get(' ');
            System.out.println(spacesNeeded);
            for (int i=0;i<spacesNeeded;i++) s = " " + s;
            System.out.println(s);
            newStrings.add(s);
            line_length=0;
        }

        return newStrings;
    }




    public static int maxWidth(List<String> lines){
        int line_length = 0;
        int max_=0;
        for (String line:lines){
            if(line==null) continue;
            for (char c:line.toCharArray()){
                System.out.println(c);
                line_length += keys.get(c);
            }
            //Tolgo la qunatitá di valori &#xxxxxx che verranno tolti
            for (int j=1;j<line.split("&#").length;j++){

                line_length -= keys.get('&')+keys.get('#');
                for (int k=0;k<6;k++){
                    line_length -= keys.get(line.split("&#")[j].toCharArray()[k]);
                }
            }

            //Tolgo la qunatitá di valori &l che verranno tolti
            line_length -= (keys.get('&')+keys.get('l'))*(line.split("&l").length-1);
            if (line_length > max_){
                max_ = line_length;
            }
            line_length=0;
        }
        return max_;
    }


    // Nice little method to create a gui item with a custom name, and description
    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }



    public static void colorAndCenterLoreAndName(ItemStack itemStack) {

        ItemMeta meta = itemStack.getItemMeta();

        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();
        List<String> loreAndName = lore;

        loreAndName.add(displayName);



        List<String> newLoreAndName = new ArrayList<>();

        for (String s : Util.fixLines(loreAndName)) {
            newLoreAndName.add(ColorUtils.translateColorCodes(s));
        }


        meta.setDisplayName(newLoreAndName.get(newLoreAndName.size() - 1));
        newLoreAndName.remove(newLoreAndName.size() - 1);
        meta.setLore(newLoreAndName);

        itemStack.setItemMeta(meta);
    }

}

