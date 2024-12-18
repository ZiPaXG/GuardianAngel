package com.guardianangel.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component{
    public int health;
    public int maxHealth;
    public boolean isDead;

    public HealthComponent(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.isDead = false;
    }
}

