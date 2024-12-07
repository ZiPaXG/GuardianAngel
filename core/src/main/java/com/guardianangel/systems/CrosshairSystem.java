package com.guardianangel.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CrosshairSystem extends EntitySystem {
    private ShapeRenderer shapeRenderer;
    private float crosshairX, crosshairY;

    public CrosshairSystem() {
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        float targetX = Gdx.input.getX();
        float targetY = Gdx.graphics.getHeight() - Gdx.input.getY();
        crosshairX += (targetX - crosshairX) * 0.1f;
        crosshairY += (targetY - crosshairY) * 0.1f;


        Gdx.gl.glLineWidth(3);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(crosshairX, crosshairY, 20);
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
