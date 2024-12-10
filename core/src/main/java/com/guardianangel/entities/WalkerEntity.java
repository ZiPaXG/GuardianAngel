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

        Texture spriteSheet = new Texture("Characters/MainCharacter/Cyborg_idle.png");
        SpriteComponent sprite = new SpriteComponent(spriteSheet, 4, 0.2f);
        this.add(sprite);
    }

}
