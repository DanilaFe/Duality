package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Position implements Component, Pool.Poolable {

    public Vector2 position;

    @Override
    public void reset() {
        position.set(0, 0);
    }

}
