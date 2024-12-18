package com.guardianangel.entities.weapons;

import com.badlogic.gdx.Gdx;

public class Pistol extends Weapon {
    public Pistol() {
        super("Pistol", 8, 2f, "UI/Crosshairs/Crosshair1.png", 1f, 10);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Pistol.wav"));
        reloadSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/PistolReload.wav"));
    }

    @Override
    protected void onShoot() {
        shootSound.play();
    }

    @Override
    protected void onReload() {
        reloadSound.play();
    }
}
