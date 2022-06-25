package me.edoardo.test.miniere;

import me.edoardo.test.Test;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Objects;

public enum RoomType {

    SPAWN(1,2,3,4,5,6,new String[]{"N"}),
    STRAIGHT_NS(1,2,3,4,5,6,new String[]{"N","S"}),
    STRAIGHT_WE(1,2,3,4,5,6,new String[]{"W","E"}),
    CROSSROAD(1,2,3,4,5,6,new String[]{"N","W","S","E"}),
    TURN_NW(1,2,3,4,5,6,new String[]{"N","W"}),
    TURN_NE(1,2,3,4,5,6,new String[]{"N","E"}),
    TURN_SW(1,2,3,4,5,6,new String[]{"S","W"}),
    TURN_SE(1,2,3,4,5,6,new String[]{"S","E"}),
    IMPASSE(1,2,3,4,5,6,new String[]{});

    private final Block NW_UP=null;
    private final Block SE_DOWN=null;
    private final String[] possibleConnections;



    RoomType(int NW_UPx,int NW_UPy,int NW_UPz,int SE_DOWNx,int SE_DOWNy,int SE_DOWNz,String[] possibleConnections){
        //this.NW_UP = Test.getInstance().getServer().getWorld("void").getBlockAt(NW_UPx,NW_UPy,NW_UPz);
        //this.SE_DOWN = Test.getInstance().getServer().getWorld("void").getBlockAt(SE_DOWNx,SE_DOWNy,SE_DOWNz);
        this.possibleConnections = possibleConnections;
    }


    public Block getNW_UP() {
        return NW_UP;
    }

    public Block getSE_DOWN() {
        return SE_DOWN;
    }

    public String[] getPossibleConnections() {
        return possibleConnections;
    }

    public boolean isPossibleToAdd(List<Room> rooms, RoomType toAdd, String sideToIgnore, Room linkedRoom) {



        int[][] relFreePos = toAdd.getRelativeFreePosNeeded(sideToIgnore);

        for(int i=0;i<relFreePos.length;i++){
            int posX=relFreePos[0][i];
            int posY=relFreePos[1][i];

            switch (sideToIgnore){

            }

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

        int[][] relativeFreePosNeeded = new int[2][];


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
}
