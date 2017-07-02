package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Velocity implements Component, Pool.Poolable {

    public Vector2 velocity;

    @Override
    public void reset() {
        velocity.set(0, 0);
    }

}
