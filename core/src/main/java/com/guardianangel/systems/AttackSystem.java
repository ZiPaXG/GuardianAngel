package com.guardianangel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.guardianangel.Main;
import com.guardianangel.components.*;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.entities.weapons.Weapon;
import com.guardianangel.entities.weapons.WeaponType;
import com.guardianangel.screens.GameOverScreen;
import com.guardianangel.utils.AmmoDropUtil;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class AttackSystem extends EntitySystem {
    private Weapon weapon;
    private PlayerEntity player;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<HealthComponent> healthMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    public int countDeathEnemy;
    private OrthographicCamera camera;
    private RayHandler rayHandler;

    // Конструктор AttackSystem
    public AttackSystem(Weapon weapon, PlayerEntity player, OrthographicCamera camera, RayHandler rayHandler) {
        this.weapon = weapon;
        this.player = player;
        this.spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        this.healthMapper = ComponentMapper.getFor(HealthComponent.class);
        this.collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        this.countDeathEnemy = 0;
        this.camera = camera;
        this.rayHandler = rayHandler; // Инициализация RayHandler
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

                Vector3 mouseWorldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                float mouseX = mouseWorldCoords.x;
                float mouseY = mouseWorldCoords.y;

                PointLight muzzleFlash = new PointLight(rayHandler, 64, new Color(200.0f, 0.5f, 0.2f, 1.0f), 150, mouseX, mouseY);
                muzzleFlash.setSoftnessLength(0f);

                // Удаление света через 0.1 секунды
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        muzzleFlash.remove();
                    }
                }, 0.05f);

                for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class, CollisionComponent.class, SpriteComponent.class).get())) {
                    HealthComponent health = healthMapper.get(entity);
                    CollisionComponent collision = collisionMapper.get(entity);
                    SpriteComponent sprite = spriteMapper.get(entity);

                    if (collision.bounds.contains(mouseX, mouseY)) {
                        if (!health.isDead) {
                            health.health -= weapon.getDamage();

                            if (health.health <= 0) {
                                health.isDead = true;
                                sprite.setAnimation(sprite.deathAnimation);
                                sprite.isHurtOrDead = true;
                                if (entity.getComponent(EnemyTagComponent.class) != null) {
                                    countDeathEnemy++;
                                    HUDSystem hudSystem = getEngine().getSystem(HUDSystem.class);
                                    if (hudSystem != null) {
                                        hudSystem.addScore(100);
                                    }

                                    int droppedAmmo = AmmoDropUtil.getRandomAmmoAmount();
                                    WeaponType droppedAmmoType = AmmoDropUtil.getRandomWeaponType();

                                    if (droppedAmmoType == WeaponType.PISTOL) {
                                        player.changeAmmoAmountForWeapon(0, droppedAmmo);
                                    } else if (droppedAmmoType == WeaponType.RIFLE) {
                                        player.changeAmmoAmountForWeapon(1, droppedAmmo);
                                    }

                                    System.out.println("Dropped ammo: " + droppedAmmo + " for " + droppedAmmoType);

                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            getEngine().removeEntity(entity);
                                        }
                                    }, sprite.deathAnimation.getAnimationDuration() - 0.05f);
                                }
                                else if (entity.getComponent(WalkerTagComponent.class) != null) {
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            getEngine().removeEntity(entity);
                                            Main.getInstance().setScreen(new GameOverScreen());
                                        }
                                    }, sprite.deathAnimation.getAnimationDuration() - 0.05f);
                                }

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

