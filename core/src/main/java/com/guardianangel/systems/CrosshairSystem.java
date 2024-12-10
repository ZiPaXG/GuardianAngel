package com.guardianangel.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class CrosshairSystem extends EntitySystem {
    private SpriteBatch batch;
    private Texture crosshairTexture;
    private OrthographicCamera camera;

    private float crosshairX = 0, crosshairY = 0;
    private static final float SIZE = 32;
    private static final float SCALE = 2f;

    public CrosshairSystem(OrthographicCamera camera) {
        super(100);
        this.camera = camera;
        batch = new SpriteBatch();
        crosshairTexture = new Texture(Gdx.files.internal("UI/Crosshairs/Crosshair1.png"));
    }

    @Override
    public void update(float deltaTime) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        Vector3 worldCoordinates = camera.unproject(new Vector3(mouseX, mouseY, 0));
        float targetX = worldCoordinates.x;
        float targetY = worldCoordinates.y;

        crosshairX += (targetX - crosshairX) * 0.15f;
        crosshairY += (targetY - crosshairY) * 0.15f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(crosshairTexture,
            crosshairX - (SIZE * SCALE) / 2,
            crosshairY - (SIZE * SCALE) / 2,
            SIZE * SCALE,
            SIZE * SCALE
        );
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        crosshairTexture.dispose();
    }
}
