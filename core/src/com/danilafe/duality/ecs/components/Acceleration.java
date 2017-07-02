package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Acceleration implements Component, Pool.Poolable {

    public Vector2 acceleration = new Vector2();

    @Override
    public void reset() {
        acceleration.set(0, 0);
    }

}
