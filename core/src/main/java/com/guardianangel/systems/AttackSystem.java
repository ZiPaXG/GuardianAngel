package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;
import com.guardianangel.components.CollisionComponent;
import com.guardianangel.entities.weapons.Weapon;

public class AttackSystem extends EntitySystem {
    private Weapon weapon;

    public AttackSystem(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (weapon.canShoot()) {
                weapon.shoot();

                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

                for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class, CollisionComponent.class).get())) {
                    HealthComponent health = entity.getComponent(HealthComponent.class);
                    CollisionComponent collision = entity.getComponent(CollisionComponent.class);

                    if (collision.bounds.contains(mouseX, mouseY)) {
                        health.health -= 10;

                        if (health.health <= 0) {
                            getEngine().removeEntity(entity);
                        }
                    }
                }
            }
        }
    }
}
