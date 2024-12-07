package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;

public class RenderSystem extends EntitySystem {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void update(float deltaTime) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class).get())) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            HealthComponent health = entity.getComponent(HealthComponent.class);

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(position.x - 10, position.y - 10, 20, 20);
        }
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

