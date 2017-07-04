package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class Child implements Component, Pool.Poolable {

    public Entity parent;

    @Override
    public void reset() {
        parent = null;
    }

}
