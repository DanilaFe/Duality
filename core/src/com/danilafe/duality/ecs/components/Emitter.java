package com.danilafe.duality.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.danilafe.duality.ecs.Recipe;

public class Emitter implements Pool.Poolable, Component {

    public float currentDelay;
    public float currentEmitDelay;
    public float emitDelay;
    public float particleDensity = 1.f / 256;
    public Recipe particleRecipe;

    @Override
    public void reset() {
        currentDelay = 0;
        currentEmitDelay = 0;
        emitDelay = 0;
        particleDensity = 1.f / 256;
        particleRecipe = null;
    }

}
