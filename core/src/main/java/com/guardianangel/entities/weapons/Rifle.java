package com.guardianangel.entities.weapons;

import com.badlogic.gdx.Gdx;

public class Rifle extends Weapon{
    public Rifle() {
        super("Rifle", 30, 6f, "UI/Crosshairs/Crosshair2.png", 0.2f);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Rifle.wav"));
        reloadSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/RifleReload.wav"));
    }
    @Override
    protected void onShoot() {
        shootSound.play();
    }

    @Override
    protected void onReload() {
        reloadSound.play();
    }

    public void dispose() {
        if (shootSound != null) {
            shootSound.dispose();
        }
    }
}
