package com.danilafe.duality.ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class SurfaceTracker implements Component, Pool.Poolable {

    public boolean onSurface;
    public Entity lastSurface;

    @Override
    public void reset() {
        onSurface = false;
        lastSurface = null;
    }

}
