package me.edoardo.test.miniere.ancestral;

public class ProbabilityDrop {


    private final double triggerProbability;
    private final AncestralDrop drop;
    private final int minQnt;
    private final int maxQnt;

    ProbabilityDrop( AncestralDrop drop,double triggerProbability,int minQnt,int maxQnt){
        this.triggerProbability = triggerProbability;
        this.drop = drop;
        this.minQnt=minQnt;
        this.maxQnt=maxQnt;
    }

    public AncestralDrop getDrop() {
        return drop;
    }

    public double getTriggerProbability() {
        return triggerProbability;
    }

    public int getMaxQnt() {
        return maxQnt;
    }

    public int getMinQnt() {
        return minQnt;
    }
}
