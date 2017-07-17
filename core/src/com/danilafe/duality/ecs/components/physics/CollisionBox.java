package com.danilafe.duality.ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class CollisionBox implements Component, Pool.Poolable {

    public Rectangle box = new Rectangle(0, 0, 0, 0);

    @Override
    public void reset() {
        box.set(0, 0, 0, 0);
    }

}
