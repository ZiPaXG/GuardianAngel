package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PathComponent;
import com.guardianangel.components.PositionComponent;
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
    }
}
