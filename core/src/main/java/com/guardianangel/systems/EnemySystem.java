package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.guardianangel.Main;
import com.guardianangel.components.EnemyTagComponent;
import com.guardianangel.components.PositionComponent;
import com.guardianangel.components.SpriteComponent;
import com.guardianangel.screens.GameOverScreen;

public class EnemySystem extends EntitySystem {
    private float speed = 150f;
    private OrthographicCamera camera;

    public EnemySystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, EnemyTagComponent.class, SpriteComponent.class).get())) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

            if (sprite.isInHurtState) {
                sprite.setAnimation(sprite.hurtAnimation);
                continue;
            }

            if (!sprite.isHurtOrDead) {
                sprite.setAnimation(sprite.runAnimation);
                position.x -= speed * deltaTime;

                sprite.flipHorizontally = true;
            }

            if (position.x + 64 < camera.position.x - camera.viewportWidth / 2) {
                getEngine().removeEntity(entity);
                Main.getInstance().setScreen(new GameOverScreen());
            }
        }
    }
}
