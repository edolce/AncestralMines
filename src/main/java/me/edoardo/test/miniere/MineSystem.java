package me.edoardo.test.miniere;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent;
import io.lumine.mythic.core.drops.Drop;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.edoardo.test.ColorUtils;
import me.edoardo.test.Test;
import me.edoardo.test.Util;
import me.edoardo.test.base.Base;
import me.edoardo.test.base.presets.Storage;
import me.edoardo.test.custom_blocks.CustomBlock;
import me.edoardo.test.database.Mysql;
import me.edoardo.test.job.blacksmith.presets.MineralBlock;
import me.edoardo.test.miniere.ancestral.Ancestral;
import me.edoardo.test.miniere.ancestral.Nature;
import me.edoardo.test.miniere.ancestral.SecretType;
import me.edoardo.test.presets.Bag;
import me.edoardo.test.presets.Pickaxe;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MineSystem implements Listener, CommandExecutor {

    private final Random randomGen = new Random();

    private final List<Room> rooms=new ArrayList<>();

    private final HashMap<String,String> outPutInputLink= new HashMap<>();
    private final HashMap<String,Integer[]> relativeConnectionXY= new HashMap<>();

    private final Player player;

    //Scoreboard data
    private final Scoreboard scoreboard;
    private int obtainedResources= 0;
    private int brokenMineralQuantity= 0;
    private int ancestralRemaining= 0;

    //COSTANTI MODIFICABILI
    private final int minRooms = 7;
    private final int maxRooms = 12;

    private final MineBlockSet mineBlockSet;
    private Location origin;
    private final World mineWorld= Bukkit.getWorld("mine_world");

    //Game is finished
    private boolean isGameFinished = false;


    //List Of Item Obtained from the dungeon
    private List<ItemStack> items = new ArrayList<>();

    //Mine object that contains all the info of the current mine (Contains all variable data)
    private Mine mineData;

    //IN V 0.2 aggiungere livelli e tipo selezionabili
    private int difficultyLevel = 0;
    private String type = "Empirico";
    private int timeMinutes = 30;
    private int timeSeconds = 15;

    //MATERIAL OF SPAWN POINT
    private final Material spawnPointMaterial = Material.BEACON;
    //Possible Spawn Point For Ancestral
    private final List<Vector> ancestralSpawnPoints = new ArrayList<>();
    //Spawned Ancestral list
    private final List<ActiveMob> spawnedAncestrals = new ArrayList<>();


    public MineSystem(Player player, Mine mineData){
        this.mineBlockSet = mineData.getMineBlockSet();
        this.player = player;
        //IN V 0.2 aggiungere livelli e tipo
        this.mineData = mineData;
        scoreboard= createScoreboard();
    }

    public void createPhysicalMine(){


        outPutInputLink.put("N","S");
        outPutInputLink.put("S","N");
        outPutInputLink.put("W","E");
        outPutInputLink.put("E","W");


        relativeConnectionXY.put("N",new Integer[]{1,0});
        relativeConnectionXY.put("S",new Integer[]{-1,0});
        relativeConnectionXY.put("W",new Integer[]{0,-1});
        relativeConnectionXY.put("E",new Integer[]{0,1});



        System.out.println("\n\n[NUOVA MINIERA] - Creazione nuova miniera");

        createBasicPath();


        System.out.printf("Sono state create un totale di %s stanze",rooms.size());
        for (Room room:rooms){

            StringBuilder output= new StringBuilder();
            output.append("[FREE]:");
            for(String face:room.getFreeConnections()){
                output.append(face).append(" ");
            }
            System.out.printf("[%s;%s]: %s | %s\n",room.getxPos(),room.getyPos(),room.getRoomType(),output);
        }


        buildTheMine(origin);
    }
    private void createBasicPath(){
        List<Room> queue = new ArrayList<>();
        Room spawn = new Room(RoomType.SPAWN, 0, 0);
        queue.add(spawn);
        this.rooms.add(spawn);

        List<RoomType> possibleRooms = new ArrayList<>();

        while (!queue.isEmpty()) {
            List<Room> roomsToRemove = new ArrayList<>();
            List<Room> roomsToAdd = new ArrayList<>();
            for (Room room : queue) {


                if (room.isCompleted()) {
                    roomsToRemove.add(room);
                    continue;
                }
                String sideToLink = room.getFreeConnections().get(0);


                //Creazione Lista Di Possibili link secondo la STRUTTURA delle altre rooms
                //System.out.println("[INIZIO RICERCA MATCH PER]: "+room.getRoomType());
                for (RoomType rType : RoomType.values()) {

                    //Se è SPAWN Rifiuta a prescindere
                    //Controllo che la roomtype da aggiungere sia collegabile quindi che l'input e output siano linkabili
                    if (Arrays.asList(rType.getPossibleConnections()).contains(outPutInputLink.get(sideToLink)) & !(rType == RoomType.SPAWN)) {

                        //Controllo se effettivamente c'è spazio per qulla room e le sue future room collegate
                        if (rType.isPossibleToAdd(this.rooms, rType, sideToLink, outPutInputLink.get(sideToLink), room)) {
                            possibleRooms.add(rType);
                        }else {
                        }
                    }else {
                    }


                }

                //COSTANTE MIN CONTROLL
                //Controllo quanti impasse ci sono
                int nImpasse=0;
                for(RoomType roomType:possibleRooms){
                    if(roomType.isImpasse()){
                      nImpasse +=1;
                    }
                }

                //Se esistono altre possibili type room e non é ancora stato raggiunto il minimo di stanze togli le impasse
                if(nImpasse<possibleRooms.size() & this.rooms.size()<minRooms){
                    possibleRooms.removeIf(RoomType::isImpasse);
                }

                //COSTANTE MAX CONTROLL
                //Controllo se esistono degli impasse come possibilitá, se esistono e il max é stato raggiunto togli tutto il resto
                if(nImpasse>0 & this.rooms.size()>maxRooms){
                    possibleRooms.removeIf(RoomType::isNotImpasse);
                }


                //Decisione ROOM a seconda delle COSTANTI
                Random rand = new Random();
                //System.out.println(possibleRooms);
                RoomType newRoomType = possibleRooms.get(rand.nextInt(possibleRooms.size()));


                int[] newRoomXYPos = RoomType.getLinkedXYPos(room, sideToLink);

                assert newRoomXYPos != null;
                Room newRoom = new Room(newRoomType, newRoomXYPos[0], newRoomXYPos[1]);

                roomsToAdd.add(newRoom);
                this.rooms.add(newRoom);
                System.out.printf("\n[NORMAL-ROOM]Aggiunta room %s in [%s,%s]\n",newRoomType,newRoomXYPos[0],newRoomXYPos[1]);
                //System.out.printf("Si sta cercando di aggiungere la room %s:[lato %s]  alla Nuova Room %s:[lato %s]\n", room.getRoomType(), sideToLink, newRoomType, outPutInputLink.get(sideToLink));
                room.addConnections(newRoom, sideToLink, outPutInputLink.get(sideToLink));
                room.removeFreeConnection(sideToLink);

                newRoom.addConnections(room, outPutInputLink.get(sideToLink), sideToLink);
                newRoom.removeFreeConnection(outPutInputLink.get(sideToLink));

                if (room.isCompleted()) roomsToRemove.add(room);
                possibleRooms.clear();

                //Aggiungere check per vedere se esistono possibili blocchi futuri e anticiparli nel caso
                isOverlappingPossible();
            }
            queue.addAll(roomsToAdd);
            queue.removeAll(roomsToRemove);

        }
    }
    private boolean isOverlappingPossible() {

        HashMap<int[],Room> ghostRooms = new HashMap<>();

        //Add "Ghost Rooms" to a list if already exists overlapping will occur so, there will be a fix.
        System.out.println("[CONTROLLO-OVP]Controllo se e presente un overlapping");
        System.out.printf("[CONTROLLO-OVP]rooms inserite: %s\n",this.rooms.size());
        scoreboard.getTeam("roomNr").setPrefix(String.format(ColorUtils.translateColorCodes("Stanze Nella Miniera: %s"),rooms.size()));
        for (Room room:this.rooms){
            for(String freeConnection:room.getFreeConnections()){
                int relX=relativeConnectionXY.get(freeConnection)[0];
                int relY=relativeConnectionXY.get(freeConnection)[1];

                int[] ghostRoom = new int[]{room.getxPos()+relX,room.getyPos()+relY};

//                for (int[] coords:ghostRooms.keySet()){
//                    if (coords[0]==ghostRoom[0]&coords[1]==ghostRoom[1]){
//                        //SOLVE OVERLAPPING
//                        //FIND A BLOCK THAT WILL SOLVE IT
//                        isFix(room,ghostRooms.get(ghostRoom),ghostRoom);
//                        return true;
//                    }else{
//                        ghostRooms.put(ghostRoom,room);
//                    }
//                }
                Room room1=isGhostAlreadyPresent(ghostRooms,ghostRoom);
                if(room1!=null){
                    //SOLVE OVERLAPPING
                    //FIND A BLOCK THAT WILL SOLVE IT
                    isFix(room,room1,ghostRoom);
                    return true;
                }else{
                    ghostRooms.put(ghostRoom,room);
                }
            }


        }
        StringBuilder output= new StringBuilder();
        for(int[] coords:ghostRooms.keySet()){
            output.append(String.format("[%s,%s] ", coords[0], coords[1]));
        }
        System.out.printf("[CONTROLLO-OVP]GhostRooms: %s\n",output);

        return false;
    }
    public Room isGhostAlreadyPresent(HashMap<int[], Room> ghostRooms, int[] ghostRoom){
        for (int[] coords:ghostRooms.keySet()){
            if (coords[0]==ghostRoom[0]&coords[1]==ghostRoom[1]){
                System.out.printf("[CONTROLLO-OVP]Overlapping found at: [%s,%s]\n",coords[0],coords[1]);
                return ghostRooms.get(coords);
            }
        }
        return null;
    }
    private void isFix(Room room0, Room room1,int[] ghostRoom) {
        //Find connection of room0
        int posX0=room0.getxPos()-ghostRoom[0];
        int posY0=room0.getyPos()-ghostRoom[1];

        String connection0 = null;

        for(String connection:relativeConnectionXY.keySet()){
            Integer[] posXY=relativeConnectionXY.get(connection);
            if(posXY[0]==posX0 & posXY[1]==posY0){
                connection0=connection;
                break;
            }
        }
        //Find connection of room1
        int posX1=room1.getxPos()-ghostRoom[0];
        int posY1=room1.getyPos()-ghostRoom[1];

        String connection1= null;

        for(String connection:relativeConnectionXY.keySet()){
            Integer[] posXY=relativeConnectionXY.get(connection);
            if(posXY[0]==posX1 & posXY[1]==posY1){
                connection1=connection;
                break;
            }
        }

        for (RoomType rType:RoomType.values()){
            if(rType.getPossibleConnections().length==2){
                if(Arrays.asList(rType.getPossibleConnections()).contains(connection0)&Arrays.asList(rType.getPossibleConnections()).contains(connection1)){
                    Room fixRoom = new Room(rType,ghostRoom[0],ghostRoom[1]);

                    System.out.println(connection0 + " " + connection1);
                    fixRoom.addConnections(room0,connection0,outPutInputLink.get(connection0));
                    fixRoom.addConnections(room1,connection1,outPutInputLink.get(connection1));
                    fixRoom.removeFreeConnection(connection0);
                    fixRoom.removeFreeConnection(connection1);

                    for (Room room:rooms){
                        if(room.compareXY(room0.getxPos(),room0.getyPos())){
                            room.addConnections(fixRoom,outPutInputLink.get(connection0),connection0);
                            room.removeFreeConnection(outPutInputLink.get(connection0));
                        }
                    }
                    for (Room room:rooms){
                        if(room.compareXY(room1.getxPos(),room1.getyPos())){
                            room.addConnections(fixRoom,outPutInputLink.get(connection1),connection1);
                            room.removeFreeConnection(outPutInputLink.get(connection1));
                        }
                    }

                    System.out.printf("[CONTROLLO-OVP]Aggiunta room %s in [%s,%s]\n",fixRoom.getRoomType(),fixRoom.getxPos(),fixRoom.getyPos());



                    this.rooms.add(fixRoom);
                    return;
                }
            }
        }

    }
    private void buildTheMine(Location origin){
        for (Room room:this.rooms){
            int xPos=room.getxPos();
            int yPos=room.getyPos();


            Location tempOrigin = origin.clone();

//            System.out.printf("[COPIO-INCOLLO]Copio da [%s;%s;%s] a [%s;%s;%s], e incollo in [%s;%s;%s]\n"
//                    ,room.getRoomType().getNW_DOWN().getBlockX()
//                    ,room.getRoomType().getNW_DOWN().getBlockY()
//                    ,room.getRoomType().getNW_DOWN().getBlockZ()
//
//                    ,room.getRoomType().getSE_UP().getBlockX()
//                    ,room.getRoomType().getSE_UP().getBlockY()
//                    ,room.getRoomType().getSE_UP().getBlockZ()
//
//                    ,tempOrigin.getBlockX()
//                    ,tempOrigin.getBlockY()
//                    ,tempOrigin.getBlockZ()
//
//                    );
            System.out.printf("[COOPING] Sto copiando la room: %s",room.getRoomType().toString());
            copyPasteRegion(room.getRoomType().getNW_DOWN(),room.getRoomType().getSE_UP(), tempOrigin.add(yPos*(21),0,-xPos*(21)));
        }
    }
    private void copyPasteRegion(Location NW_DOWN,Location SE_UP, Location NW_DOWN_NEW){



        int xWidth=Math.abs(NW_DOWN.getBlockX()-SE_UP.getBlockX());
        int yWidth=Math.abs(NW_DOWN.getBlockY()-SE_UP.getBlockY());
        int zWidth=Math.abs(NW_DOWN.getBlockZ()-SE_UP.getBlockZ());



        //Materiali dei blocchi non da cambiare
        Material[][][] copiedRegion = new Material[xWidth+1][yWidth+1][zWidth+1];
        //Materiali dei blocchi da cambiare
        HashMap<Material,List<Vector>> templateCopiedRegion = new HashMap<>();


        //Inizializzo la hashmap
        for(Material materialToReplace:mineBlockSet.getWholeMap().keySet()) {
         templateCopiedRegion.put(materialToReplace,new ArrayList<>());
        }

        //templateCopiedRegion.forEach((key, value) -> System.out.println(key + " " + value));


        //COPY REGION
        for(int x=0;x<xWidth+1;x++){
            for(int y=0;y<yWidth+1;y++){
                for(int z=0;z<xWidth+1;z++){
                    Material materialToCopy = Bukkit.getWorld("main").getBlockAt(NW_DOWN.clone().add(x,y,z)).getType();
                    //Se il materiale e un materiale da template (terracotta) allora aggiungilo alla hashmap list
                    if(templateCopiedRegion.containsKey(materialToCopy)) {
                        templateCopiedRegion.get(materialToCopy).add(new Vector(x,y,z));
                        copiedRegion[x][y][z] = Material.AIR;
                    }else {
                        if(materialToCopy==spawnPointMaterial){
                            ancestralSpawnPoints.add(NW_DOWN_NEW.clone().add(new Vector(x,y,z)).toVector());
                            copiedRegion[x][y][z] = Material.AIR;
                        }else copiedRegion[x][y][z] = materialToCopy;
                    }
                }
            }
        }

        //templateCopiedRegion.forEach((key, value) -> System.out.println(key + " " + value));







        //Place every copiedRegion Block
        for(int x=0;x<xWidth+1;x++){
            for(int y=0;y<yWidth+1;y++){
                for(int z=0;z<xWidth+1;z++){
                    boolean placed=false;
                    Block block= mineWorld.getBlockAt(NW_DOWN_NEW.clone().add(x,y,z));


//                    // @TODO FIX CRASH
//                    for(MiningBlockSetSingle setSingle:miningBlockSet){
//                        if(setSingle.getTemplateMaterial()==copiedRegion[x][y][z]){
//                            MiningBlock randomChoice = setSingle.randomChoice(rand);
//                            System.out.println(randomChoice.getCustomBlock());
//                            randomChoice.getCustomBlock().placeBlock(block);
//                            placed=true;
//                        }
//                    }


                    if(!placed) block.setType(copiedRegion[x][y][z]);
                }
            }
        }
        //Place every new substitute to template Blocks
        for (Material matToSet: templateCopiedRegion.keySet()){

            //Shuffle templates vector
            Collections.shuffle(templateCopiedRegion.get(matToSet));
            System.out.printf("[SHUFFLE] Lista vettori shuffled: %s\n",matToSet);

            for(Material matToReplace:mineBlockSet.getWholeMap().keySet()){
                if(matToReplace!=matToSet) continue;
                HashMap<Vector, MiningBlock> miningBlockHashMap= mineBlockSet.fakeRandomChoice(templateCopiedRegion.get(matToSet),matToReplace);
                System.out.printf("[FAKE-CHOICE] Fake-Choice effettuata per: %s\n",matToReplace);
                for (Vector vector: miningBlockHashMap.keySet()){
                    MiningBlock blockToPlace = miningBlockHashMap.get(vector);
                    Block block= mineWorld.getBlockAt(NW_DOWN_NEW.clone().add(vector));
                    blockToPlace.getCustomBlock().placeBlock(block);
                }
            }


        }
    }
    private void spawnAncestrals(){
        //Spawn Ancestrals
        Collections.shuffle(ancestralSpawnPoints);
        for(int i=0;i<mineData.getLevel().getAncestralsQuantity();i++){
            ActiveMob activeMob = mineData.getLevel().getAncestral().spawnAtLoc(ancestralSpawnPoints.get(i).clone().add(new Vector(0,1,0)).toLocation(mineWorld));
            System.out.println(ancestralSpawnPoints.get(i).clone().add(new Vector(0,1,0)));
            spawnedAncestrals.add(activeMob);
            ancestralRemaining++;
        }
    }


//    //MENU
//    //MENU
//    private String menuTitle = "Miniera";
//    private Inventory gui = Bukkit.createInventory(null, 27,menuTitle );
//
//    public Inventory getMineGui(){
//
//        while(gui.firstEmpty()!=-1) gui.addItem(Util.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,"",""));
//
//
//
//        //Current Mine Level
//        gui.setItem(10,Util.createGuiItem(Material.CHEST,
//                ColorUtils.translateColorCodes("&f&bSelect Mine Level"),
//                ColorUtils.translateColorCodes("&f&bWIP")));
//
//
//        //Mine Description
//        gui.setItem(4,Util.createGuiItem(Material.FURNACE_MINECART,
//                ColorUtils.translateColorCodes("&f&bMINE INFO"),
//                ColorUtils.translateColorCodes(String.format("&f&bLevel: %s",difficultyLevel)),
//                ColorUtils.translateColorCodes(String.format("&f&bType: %s",type)),
//                ColorUtils.translateColorCodes(String.format("&f&bTime: %s minutes",timeMinutes)),
//                ColorUtils.translateColorCodes("&f&bParty: &4&fDISATTIVATO"),
//                ColorUtils.translateColorCodes(""),
//                ColorUtils.translateColorCodes(""),
//                ColorUtils.translateColorCodes(""),
//                ColorUtils.translateColorCodes(""),
//                ColorUtils.translateColorCodes("")
//        ));
//
//        //Join Mine
//        gui.setItem(13,Util.createGuiItem(Material.LODESTONE,
//                ColorUtils.translateColorCodes("&f&bClick To Join Mine"),
//                ColorUtils.translateColorCodes("")));
//
//
//
//        //Select Mine
//        gui.setItem(16, Util.createGuiItem(Material.CAMPFIRE,
//                ColorUtils.translateColorCodes("&f&bSelect Mine Type"),
//                ColorUtils.translateColorCodes("&f&bWIP")));
//
//
//
//        return gui;
//    }
//
//    //MINE MENU
//    @EventHandler
//    public void onClickedInventory(InventoryClickEvent event){
//        if(!event.getView().getTitle().equals(menuTitle)) return;
//        event.setCancelled(true);
//        if(event.getCurrentItem().getType().equals(Material.LODESTONE)){
//            searchPlaceAndStart();
//            //Test.getInstance().getActiveMines();
//        }
//    }


    public void searchPlaceAndStart(){
        //Search available location @TODO TO REWOK AND CREATE LIST OF REGION
        Random rand = new Random();
        int x_random = rand.nextInt(20000) - 10000;
        int z_random = rand.nextInt(20000) - 10000;
        this.origin = new Location(mineWorld,x_random,56,z_random);
        createPhysicalMine();
        player.teleport(origin.clone().add(10.5,10,13.5));
        spawnAncestrals();
        startMineGame();
    }

    //MINE GAME
    public void startMineGame(){
        player.setScoreboard(scoreboard);

        Test.getInstance().getBlockInMiningState().put(player,new HashMap<>());

        Bukkit.getScheduler().runTaskTimer(Test.getInstance(), (task) -> {
            if(isGameFinished) task.cancel();
            if(timeSeconds==0){
                task.cancel();
                finishMiniGame();
            }else{
                scoreboard.getTeam("remTime").setPrefix(
                        String.format(ColorUtils.translateColorCodes("Tempo Rimasto: %s"),timeSeconds--)
                );
            }
        }, 20, 20);


    }
    //FINISCE - VIVO (OPPURE /exit)
    public void finishMiniGame(){
        isGameFinished=true;
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Base basePlayer = new Base(player);
        player.teleport(basePlayer.getSpawn());
        player.sendMessage("La miniera é collassata, sei stato riportato alla tua base!");
        Test.getInstance().getBlockInMiningState().remove(player);
        HandlerList.unregisterAll(this);
        //SE NON HAI USATO IL COMANDO ROLLA LA PROBABILITA DI SBLOCCARE NUOVI LIVELLI/MINIERE/FLOORS
        if(timeSeconds==0){
            double randomDouble = randomGen.nextDouble();
            if(randomDouble<mineData.getFindNewFloorProbability()){
                //Trovata nuovo piano
                Mysql.incrementFloor(mineData);
                player.sendMessage("HAI SCOPERTO UN NUOVO PIANO DELLA MINIERA, DAI UN'OCCHIATA AI NUOVI BONUS!");
            }
            if(randomDouble<mineData.getFindNewMineProbability()){
                //Trovata nuova miniera

                //Creazione Mine
                int[] guiPositions = mineData.getGuiPositions(randomGen);
                Mine mine=new Mine(player,mineData.getLevel(),1,guiPositions[0],guiPositions[1], Nature.getRandom(randomGen),SecretType.getRandom(randomGen));
                player.sendMessage("HAI SCOPERTO UNA NUOVA MINIERA!!!");

                //PushMine In database
                Mysql.addNewMine(mine);
            }
            if(randomDouble<mineData.getFindNextLevelProbability()){
                //Trovato nuovo livello
                //Creazione Mine
                MineLevel nextMineLevel = MineLevel.valueOf("LEVEL"+(mineData.getLevel().getLevelInt()+1));
                Mine mine=new Mine(player,nextMineLevel,1,7,-1, Nature.getRandom(randomGen),SecretType.getRandom(randomGen));
                player.sendMessage("HAI SCOPERTO UNA NUOVO LIVELLO!!!!!");
                //PushMine In database
                Mysql.addNewMine(mine);
            }
        }
    }

    //FINISCE - MORTE
    @EventHandler
    public void isPlayerDeadInMine(PlayerDeathEvent event){
        isGameFinished=true;
        if(!event.getEntity().getWorld().equals(Bukkit.getWorld("mine_world"))) return;
        event.setKeepInventory(true);
        player.sendMessage("Sei Morto e hai perso le risorse trovate in miniera!");
        player.sendMessage(String.format("Risorse perse: %s Stone",obtainedResources));
        if (items.size()!=0){
            player.sendMessage("Hai Perso anche i seguenti oggetti in miniera:");
            for (ItemStack item : items){
                player.sendMessage(String.format("Hai perso %s x%s",item.getType(),item.getAmount()));
                player.getInventory().remove(item);
                Test.getInstance().getBlockInMiningState().remove(player);
            }
        }
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void isPlayerRespawning(PlayerRespawnEvent event){
        if(!event.getPlayer().getWorld().equals(Bukkit.getWorld("mine_world"))) return;
        Base basePlayer = new Base(player);
        event.setRespawnLocation(basePlayer.getSpawn());
    }

    //CONTAGGIO RISORSE OTTENUTE DALLA MINIERA
    // @TODO AGGIUNGERE HASHMAP CHE CONTROLLA NEL CASO GLI ITEM SI STACCKASSERO TRA DI LORO RENDENDO IL Invetory.remove(Itemstack) impossibile da detectare
    @EventHandler
    public void playerPickUpEvent(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!event.getEntity().getWorld().equals(Bukkit.getWorld("mine_world"))) return;
        ItemStack pickedUpItem = event.getItem().getItemStack();
        items.add(pickedUpItem);
    }

    @EventHandler
    public void playerDropEvent(PlayerDropItemEvent event){
        if(!event.getPlayer().getWorld().equals(Bukkit.getWorld("mine_world"))) return;
        ItemStack pickedUpItem = event.getItemDrop().getItemStack();
        items.remove(pickedUpItem);
    }

    //@TODO AUMENTA IL COUNTER DI RISORE OTTENUTE


    //SCOREBOARD
    public Scoreboard createScoreboard() {
//                Score roomExploredScore = objective.getScore(ColorUtils.translateColorCodes("Stanze Esplorate: (WIP V0.2)"));
//                Score fossilsPresentScore = objective.getScore(ColorUtils.translateColorCodes("Ancestrali Presenti: (WIP V0.2)"));

        Scoreboard board =  Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("dummy", "title","rerewewer");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("MINE INFO");

        HashMap<String,String> infoXtitles = new HashMap<>();

        infoXtitles.put(
                "remTime",
                String.format(ColorUtils.translateColorCodes("Tempo Rimasto: %s"),timeSeconds)
        );
        infoXtitles.put(
                "obtRes",
                String.format(ColorUtils.translateColorCodes("Risorse Ottenute: %s"),obtainedResources)
        );
        infoXtitles.put(
                "remAnc",
                String.format(ColorUtils.translateColorCodes("Ancestrali Rimasti: %s"),mineData.getLevel().getAncestralsQuantity())
        );
        infoXtitles.put(
                "remMin",
                String.format(ColorUtils.translateColorCodes("Minerali Rimasti: %s"),mineData.getLevel().getMaxMineralQuantity())
        );
        infoXtitles.put(
                "roomNr",
                String.format(ColorUtils.translateColorCodes("Stanze Nella Miniera: %s"),rooms.size())
        );

        int i=0;
        for (Map.Entry<String, String> set : infoXtitles.entrySet()) {

            System.out.println(set.getKey());
            Team line = board.registerNewTeam(set.getKey());
            line.addEntry(String.format("§%s",i));
            line.setPrefix(set.getValue());
            obj.getScore(String.format("§%s",i)).setScore(i++);

        }



        return board;
    }


//    @EventHandler
//    public void p(Entity)
    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        Block blockBreaking = event.getBlock();
        //Ogni blocco custom é fatto da note_block
        if (blockBreaking.getType() != Material.NOTE_BLOCK) return;
        //Controllare a quale custom_block corrisponde
        CustomBlock matchedBlock= CustomBlock.getCustomBlockFromBlock(blockBreaking);

        //Check if the custom_block is a mining_block
        MiningBlock miningBlock = MiningBlock.getMiningBlockFromCustom(matchedBlock);
        if(miningBlock!=null ){
            if(miningBlock.getBagType()==null)return;
            //Control if you have proper bag for the block
            Player player=event.getPlayer();
            event.setDropItems(false);
            //Get requested BegType to breakBlock
            Storage bagTypeStorage = miningBlock.getBagType();
            //Check If Player has the right bag (if not dont break and send message)
            Bag bag = Bag.getBagFromInventory(bagTypeStorage,player.getInventory());
            //Controllare se il piccone c'é e se puó rompere il blocco
            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
            Pickaxe pickaxeInHand = Pickaxe.getPickaxeFromItemStack(itemInHand);
            if (pickaxeInHand == null) {
                player.sendMessage("NON HAI UN PICCONE IN MANO!!");
                return;
            }
            if (pickaxeInHand.getHardnessLevel().getHardnessValue() < miningBlock.getHardnessLevel().getHardnessValue()) {
                player.sendMessage(String.format("NON HAI UN PICCONE ABBASTANZA FORTE (HARDNESS LEVEL RICHIESTA: %s)!!",miningBlock.getHardnessLevel()));
                return;
            }
            System.out.println(bag);
            //Controllare se é null allora la bag non é presente
            if(bag==null) {
                player.sendMessage("NON HAI LA BAG GIUSTA, TI SERVER LA BAG: "+bagTypeStorage);
                return;
            }
            System.out.println(bag.getResources());
            System.out.println(bag.getMaxResources());
            //La bag é stata trovata, controllare se é piena
            if(bag.isFull()){
                player.sendMessage("LA TUA BAG É PIENA NON PUOI MINARE ALTRI MATERIALI");
                return;
            }
            //Se sono stati passati tutti i controlli allora procedere al cyclo di rottura del blocco
            //Se il blocco non é ancora presente nella hashmap che contiene i blocchi in via di mining aggiungerlo
            HashMap<Vector, Integer> blockInMiningState = Test.getInstance().getBlockInMiningState().getOrDefault(player, null);
            if (blockInMiningState==null){
                player.sendMessage("A QUNTO PARE NON SEI IN UNA SESSIONE DI MINIERA, QUINDI NON PUOI ROMPERE QUESTO BLOCCO");
                return;
            }
            //Calcolare il danno arrecato al blocco (per il momento default 1)
            int blockDamage = pickaxeInHand.getBlockDamage();

            Vector vector = blockBreaking.getLocation().toVector();

            //Se non contiene aggiungere nuovo
            if(!blockInMiningState.containsKey(vector)){
                blockInMiningState.put(vector,blockDamage);
            }else{
                blockInMiningState.put(vector,blockInMiningState.get(vector)+blockDamage);
            }

            //Current damage
            int currentBlockDamage = blockInMiningState.get(vector);

            //Controllare se il blocco si deve rompere
            if(currentBlockDamage>=miningBlock.getLifePoints()){
                //blocco rotto
                if(miningBlock.getNextBlock()!=null){
                    event.setCancelled(true);
                    miningBlock.getNextBlock().placeBlock(blockBreaking);
                }else{
                    event.setCancelled(false);
                }

                if(bag.addResources(miningBlock.getDropQuantity())){
                    player.sendMessage("HAI LA BAG PIENA ALCUNE RISORSE SONO STATE BUTTATE!!!");
                }else{
                    player.sendMessage(String.format("SONO STATE AGGIUNTE %s di %s ALLA TUA BAG!",miningBlock.getDropQuantity(),miningBlock.getBagType().toString()));
                }
                bag.updateBag(player.getInventory());

                obtainedResources+=miningBlock.getDropQuantity();

                scoreboard.getTeam("obtRes").setPrefix(
                        String.format(ColorUtils.translateColorCodes("Risorse Ottenute: %s"),obtainedResources)
                );

                //CHANCE TO REPLACE WITH MINERAL
                if(randomGen.nextDouble() < miningBlock.getMineralSpawnRate()){
                    //CHECK IF THERE ARE ANY MINERALS LEFT TO FIND
                    if(mineData.getLevel().getMaxMineralQuantity()!=brokenMineralQuantity) {
                        //SPAWN TRIGGERED
                        //CHOOSE THE MINERAL TO SPAWN
                        for (MineralBlock mineralBlock : MineralBlock.values()) {
                            if (mineralBlock.getDropFrom() == miningBlock) {
                                mineralBlock.getMiningBlock().getCustomBlock().placeBlock(blockBreaking);
                                event.setCancelled(true);
                                break;
                            }
                        }
                    }
                }

            }else{
                //blocco non rotto
                event.setCancelled(true);
                player.sendMessage(String.format("[%s/%s]",currentBlockDamage,miningBlock.getLifePoints()));
            }
        }

        //Control if you have proper bag for the block
        Player player=event.getPlayer();
        event.setDropItems(false);

    }


    //MINERAL BREAK EVENT
    @EventHandler
    public void onMineralBreak(BlockBreakEvent event){
        CustomBlock customBlockBroken = CustomBlock.getCustomBlockFromBlock(event.getBlock());
        if (customBlockBroken == null) return;
        //Controlla se il blocco e un minerale
        Mineral mineralBroken = null;
        int i=0;
        for(Mineral mineral: Mineral.values()){
            i++;
            if (mineral.getCustomBlock()==customBlockBroken) {
                mineralBroken=mineral;
                break;
            }
            if (i==Mineral.values().length) return;
        }

        Location location = event.getBlock().getLocation();
        event.setDropItems(false);

        //@todo: modify rarity of purity and rarity (NOW IS DEAFULT TO 0,0)

        location.getWorld().dropItemNaturally(location,mineralBroken.getMineralType().getMineralItem_0Rarity_0Purity().getItemStack());
        brokenMineralQuantity++;
        scoreboard.getTeam("remMin").setPrefix(
                String.format(ColorUtils.translateColorCodes("Minerali Rimasti: %s"),mineData.getLevel().getMaxMineralQuantity()-brokenMineralQuantity)
        );
    }

    //ANCESTRAL DEAD EVENT
    @EventHandler
    public void onKilledAncestral(MythicMobDeathEvent event){
        System.out.println("Ancestrale '"+event.getMobType().getInternalName()+"' ucciso da: "+event.getKiller().getName());
        for(ActiveMob ancestral:spawnedAncestrals){
            if(event.getMob()==ancestral){
                ancestralRemaining--;
            }
        }
        scoreboard.getTeam("remAnc").setPrefix(
                String.format(ColorUtils.translateColorCodes("Ancestrali Rimasti: %s"),ancestralRemaining)
        );



        for(Ancestral ancestral: Ancestral.values()){
            if(Objects.equals(ancestral.getInternalName(), event.getMob().getType().getInternalName())){
                Location location = event.getEntity().getLocation();
                for(ItemStack item:ancestral.getDrops()){
                    location.getWorld().dropItemNaturally(location,item);
                }
            }
        }

    }

    //ANCESTRAL DESPAWN
    @EventHandler
    public void onDespawnAncestral(MythicMobDespawnEvent event){
        System.out.println("ANCESTRAL DESPAWNED");
        for(ActiveMob ancestral:spawnedAncestrals){
            if(event.getMob()==ancestral){
                ancestralRemaining--;
            }
        }
        scoreboard.getTeam("remAnc").setPrefix(
                String.format(ColorUtils.translateColorCodes("Ancestrali Rimasti: %s"),ancestralRemaining)
        );

    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if(!(sender instanceof Player)){
            return false;
        }

        finishMiniGame();

        return true;
    }



}
