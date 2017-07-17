package com.danilafe.duality.ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class FrictionEntity implements Component, Pool.Poolable {

    public float reduceAmount = 1;

    @Override
    public void reset() {
        reduceAmount = 1;
    }

}
