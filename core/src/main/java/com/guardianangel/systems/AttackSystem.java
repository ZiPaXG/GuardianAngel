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
                        if (!health.isDead) { // Проверяем, не мертв ли враг
                            health.health -= weapon.getDamage(); // Уменьшаем здоровье на урон оружия

                            if (health.health <= 0) {
                                health.isDead = true; // Помечаем врага как мертвого
                                sprite.setAnimation(sprite.deathAnimation);
                                sprite.isHurtOrDead = true;

                                HUDSystem hudSystem = getEngine().getSystem(HUDSystem.class);
                                if (hudSystem != null) {
                                    if (entity.getComponent(WalkerTagComponent.class) == null) {
                                        hudSystem.addScore(100); // Добавляем 100 очков за убийство врага
                                    } else {
                                        hudSystem.addScore(-50); // Вычитаем 50 очков за попадание в WalkerEntity
                                    }
                                }

                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        getEngine().removeEntity(entity);
                                        if (entity.getComponent(WalkerTagComponent.class) != null)
                                            Main.getInstance().setScreen(new GameOverScreen());
                                    }
                                }, sprite.deathAnimation.getAnimationDuration() - 0.05f);
                            } else if (!sprite.isHurtOrDead && sprite.hurtAnimation != null) {
                                sprite.setAnimation(sprite.hurtAnimation);
                                sprite.isInHurtState = true;
                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        sprite.isInHurtState = false;
                                    }
                                }, sprite.hurtAnimation.getAnimationDuration());
                            }
                        }
                    }
                }
            }
        }
    }




}
