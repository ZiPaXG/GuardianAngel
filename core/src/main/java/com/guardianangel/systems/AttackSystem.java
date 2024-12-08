package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;
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

                for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class).get())) {
                    PositionComponent position = entity.getComponent(PositionComponent.class);
                    HealthComponent health = entity.getComponent(HealthComponent.class);

                    if (Math.abs(position.x - mouseX) < 20 && Math.abs(position.y - mouseY) < 20) {
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
