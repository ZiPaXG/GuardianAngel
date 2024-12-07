package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.guardianangel.entities.weapons.Weapon;

public class PlayerEntity extends Entity {
    private Weapon currentWeapon;
    private int ammoAmount;
    private int scoreCount;

    public PlayerEntity(Weapon currentWeapon, int ammoAmount) {
        this.currentWeapon = currentWeapon;
        this.ammoAmount = ammoAmount;
        this.scoreCount = 0;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public int getAmmoAmount() {
        return ammoAmount;
    }

    public void setAmmoAmount(int changeAmount) {
        if (Math.abs(changeAmount) > ammoAmount) {
            ammoAmount = 0;
        }
        else if (ammoAmount > 0) {
            ammoAmount += changeAmount;
        }
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount += scoreCount;
    }
}
