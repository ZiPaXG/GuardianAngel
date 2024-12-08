package com.guardianangel.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guardianangel.entities.GuardEntity;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.entities.WalkerEntity;
import com.guardianangel.entities.weapons.Pistol;
import com.guardianangel.systems.*;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Engine engine;
    private HUDSystem hudSystem;

    public void createEntities() {
        Entity walker = new WalkerEntity(100, 100, 50);
        Entity guard = new GuardEntity(200, 200, 100);

        engine.addEntity(walker);
        engine.addEntity(guard);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        engine = new Engine();
        Pistol pistol = new Pistol();
        PlayerEntity player = new PlayerEntity(pistol, 24);
        hudSystem = new HUDSystem(player);

        engine.addSystem(new PathFollowerSystem());
        engine.addSystem(new GuardSystem());
        engine.addSystem(new CrosshairSystem());
        engine.addSystem(new ReloadSystem(player));
        engine.addSystem(new AttackSystem(player.getCurrentWeapon()));
        engine.addSystem(new HUDSystem(player));
        engine.addSystem(new RenderSystem());
        engine.addSystem(hudSystem);
        createEntities();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
        batch.begin();

        batch.end();

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        hudSystem.dispose();
    }
}
