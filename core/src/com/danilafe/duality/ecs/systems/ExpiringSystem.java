package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.ecs.EntityUtils;
import com.danilafe.duality.ecs.components.particle.Expiring;

public class ExpiringSystem extends IteratingSystem {

    public ExpiringSystem() {
        super(Family.all(Expiring.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if ((entity.getComponent(Expiring.class).expireTime -= deltaTime) < 0)
            EntityUtils.safeDelete(getEngine(), entity);
    }
}
