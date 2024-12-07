package com.guardianangel.entities.weapons;

public class Pistol extends Weapon {
    public Pistol() {
        super("Pistol", 8, 2f);
    }

    @Override
    protected void onShoot() {
        System.out.println("Pistol fired!");
    }
}
