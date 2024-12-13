package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.guardianangel.Main;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;
import com.guardianangel.components.CollisionComponent;
import com.guardianangel.components.WalkerTagComponent;
import com.guardianangel.entities.weapons.Weapon;
import com.guardianangel.screens.GameOverScreen;

public class AttackSystem extends EntitySystem {
    private Weapon weapon;

    public AttackSystem(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changeWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void update(float deltaTime) {
        weapon.update(deltaTime);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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
                            if (entity.getComponent(WalkerTagComponent.class) != null)
                                Main.getInstance().setScreen(new GameOverScreen());
                            getEngine().removeEntity(entity);
                        }
                    }
                }
            }
        }
    }
}
