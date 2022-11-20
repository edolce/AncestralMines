package me.edoardo.test;


import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilHolo {
    public UtilHolo(String name, Hologram hologram) {
        this.name = name;
        this.hologram = hologram;
    }

    private final String name;
    private final Hologram hologram;

    private String getStringLocation(){
        return hologram.getLocation().getWorld().getName() +
                ", " +
                hologram.getLocation().getX() +
                ", " +
                hologram.getLocation().getY() +
                ", " +
                hologram.getLocation().getZ();
    }

    private List<String> getLines(){
        List<String> lines = new ArrayList<>();


        //I know it kinda illegal but idc
        try {
            for (int i=0;true;i++){
                lines.add(hologram.getLine(i).toString().replace("CraftTextLine [text=","").replace("]",""));
            }
        }catch (IndexOutOfBoundsException exception){
            return lines;
        }
    }

    public void writeValue(){

        File file = new File( "plugins\\test\\storageHologramsDatabase.yml");

        FileConfiguration holoStorage = YamlConfiguration.loadConfiguration(file);

        System.out.println(file.exists());
            try {

                holoStorage.createSection(name);
                holoStorage.set(name+".location", getStringLocation());
                holoStorage.createSection(name+".lines");
                holoStorage.set(name+".lines",getLines());

                holoStorage.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

    }
}