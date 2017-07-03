package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.function.Function;

/**
 * Created by vanilla on 7/2/17.
 */
public class Following implements Component, Pool.Poolable {

    public static final Function<Float, Float> CURVE =
            (x) -> (float) (x / Math.sqrt(1 + Math.pow(x, 2)));

    public Entity otherEntity = null;
    public Vector2 offset = new Vector2();
    public float maxMove = 0;

    @Override
    public void reset() {
        otherEntity = null;
        offset.set(0, 0);
        maxMove = 0;
    }

}
