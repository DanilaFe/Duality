package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.components.CollisionBox;
import com.danilafe.duality.ecs.components.FrictionCause;
import com.danilafe.duality.ecs.components.FrictionEntity;
import com.danilafe.duality.ecs.components.Velocity;
import com.danilafe.duality.ecs.systems.util.DualSystem;

public class FrictionSystem extends DualSystem {

    public FrictionSystem() {
        super(Family.all(CollisionBox.class, FrictionCause.class).get(),
                Family.all(CollisionBox.class, FrictionEntity.class, Velocity.class).get());
    }


    @Override
    public void update(float deltaTime) {
        for (Entity frictionEntity : entitiesB) {
            CollisionBox box = frictionEntity.getComponent(CollisionBox.class);
            Velocity vel = frictionEntity.getComponent(Velocity.class);
            float reduce = (float) Math.pow(frictionEntity.getComponent(FrictionEntity.class).reduceAmount, deltaTime);

            for (Entity causeEntity : entitiesA) {
                CollisionBox otherBox = causeEntity.getComponent(CollisionBox.class);
                FrictionCause cause = causeEntity.getComponent(FrictionCause.class);

                if (!(box.box.x > otherBox.box.x + otherBox.box.width || box.box.x + box.box.width < otherBox.box.x)
                        && box.box.y == otherBox.box.y + otherBox.box.height) {
                    float friction = (float) Math.pow(cause.frictionAmount, deltaTime);
                    vel.velocity.x *= friction;
                    vel.velocity.x /= reduce;
                }
            }
        }
    }
}
