package me.edoardo.test.miniere;

import me.edoardo.test.Test;
import org.bukkit.Location;

import java.util.List;
import java.util.Objects;

public enum RoomType {

    SPAWN(167,56,-71,187,76,-51,new String[]{"S"}, true),
    CROSSROAD(167,56,-50,187,76,-30,new String[]{"N","W","S","E"}, false),
    STRAIGHT_NS(167,56,-29,187,76,-9,new String[]{"N","S"}, false),
    STRAIGHT_WE(167,56,-7,187,76,13,new String[]{"W","E"}, false),
    TURN_NW(251,56,-71,271,76,-51,new String[]{"N","W"}, false),
    TURN_NE(251,56,-29,271,76,-9,new String[]{"N","E"}, false),
    TURN_SW(209,56,-71,229,76,-51,new String[]{"S","W"}, false),
    TURN_SE(209,56,-29,229,76,-9,new String[]{"S","E"}, false),
    IMPASSEN(209,56,55,229,76,75,new String[]{"N"}, true),
    IMPASSEW(251,56,55,271,76,75,new String[]{"W"}, true),
    IMPASSES(251,56,13,271,76,33,new String[]{"S"}, true),
    IMPASSEE(209,56,13,229,76,33,new String[]{"E"}, true);

    private final Location NW_DOWN;
    private final Location SE_UP;
    private final String[] possibleConnections;
    private final boolean isImpasse;



    RoomType(int NW_DOWNx, int NW_DOWNy, int NW_DOWNz, int SE_UPx, int SE_UPy, int SE_UPz, String[] possibleConnections, boolean isImpasse){
        this.isImpasse = isImpasse;
        this.NW_DOWN = new Location(Test.getInstance().getServer().getWorld("main"),NW_DOWNx,NW_DOWNy,NW_DOWNz);
        this.SE_UP = new Location(Test.getInstance().getServer().getWorld("main"),SE_UPx,SE_UPy,SE_UPz);
        this.possibleConnections = possibleConnections;
    }


    public Location getNW_DOWN() {
        return NW_DOWN;
    }

    public Location getSE_UP() {
        return SE_UP;
    }

    public String[] getPossibleConnections() {
        return possibleConnections;
    }

    public boolean isPossibleToAdd(List<Room> rooms, RoomType toAdd, String sideToIgnore, String sideToIgnoreReversed, Room linkedRoom) {


        //Ricavo le posizioni relative che andrebbero ad occupare eventuali rooms collegate al pezzo
        int[][] relFreePos = toAdd.getRelativeFreePosNeeded(sideToIgnoreReversed);




        for(int i=0;i<relFreePos[0].length;i++){
            int posX=relFreePos[0][i];
            int posY=relFreePos[1][i];

            int[] roomXYPos = getLinkedXYPos(linkedRoom,sideToIgnore);

            assert roomXYPos != null;
            int posXtoControl = roomXYPos[0]+posX;
            int posYtoControl = roomXYPos[1]+posY;

            for (Room room:rooms){
                if (room.compareXY(posXtoControl,posYtoControl)) return false;
            }
        }

        return true;

    }

    public int[][] getRelativeFreePosNeeded(String sideToIgnore) {

        int[][] relativeFreePosNeeded = new int[2][possibleConnections.length];


        for (int k=0;k<possibleConnections.length;k++){

            if (Objects.equals(possibleConnections[k], sideToIgnore)) continue;

            switch (possibleConnections[k]){
                case "N": {
                    relativeFreePosNeeded[0][k]=1;
                    relativeFreePosNeeded[1][k]=0;
                }
                break;
                case "S": {
                    relativeFreePosNeeded[0][k]=-1;
                    relativeFreePosNeeded[1][k]=0;
                }
                break;
                case "W": {
                    relativeFreePosNeeded[0][k]=0;
                    relativeFreePosNeeded[1][k]=-1;
                }
                break;
                case "E": {
                    relativeFreePosNeeded[0][k]=0;
                    relativeFreePosNeeded[1][k]=1;
                }
                break;
            }
        }
        return relativeFreePosNeeded;
    }


    public static int[] getLinkedXYPos(Room room,String linkedSide){
        switch (linkedSide){
            case "N": {
                return new int[]{room.getxPos()+1,room.getyPos()};
            }
            case "S": {
                return new int[]{room.getxPos()-1,room.getyPos()};
            }
            case "W": {
                return new int[]{room.getxPos(),room.getyPos()-1};
            }
            case "E": {
                return new int[]{room.getxPos(),room.getyPos()+1};
            }
        }
        return null;
    }

    public boolean isImpasse() {
        return isImpasse;
    }
    public boolean isNotImpasse() {
        return !isImpasse;
    }
}
