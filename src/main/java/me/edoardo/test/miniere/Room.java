package me.edoardo.test.miniere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Room {

    private final RoomType roomType;
    private HashMap<String,Room> connections=new HashMap<>();
    private final List<String> freeConnections;
    private final int xPos;
    private final int yPos;

    public Room(RoomType roomType, int xPos, int yPos){

        this.roomType=roomType;
        this.xPos = xPos;
        this.yPos = yPos;
        this.freeConnections = new ArrayList<>(Arrays.asList(roomType.getPossibleConnections()));
    }

    public HashMap<String,Room> getConnections() {
        return connections;
    }

    public void addConnections(Room room,String side,String reverseSide) {
        if(!Arrays.asList(room.getRoomType().getPossibleConnections()).contains(reverseSide)){
            System.out.println("Si Sta cercando di aggiungere una connessione non valida");
            return;
        }
        this.connections.put(side,room);
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isCompleted(){
        return this.connections.size() == roomType.getPossibleConnections().length;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean compareXY(int posXtoControl, int posYtoControl) {

        return posXtoControl == xPos & posYtoControl == yPos;

    }

    public List<String> getFreeConnections() {

        return freeConnections;

    }


    public void removeFreeConnection(String connection){
        this.freeConnections.remove(connection);
    }
}
