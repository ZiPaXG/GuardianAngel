package com.guardianangel.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.guardianangel.components.PathComponent;
import com.guardianangel.entities.GuardEntity;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.entities.WalkerEntity;
import com.guardianangel.entities.weapons.Pistol;
import com.guardianangel.systems.*;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Engine engine;
    private HUDSystem hudSystem;

    private OrthographicCamera camera;

    public void createEntities() {
        WalkerEntity walker = new WalkerEntity(100, 100, 50);

        PathComponent path = walker.getComponent(PathComponent.class);
        path.path.add(new Vector2(200, 100));
        path.path.add(new Vector2(200, 200));
        path.path.add(new Vector2(100, 200));
        path.path.add(new Vector2(100, 100));

        path.speed = 100;
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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        engine.addSystem(new PathFollowerSystem());
        engine.addSystem(new GuardSystem());
        engine.addSystem(new CrosshairSystem(camera));
        engine.addSystem(new ReloadSystem(player));
        engine.addSystem(new AttackSystem(player.getCurrentWeapon()));
        engine.addSystem(new HUDSystem(player));
        engine.addSystem(new RenderSystem());
        engine.addSystem(hudSystem);
        createEntities();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        engine.update(delta);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1600, 900);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

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
