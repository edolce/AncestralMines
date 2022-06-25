package me.edoardo.test.miniere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mine {

    private List<Room> rooms=new ArrayList<>();


    //COSTANTI MODIFICABILI
    private final int minRooms = 10;
    private final int maxRooms = 25;

    public Mine(){


        createBasicPath();

        for (Room room:rooms){
            System.out.printf("[%s;%s]: %s%n",room.getxPos(),room.getyPos(),room.getRoomType());
        }


    }




    private void createBasicPath(){
        List<Room> queue = new ArrayList<>();
        Room spawn = new Room(RoomType.SPAWN, 0, 0);
        queue.add(spawn);
        this.rooms.add(spawn);

        List<RoomType> possibleRooms = new ArrayList<>();

        while (!queue.isEmpty()) {
            for (Room room : queue) {

                String sideToLink = room.getFreeConnections().get(0);


                //Creazione Lista Di Possibili link secondo la STRUTTURA delle altre rooms
                for(RoomType rType:RoomType.values()){
                    if(rType.isPossibleToAdd(this.rooms,rType,sideToLink,room)) possibleRooms.add(rType);
                }

                //Decisione ROOM a seconda delle COSTANTI
                Random rand = new Random();
                RoomType newRoomType = possibleRooms.get(rand.nextInt(possibleRooms.size()));


                int[] newRoomXYPos=RoomType.getLinkedXYPos(room,sideToLink);

                assert newRoomXYPos != null;
                Room newRoom = new Room(newRoomType,newRoomXYPos[0],newRoomXYPos[1]);

                queue.add(newRoom);
                this.rooms.add(newRoom);
                room.addConnections(newRoom,sideToLink);
                room.removeFreeConnection(sideToLink);

                if (room.isCompleted()) queue.remove(room);
                if (this.rooms.size()>maxRooms) return;
            }

        }
    }











}
