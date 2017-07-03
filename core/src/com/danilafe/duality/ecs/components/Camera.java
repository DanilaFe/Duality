package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Camera implements Component, Pool.Poolable {

    public OrthographicCamera camera = null;
    public Vector2 offset = new Vector2();

    @Override
    public void reset() {
        camera = null;
        offset.set(0, 0);
    }
}
