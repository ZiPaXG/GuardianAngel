package com.guardianangel.entities.weapons;

public class Rifle extends Weapon{
    public Rifle() {
        super("Rifle", 30, 2.5f, "UI/Crosshairs/Crosshair2.png", 0.2f);
    }
    @Override
    protected void onShoot() {
        System.out.println("Rifle fired!");
    }
}
