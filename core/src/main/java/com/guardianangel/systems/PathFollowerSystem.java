package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.guardianangel.components.PathComponent;
import com.guardianangel.components.PositionComponent;
import com.guardianangel.components.SpriteComponent;
import com.guardianangel.components.WalkerTagComponent;

public class PathFollowerSystem extends EntitySystem {
    private static final float TOLERANCE = 1.0f;
    private static final float SPEED = 160.0f;

    private boolean moveToNextPoint = false;

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            moveToNextPoint = true;
        }

        for (Entity entity : getEngine().getEntitiesFor(Family.all(WalkerTagComponent.class, PathComponent.class, PositionComponent.class, SpriteComponent.class).get())) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            PathComponent path = entity.getComponent(PathComponent.class);
            SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

            if (path.path == null || path.path.isEmpty() || path.currentIndex >= path.path.size()) {
                continue;
            }

            if (!moveToNextPoint) {
                sprite.setAnimation(sprite.idleAnimation);;
                continue;
            }

            Vector2 currentTarget = path.path.get(path.currentIndex);
            Vector2 currentPosition = new Vector2(position.x, position.y);

            Vector2 direction = currentTarget.cpy().sub(currentPosition).nor();
            currentPosition.mulAdd(direction, SPEED * deltaTime);

            position.x = currentPosition.x;
            position.y = currentPosition.y;

            sprite.setAnimation(sprite.runAnimation);;

            if (currentPosition.dst(currentTarget) <= TOLERANCE) {
                path.currentIndex++;
                moveToNextPoint = false;

                if (path.currentIndex >= path.path.size()) {
                    path.path.clear();
                    path.currentIndex = 0;
                    sprite.setAnimation(sprite.idleAnimation);
                }
            }
        }
    }
}
