package com.guardianangel.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.utils.FontManager;

public class HUDSystem extends EntitySystem {
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private PlayerEntity player;
    private float timeElapsed;
    private int score;

    public HUDSystem(PlayerEntity player) {
        spriteBatch = new SpriteBatch();
        font = FontManager.getDefaultFont();
        this.player = player;
        timeElapsed = 0;
        score = 0;
    }

    @Override
    public void update(float deltaTime) {
        timeElapsed += deltaTime;

        int totalSeconds = (int) timeElapsed;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        spriteBatch.begin();
        font.draw(spriteBatch, String.format("Time: %d:%02d", minutes, seconds), 10, Gdx.graphics.getHeight() - 10);
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
