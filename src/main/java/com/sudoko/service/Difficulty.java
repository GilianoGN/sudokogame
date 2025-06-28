package com.sudoko.service;

public enum Difficulty {
    EASY(0.55),
    MEDIUM(0.45),
    HARD(0.35),
    EXPERT(0.25);

    private final double fillPercentage;

    Difficulty(double fillPercentage){
        this.fillPercentage = fillPercentage;
    }
    public double getFillPercentage(){
        return fillPercentage;
    }
}