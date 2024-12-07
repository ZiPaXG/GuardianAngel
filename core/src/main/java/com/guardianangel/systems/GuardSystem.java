package com.guardianangel.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.guardianangel.components.HealthComponent;
import com.guardianangel.components.PositionComponent;

public class GuardSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        for (Entity entity : getEngine().getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class).get())) {
        }
    }
}
