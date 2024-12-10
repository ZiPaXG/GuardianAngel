package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guardianangel.components.PositionComponent;
import com.guardianangel.components.SpriteComponent;

public class RenderSystem extends EntitySystem {
    private final SpriteBatch batch = new SpriteBatch();
    private final OrthographicCamera camera;

    public RenderSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, SpriteComponent.class).get())) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

            sprite.stateTime += deltaTime;

            sprite.render(batch, position.x, position.y);
        }
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }
}
