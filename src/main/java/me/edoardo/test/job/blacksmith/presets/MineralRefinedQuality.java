package me.edoardo.test.job.blacksmith.presets;

public enum MineralRefinedQuality {
    PERFECTLY_REFINED("",100,81),
    WELL_REFINED("",80,61),
    AVERAGE_REFINED("",60,41),
    NOT_GOOD_REFINED("",40,21),
    ORRIBLE_REFINED("",20,0);

    final String display;
    final int rangeMin;
    final int rangeMax;

    MineralRefinedQuality(String display, int rangeMax, int rangeMin){
        this.display = display;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }
}
