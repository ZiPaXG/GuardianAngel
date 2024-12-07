package com.guardianangel.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guardianangel.entities.PlayerEntity;

public class HUDSystem extends EntitySystem {
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private PlayerEntity player;
    private float timeElapsed;
    private int score;

    public HUDSystem(PlayerEntity player) {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        this.player = player;
        timeElapsed = 0;
        score = 0;
    }

    @Override
    public void update(float deltaTime) {
        timeElapsed += deltaTime;

        spriteBatch.begin();
        font.draw(spriteBatch, "Time: " + (int) timeElapsed + "s", 10, Gdx.graphics.getHeight() - 10);
        font.draw(spriteBatch, "Ammo: " + player.getCurrentWeapon().getCurrentAmmo() + "/" + player.getAmmoAmount(), 10, Gdx.graphics.getHeight() - 40);
        font.draw(spriteBatch, "Score: " + score, 10, Gdx.graphics.getHeight() - 70);
        if (player.getCurrentWeapon().isReloading()) {
            font.draw(spriteBatch, "Reloading...", 10, Gdx.graphics.getHeight() - 100);
        }
        spriteBatch.end();
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
