package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class Expiring implements Component, Pool.Poolable {

    public float expireTime = Float.MAX_VALUE;

    @Override
    public void reset() {
        expireTime = Float.MAX_VALUE;
    }
}
