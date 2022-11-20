package me.edoardo.test.database;

import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.job.alchemist.Research;
import me.edoardo.test.job.alchemist.presets.Ingredient;
import me.edoardo.test.job.alchemist.presets.Stage;
import me.edoardo.test.job.old_alchemist.Cauldron;
import me.edoardo.test.job.old_alchemist.presets.BuffType;
import me.edoardo.test.job.old_alchemist.presets.EssenceType;
import me.edoardo.test.job.old_alchemist.presets.Rarity;
import me.edoardo.test.job.blacksmith.presets.BlacksmithFurnace;
import me.edoardo.test.job.blacksmith.presets.MineralItem;
import me.edoardo.test.job.blacksmith.presets.MineralPurity;
import me.edoardo.test.job.blacksmith.presets.MineralType;
import me.edoardo.test.miniere.Mine;
import me.edoardo.test.miniere.MineLevel;
import me.edoardo.test.miniere.ancestral.Nature;
import me.edoardo.test.miniere.ancestral.SecretType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public final class Mysql {
    private final static String URL = "jdbc:mysql://localhost:3306/serverminecraft";
    private final static String USER = "root";
    private final static String PASSWORD = "edoardo2000";

//    private final static String INSERT = "INSERT INTO animals " + "(name) values (?)";
//    private final static String SELECT = "SELECT * from animals";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createConnection(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertData(String insert){
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setString(1, "dog");
            ps.execute();
            ps.setString(1, "cat");
            ps.execute();
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




    public static ResultSet getData(String select) {
        try {
            if (connection.isClosed()) createConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(select);
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void insertNewUser(String uuid) {


        //inserisce nello storage users i valori nome utente e password


        //Insert Storages
        int lastStorageId=-1;
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO storage values ()",Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ps.execute();
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            lastStorageId=rs.getInt(1);
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Get Storage Ids


        //Insert Player and Storages
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user (user_id,stone_storage_id,dirt_storage_id,wood_storage_id) values (?,?,?,?)");
            ps.setString(1, uuid);
            ps.setInt(2, lastStorageId-2);
            ps.setInt(3, lastStorageId-1);
            ps.setInt(4, lastStorageId);
            ps.execute();
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        //insert cauldron
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO cauldron (user_id) values (?)");
            ps.setString(1,uuid);
            ps.execute();
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        //insert Essence-Buff link
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO cauldron (user_id,type1,type2,type3,type4) values (?,?,?,?,?)");
            ps.setString(1,uuid);
            ps.setString(2,BuffType.ATTACK_SPEED.toString());
            ps.setString(3,BuffType.HP.toString());
            ps.setString(4,BuffType.DAMAGE.toString());
            ps.setString(5,BuffType.LIFESTEAL.toString());
            ps.execute();
        }catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isPlayerInDB(String uuid)  {

        ResultSet rs=getData(String.format("SELECT * FROM user WHERE user_id='%s'",uuid));

        try {
            if (connection.isClosed()) createConnection();

            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //Quantity [0]
    //Max Quantity [1]
    //ID [2]
    public static int[] getStorageInfo(Storage storage, Player player) {

        int[] storageInfo = new int[4];


        ResultSet rs=getData(String.format("SELECT * FROM user WHERE user_id='%s'",player.getUniqueId()));



        try {
            if (connection.isClosed()) createConnection();

            rs.next();

            String toQuery = "";

            switch (storage){
                case STONE: toQuery="stone_storage_id";
                break;
                case DIRT: toQuery="dirt_storage_id";
                break;
                case WOOD: toQuery="wood_storage_id";
                break;
            }

            int storage_id = rs.getInt(toQuery);

            ResultSet rs2=getData(String.format("SELECT * FROM storage WHERE storage_id=%s",storage_id));

            rs2.next();
            storageInfo[0]=rs2.getInt("quantity");
            storageInfo[1]=rs2.getInt("max_quantity");
            storageInfo[2]=rs2.getInt("storage_id");
            storageInfo[3]=rs2.getInt("level");

        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        return storageInfo;
    }

    public static void updateStorageInfo(int storageId,int quantity){

        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE storage SET quantity = (?) WHERE storage_id=(?);");
            ps.setInt(1, quantity);
            ps.setInt(2, storageId);
            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void levelUpStorage(int storageId, int newMaxQuantity, int newLevel) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE storage SET max_quantity = (?), level = (?) WHERE storage_id=(?);");
            ps.setInt(1, newMaxQuantity);
            ps.setInt(2, newLevel);
            ps.setInt(3, storageId);
            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static HashMap<EssenceType, BuffType> getEssenceTypeLinkedBuff(Player player) {

        ResultSet rs=getData(String.format("SELECT * FROM essence WHERE user_id='%s'",player.getUniqueId()));

        try {
            if (connection.isClosed()) createConnection();

            rs.next();

            HashMap<EssenceType, BuffType> linkedEssenceBuff = new HashMap<>();

            linkedEssenceBuff.put(EssenceType.TYPE1,BuffType.valueOf(rs.getString("type1")));

            linkedEssenceBuff.put(EssenceType.TYPE2,BuffType.valueOf(rs.getString("type2")));

            linkedEssenceBuff.put(EssenceType.TYPE3,BuffType.valueOf(rs.getString("type3")));

            linkedEssenceBuff.put(EssenceType.TYPE4,BuffType.valueOf(rs.getString("type4")));

            return linkedEssenceBuff;

        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static Cauldron getPlayerCauldron(Player player) {


        ResultSet rs=getData(String.format("SELECT * FROM cauldron WHERE user_id='%s'",player.getUniqueId()));

        try {
            if (connection.isClosed()) createConnection();
            rs.next();
            HashMap<EssenceType, Integer> essencesInCauldron = new HashMap<>();
            essencesInCauldron.put(EssenceType.TYPE1,rs.getInt("essence1"));
            essencesInCauldron.put(EssenceType.TYPE2,rs.getInt("essence2"));
            essencesInCauldron.put(EssenceType.TYPE3,rs.getInt("essence3"));
            essencesInCauldron.put(EssenceType.TYPE4,rs.getInt("essence4"));
            return new Cauldron(player,essencesInCauldron);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void pushCauldron(Player player, Cauldron cauldron) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE cauldron SET essence1 = (?), essence2 = (?), essence3 = (?), essence4 = (?) WHERE user_id=(?);");
            ps.setInt(1, cauldron.getEssenceInsideCalderon().get(EssenceType.TYPE1));
            ps.setInt(2, cauldron.getEssenceInsideCalderon().get(EssenceType.TYPE2));
            ps.setInt(3, cauldron.getEssenceInsideCalderon().get(EssenceType.TYPE3));
            ps.setInt(4, cauldron.getEssenceInsideCalderon().get(EssenceType.TYPE4));
            ps.setString(5, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void resetCauldron(Player player){
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE cauldron SET essence1 = (?), essence2 = (?), essence3 = (?), essence4 = (?) WHERE user_id=(?);");
            ps.setInt(1, 0);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setString(5, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BlacksmithFurnace getFurnace(int furnaceId) {


        ResultSet rs=getData(String.format("SELECT * FROM furnace WHERE furnace_id='%s'",furnaceId));

        try {
            if (connection.isClosed()) createConnection();
            rs.next();
            boolean isSmelting = rs.getBoolean("is_smelting");
            Timestamp start_time;
            int duration;
            int temperature;
            MineralItem mineralItem;
            if(isSmelting){
                JSONObject mineralItemJSON = new JSONObject(rs.getString("item_in_smelting"));
                start_time = rs.getTimestamp("start_time");
                duration = rs.getInt("duration_time");
                temperature = rs.getInt("temperature");

                MineralType mineralType = MineralType.valueOf(mineralItemJSON.getString("mineral_type"));
                String title = mineralItemJSON.getString("title");
                Rarity rarity = Rarity.valueOf(mineralItemJSON.getString("rarity"));
                MineralPurity purity = MineralPurity.valueOf(mineralItemJSON.getString("purity"));
                mineralItem = new MineralItem(mineralType,title,rarity,purity);
            }else {
                start_time = rs.getTimestamp("start_time");
                duration = rs.getInt("duration_time");
                temperature = rs.getInt("temperature");
                mineralItem = new MineralItem(MineralType.NONE,null,Rarity.COMMON,MineralPurity.STAR0);
            }

            return new BlacksmithFurnace(furnaceId,isSmelting,start_time,duration,temperature, mineralItem);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static List<BlacksmithFurnace> getFurnaces(Player player) {

        ResultSet rs=getData(String.format("SELECT * FROM furnace WHERE user_id='%s'",player.getUniqueId()));

        try {
            if (connection.isClosed()) createConnection();

            List<BlacksmithFurnace> furnaces = new ArrayList<>();

            while(rs.next()){
                int furnaceId = rs.getInt("furnace_id");
                boolean isSmelting = rs.getBoolean("is_smelting");
                Timestamp start_time;
                int duration;
                int temperature;
                MineralItem mineralItem;
                if(isSmelting){
                    JSONObject mineralItemJSON = new JSONObject(rs.getString("item_in_smelting"));
                    start_time = rs.getTimestamp("start_time");
                    duration = rs.getInt("duration_time");
                    temperature = rs.getInt("temperature");

                    MineralType mineralType = MineralType.valueOf(mineralItemJSON.getString("mineral_type"));
                    String title = mineralItemJSON.getString("title");
                    Rarity rarity = Rarity.valueOf(mineralItemJSON.getString("rarity"));
                    MineralPurity purity = MineralPurity.valueOf(mineralItemJSON.getString("purity"));
                    mineralItem = new MineralItem(mineralType,title,rarity,purity);
                }else {
                    start_time = rs.getTimestamp("start_time");
                    duration = rs.getInt("duration_time");
                    temperature = rs.getInt("temperature");
                    mineralItem = new MineralItem(MineralType.NONE,null,Rarity.COMMON,MineralPurity.STAR0);
                }





                furnaces.add(new BlacksmithFurnace(furnaceId,isSmelting,start_time,duration,temperature, mineralItem));
            }

            return furnaces;
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void updateFurnace(BlacksmithFurnace furnace) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE furnace SET is_smelting = (?), item_in_smelting = (?), start_time = (?), duration_time = (?), temperature = (?) WHERE furnace_id=(?);");
            ps.setBoolean(1, furnace.isSmelting());
            ps.setString(2, furnace.getItemSmeltingJSON());
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            ps.setInt(4, furnace.getDuration());
            ps.setInt(5, furnace.getTemperature());
            ps.setInt(6, furnace.getId());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //RESET FURNACE
    public static void resetFurnace(int furnaceId) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE furnace SET is_smelting = (?), item_in_smelting = (?), start_time = (?), duration_time = (?), temperature = (?) WHERE furnace_id=(?);");
            ps.setBoolean(1, false);
            ps.setNull(2, Types.VARCHAR);
            ps.setNull(3, Types.TIMESTAMP);
            ps.setNull(4, Types.INTEGER);
            ps.setNull(5, Types.INTEGER);
            ps.setInt(6, furnaceId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static int[] getMaxQuality(Player player, MineralType mineralType) {
        System.out.println(mineralType);
        ResultSet rs=getData(String.format("SELECT * FROM perfect_quality WHERE user_id='%s' AND mineral_type='%s'",player.getUniqueId(),mineralType.toString()));

        try {
            if (connection.isClosed()) createConnection();
            rs.next();
            int [] durTemp = new int[]{
                    rs.getInt("perfect_duration"),
                    rs.getInt("perfect_temperature")
            };
            return durTemp;
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }


    public static Location createBase(Player player){
        //Prendi le ultime coordinate dell'ultima base inserita e secondo lo schema calcolare nuove coordinate
        Location lastBaseSpawn = new Location(
                Bukkit.getWorld("base_world"),
                0,
                0,
                0
        );
        ResultSet rs=getData(String.format("SELECT * FROM base ORDER BY base_id DESC",player.getUniqueId()));
        try {
            if (connection.isClosed()) createConnection();
            rs.next();
            lastBaseSpawn = new Location(
                    Bukkit.getWorld("base_world"),
                    rs.getInt("spawn_x"),
                    rs.getInt("spawn_y"),
                    rs.getInt("spawn_z")
            );
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        int x=lastBaseSpawn.getBlockX();
        int z=lastBaseSpawn.getBlockZ();

        org.bukkit.util.Vector offset =new Vector(0,0,0);

        if (Math.abs(x)==Math.abs(z)){
            if (x>=0 && z>=0) offset=new Vector(-1,0,0);
            if (x>=0 && z<0) offset=new Vector( 1,0,0);
            if (x<0 && z>=0) offset=new Vector( 0,0,-1);
            if (x<0 && z<0) offset=new Vector( 1,0,0);
        }else{
            if(Math.abs(x)>Math.abs(z)){
                if (x>0) offset=new Vector( 0,0,1);
                if (x<0) offset=new Vector( 0,0,-1);
            }else{
                if (z>0) offset=new Vector( -1,0,0);
                if (z<0) offset=new Vector( 1,0,0);
            }
        }

        System.out.println("offset: " + offset);

        Location newBaseSpawn = lastBaseSpawn.clone().add(offset.multiply(250));
        System.out.println("offset: " + newBaseSpawn.toVector());
        //Controllare se nessuna base e a quelle coordinate
        ResultSet rs2=getData(String.format("SELECT * FROM base WHERE spawn_x='%s' AND spawn_y='%s'",newBaseSpawn.getBlockX(),newBaseSpawn.getBlockZ()));
        try {
            if (connection.isClosed()) createConnection();
            if(rs2.next()) {
                System.out.println("BASE GIA PRESENTE A QUESTE COORDINATE");
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }



        //Inserisci nel database
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO base (player_uuid,spawn_x,spawn_y,spawn_z) values (?,?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, newBaseSpawn.getBlockX());
            ps.setInt(3, newBaseSpawn.getBlockY());
            ps.setInt(4, newBaseSpawn.getBlockZ());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newBaseSpawn;
    }

    public static boolean doesPlayerHasBase(Player player) {
        ResultSet rs=getData(String.format("SELECT * FROM base WHERE player_uuid='%s'",player.getUniqueId()));
        try {
            if (connection.isClosed()) createConnection();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static Location getPlayerBase(Player player) {
        ResultSet rs=getData(String.format("SELECT * FROM base WHERE player_uuid='%s'",player.getUniqueId()));
        try {
            if (connection.isClosed()) createConnection();

            rs.next();
            return new Location(Bukkit.getWorld("base_world"),rs.getInt("spawn_x"),rs.getInt("spawn_y"),rs.getInt("spawn_z"));
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //INSERT NEW MINE IN DATABASE
    public static void addNewMine(Mine mine) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO mine (player_uuid,discovered_floors,level,nature_type,secret_type,gui_position,tunnel_position) values (?,?,?,?,?,?,?)");
            ps.setString(1, mine.getPlayer().getUniqueId().toString());
            ps.setInt(2, mine.getFloors());
            ps.setInt(3, mine.getLevel().getLevelInt());
            ps.setString(4, mine.getNature().toString());
            ps.setString(5, mine.getSecretType().toString());
            ps.setInt(6, mine.getGuiPosition());
            ps.setInt(7, mine.getTunnelPosition());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    //GET ALL PLAYER'S MINE OF A CERTAIN LEVEL
    public static List<Mine> getSpecificLevelMines(Player player, MineLevel level){
        ResultSet rs=getData(String.format("SELECT * FROM mine WHERE player_uuid='%s' AND level='%s'",player.getUniqueId(),level.getLevelInt()));
        List<Mine> mines = new ArrayList<>();
        try {
            if (connection.isClosed()) createConnection();
            while(rs.next()){
                int floors = rs.getInt("discovered_floors");
                int guiPosition = rs.getInt("gui_position");
                int tunnelPosition = rs.getInt("tunnel_position");
                Nature nature = Nature.valueOf(rs.getString("nature_type"));
                SecretType secretType = SecretType.valueOf(rs.getString("secret_type"));
                mines.add(new Mine(player,level,floors, guiPosition, tunnelPosition, nature,secretType));
            }} catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return mines;
    }

    public static void incrementFloor(Mine mineData) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE mine SET discovered_floors = (?) WHERE player_uuid=(?) AND level=(?) AND gui_position=(?);");
            ps.setInt(1, mineData.getFloors()+1);
            ps.setString(2, mineData.getPlayer().getUniqueId().toString());
            ps.setInt(3, mineData.getLevel().getLevelInt());
            ps.setInt(4, mineData.getGuiPosition());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Stage getCurrentIngredientStage(Player player, Ingredient ingredient) {
        ResultSet rs=getData(String.format("SELECT * FROM research WHERE player_uuid='%s' AND ingredient='%s' ORDER BY stage DESC",player.getUniqueId(),ingredient.toString()));
        try {
            if (connection.isClosed()) createConnection();
            if(!rs.next()) return ingredient.getStages().get(0);
            if(rs.getObject("isSuccessful")==null) ingredient.getStages().get(rs.getInt("stage"));
            return ingredient.getStages().get(rs.getInt("stage")+1);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static List<Research> getResearchesOfIngredient(Player player, Ingredient ingredient) {
        ResultSet rs=getData(String.format("SELECT * FROM research WHERE player_uuid='%s' AND ingredient='%s'",player.getUniqueId(),ingredient.toString()));
        List<Research> research = new ArrayList<>();
        try {
            if (connection.isClosed()) createConnection();
            while(rs.next()){

                PreparedStatement ps = connection.prepareStatement("INSERT INTO `serverminecraft`.`research` (`player_uuid`, `ingredient`, `stage`, `combination`, `start_timestamp`, `finish_timestamp`, `isSuccessful`) VALUES (?, ?, ?, ?, ?, ?, ?);");

                Stage stage = ingredient.getStages().get(rs.getInt("stage"));
                String stringCombination = rs.getString("combination");

                List<Integer> combination = new ArrayList<>();

                for(char a : stringCombination.toCharArray()){
                    combination.add(Integer.parseInt(String.valueOf(a)));
                }

                long startTimestamp = rs.getLong("start_timestamp");
                long finishTimestamp = rs.getLong("finish_timestamp");
                Boolean isSuccessful= rs.getBoolean("isSuccessful");

                research.add(new Research(player,ingredient,stage,combination,startTimestamp,finishTimestamp,isSuccessful));
            }} catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return research;
    }

    public static void pushResearch(Research research) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `serverminecraft`.`research` (`player_uuid`, `ingredient`, `stage`, `combination`, `start_timestamp`, `finish_timestamp`, `isSuccessful`) VALUES (?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, research.getPlayer().getUniqueId().toString());
            ps.setString(2, research.getIngredient().toString());
            ps.setInt(3, research.getStage().getStageNumber());
            ps.setString(4, research.getCombinationToString());
            ps.setLong(5, research.getStartTimestamp());
            if(research.getFinishTimestamp()==0) ps.setNull(6, Types.BIGINT);
            else ps.setLong(6, research.getFinishTimestamp());
            if(research.isSuccessful()==null) ps.setNull(7, Types.BOOLEAN);
            else ps.setBoolean(7, research.isSuccessful());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void updateResearch(Research research){
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE research SET finish_timestamp = (?) , isSuccessful = (?) WHERE player_uuid=(?) AND ingredient=(?) AND stage=(?) AND start_timestamp=(?);");
            ps.setLong(1, research.getFinishTimestamp());
            ps.setBoolean(2, research.isSuccessful());
            ps.setString(3, research.getPlayer().getUniqueId().toString());
            ps.setString(4, research.getIngredient().toString());
            ps.setInt(5, research.getStage().getStageNumber());
            ps.setLong(6, research.getStartTimestamp());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
