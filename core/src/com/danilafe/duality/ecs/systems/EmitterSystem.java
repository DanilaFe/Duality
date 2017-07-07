package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.ResourceManager;
import com.danilafe.duality.ecs.RecipeDatabase;
import com.danilafe.duality.ecs.components.CollisionBox;
import com.danilafe.duality.ecs.components.Emitter;

public class EmitterSystem extends IteratingSystem {

    public ResourceManager resources;

    public EmitterSystem(){
        super(Family.all(CollisionBox.class, Emitter.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Emitter emitter = entity.getComponent(Emitter.class);
        CollisionBox box = entity.getComponent(CollisionBox.class);

        emitter.currentEmitDelay = emitter.emitDelay / (box.box.area() * emitter.particleDensity);
        emitter.currentDelay -= deltaTime;
        while(emitter.currentDelay < 0 && emitter.emitDelay != 0){
            emitter.currentDelay += emitter.currentEmitDelay;
            getEngine().addEntity(emitter.particleRecipe.create((PooledEngine) getEngine(), resources,
                    box.box.x + (float) Math.random() * box.box.width,
                    box.box.y + (float) Math.random() * box.box.height));
        }
    }

}
