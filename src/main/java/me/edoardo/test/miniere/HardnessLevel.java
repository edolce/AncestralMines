package me.edoardo.test.miniere;

public enum HardnessLevel {

    Level0(0),
    Level1(1),
    Level2(2),
    Level3(3),
    Level4(4),
    Level5(5),
    Level6(6),
    Level7(7),
    Level8(8);

    private final int hardnessValue;

    HardnessLevel(int hardnessValue){
        this.hardnessValue=hardnessValue;
    }

    public int getHardnessValue() {
        return hardnessValue;
    }
}
