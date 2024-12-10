package com.guardianangel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class CollisionComponent implements Component {
    public Rectangle bounds;

    public CollisionComponent(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }
}
