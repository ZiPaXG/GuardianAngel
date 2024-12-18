package com.guardianangel.utils;

public class GameState {
    private int enemiesRemaining;
    private boolean waveCleared;

    public void setEnemiesRemaining(int count) {
        this.enemiesRemaining = count;
        waveCleared = (count <= 0);
    }

    public void enemyDefeated() {
        enemiesRemaining = Math.max(0, enemiesRemaining - 1);
        waveCleared = (enemiesRemaining <= 0);
    }

    public boolean isWaveCleared() {
        return waveCleared;
    }
}
