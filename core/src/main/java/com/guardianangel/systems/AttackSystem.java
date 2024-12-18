package com.guardianangel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;
import com.guardianangel.Main;
import com.guardianangel.components.*;
import com.guardianangel.entities.weapons.Weapon;
import com.guardianangel.screens.GameOverScreen;

public class AttackSystem extends EntitySystem {
    private Weapon weapon;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<HealthComponent> healthMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    public AttackSystem(Weapon weapon) {
        this.weapon = weapon;
        this.spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        this.healthMapper = ComponentMapper.getFor(HealthComponent.class);
        this.collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
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

                for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class, CollisionComponent.class, SpriteComponent.class).get())) {
                    HealthComponent health = healthMapper.get(entity);
                    CollisionComponent collision = collisionMapper.get(entity);
                    SpriteComponent sprite = spriteMapper.get(entity);

                    if (collision.bounds.contains(mouseX, mouseY)) {
                        health.health -= 10;

                        if (sprite != null && !sprite.isHurtOrDead && sprite.hurtAnimation != null) {
                            sprite.setAnimation(sprite.hurtAnimation);
                            sprite.isHurtOrDead = true;
                            sprite.stateTime = 0;
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    sprite.isHurtOrDead = false;
                                }
                            }, sprite.hurtAnimation.getAnimationDuration());
                        }


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
