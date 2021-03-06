package com.danilafe.duality.ecs.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class CameraShake implements Component, Pool.Poolable {

    public float shakeLength;
    public float damping = 1;
    public float threshold = 0.5f;
    public float currentDelay = 0;
    public float moveDelay = 0;
    public Vector2 currentOffset = new Vector2();
    public Vector2 movingTowards = new Vector2();
    public Vector2 movingFrom = new Vector2();

    public void shake(float intensity, float shakeDelay) {
        shakeLength = intensity;
        currentDelay = moveDelay = shakeDelay;
        movingTowards.set(shakeDelay, 0).rotate((float) (Math.random() * 360));
    }

    @Override
    public void reset() {
        shakeLength = 0;
        damping = 1;
        threshold = 0.5f;
        currentOffset.set(0, 0);
        movingTowards.set(0, 0);
        movingFrom.set(0, 0);
        moveDelay = 0;
        currentDelay = 0;
    }

}
