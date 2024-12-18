package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.guardianangel.entities.weapons.Weapon;

public class PlayerEntity extends Entity {
    private Weapon[] weapons;
    private int currentWeaponIndex;
    private int[] ammoAmount;
    private int scoreCount;

    public PlayerEntity(Weapon[] initialWeapons, int[] ammoAmount) {
        this.weapons = initialWeapons;
        this.currentWeaponIndex = 0;
        this.ammoAmount = ammoAmount;
        this.scoreCount = 0;
    }

    public Weapon getCurrentWeapon() {
        return weapons[currentWeaponIndex];
    }

    public void switchWeapon(int index) {
        if (index >= 0 && index < weapons.length) {
            currentWeaponIndex = index;
        }
    }

    public int getAmmoAmount() {
        return ammoAmount[currentWeaponIndex];
    }

    public void changeAmmoAmount(int changeAmount) {
        ammoAmount[currentWeaponIndex] = Math.max(0, ammoAmount[currentWeaponIndex] + changeAmount);
    }

    public void changeAmmoAmountForWeapon(int weaponIndex, int changeAmount) {
        if (weaponIndex >= 0 && weaponIndex < ammoAmount.length) {
            ammoAmount[weaponIndex] = Math.max(0, ammoAmount[weaponIndex] + changeAmount);
        }
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void addScore(int score) {
        this.scoreCount += score;
    }
}
