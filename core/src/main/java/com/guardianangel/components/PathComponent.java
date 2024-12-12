package com.guardianangel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class PathComponent implements Component {
    public List<Vector2> path;
    public int currentIndex = 0;
}
