package com.guardianangel.entities;

import com.badlogic.ashley.core.Entity;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;

public class GuardEntity extends Entity {
    public GuardEntity(float x, float y, int initialHealth) {
        PositionComponent position = new PositionComponent();
        position.x = x;
        position.y = y;
        this.add(position);

        HealthComponent health = new HealthComponent();
        health.health = initialHealth;
        this.add(health);
    }
}
