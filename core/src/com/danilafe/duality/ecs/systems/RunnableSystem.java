package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.ecs.components.Runnable;

public class RunnableSystem extends IteratingSystem {

    public RunnableSystem(){
        super(Family.all(Runnable.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Runnable runnable = entity.getComponent(Runnable.class);
        if(runnable.runnable != null) runnable.runnable.run(getEngine(), entity, deltaTime);
    }
}
