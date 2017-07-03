package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class FrictionCause implements Component, Pool.Poolable {

    public float frictionAmount = 1;

    @Override
    public void reset() {
        frictionAmount = 1;
    }
}
