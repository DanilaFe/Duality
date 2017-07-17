package com.danilafe.duality.ecs.components.overlap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class OverlapTracker implements Component, Pool.Poolable {

    public Array<Entity> entities = new Array<>();

    @Override
    public void reset() {
        entities.clear();
    }

}
