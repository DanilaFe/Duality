package com.danilafe.duality.ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlatformWalker implements Pool.Poolable, Component {

    public boolean solid = true;

    @Override
    public void reset() {
        solid = true;
    }

}
