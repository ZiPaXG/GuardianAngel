package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.guardianangel.components.*;

import java.util.ArrayList;

public class WalkerEntity extends Entity {
    public WalkerEntity(float x, float y, int initialHealth) {
        PositionComponent position = new PositionComponent();
        position.x = x;
        position.y = y;
        this.add(position);

        HealthComponent health = new HealthComponent();
        health.health = initialHealth;
        this.add(health);

        PathComponent path = new PathComponent();
        path.path = new ArrayList<>();
        this.add(path);

        this.add(new WalkerTagComponent());

        CollisionComponent collision = new CollisionComponent(x, y, 64, 96);
        this.add(collision);

        SpriteComponent spriteComponent = getSpriteComponent();

        this.add(spriteComponent);
    }

    private static SpriteComponent getSpriteComponent() {
        Texture idleSpriteSheet = new Texture("Characters/MainCharacter/Cyborg_idle.png");
        Texture runSpriteSheet = new Texture("Characters/MainCharacter/Cyborg_run.png");
        Texture hurtSpriteSheet = new Texture("Characters/MainCharacter/Cyborg_hurt.png");
        Texture deathSpriteSheet = new Texture("Characters/MainCharacter/Cyborg_death.png");

        return new SpriteComponent(
            idleSpriteSheet, 4, 0.2f,
            runSpriteSheet, 6, 0.1f,
            hurtSpriteSheet, 2, 0.1f,
            deathSpriteSheet, 6, 0.2f
        );
    }
}
