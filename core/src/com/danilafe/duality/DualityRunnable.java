package com.danilafe.duality;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.danilafe.duality.controls.ControlManager;
import com.danilafe.duality.ecs.EntityUtils;
import com.danilafe.duality.ecs.recipe.RecipeDatabase;

public abstract class DualityRunnable {

    public abstract void run(PooledEngine run, ResourceManager resources,
                    RecipeDatabase recipes, ControlManager controls);

    public void clearEntities(Engine engine){
        ImmutableArray<Entity> entities = engine.getEntities();
        while(entities.size() != 0) EntityUtils.safeDelete(engine, entities.first());
    }

}
