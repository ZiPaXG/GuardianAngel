package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.guardianangel.components.*;

public class EnemyEntity extends Entity {
    public EnemyEntity(float x, int initialHealth, int enemyType) {
        PositionComponent position = new PositionComponent();
        position.x = x;
        position.y = 241.70013f;
        this.add(position);

        HealthComponent health = new HealthComponent(50);
        this.add(health);

        this.add(new EnemyTagComponent());

        CollisionComponent collision = new CollisionComponent(x, 241.70013f, 64, 96);
        this.add(collision);

        SpriteComponent spriteComponent = getSpriteComponent(enemyType);
        this.add(spriteComponent);
    }

    private static SpriteComponent getSpriteComponent(int enemyType) {
        String basePath;
        if (enemyType == 1) {
            basePath = "Characters/Enemy/Biker/Biker_";
        } else {
            basePath = "Characters/Enemy/Punk/Punk_";
        }

        Texture runSpriteSheet = new Texture(basePath + "run.png");
        Texture hurtSpriteSheet = new Texture(basePath + "hurt.png");
        Texture deathSpriteSheet = new Texture(basePath + "death.png");

        return new SpriteComponent(
            null, 0, 0f,
            runSpriteSheet, 6, 0.1f,
            hurtSpriteSheet, 4, 0.1f,
            deathSpriteSheet, 6, 0.2f
        );
    }
}
