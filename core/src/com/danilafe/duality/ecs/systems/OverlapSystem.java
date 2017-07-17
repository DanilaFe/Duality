package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.components.physics.CollisionBox;
import com.danilafe.duality.ecs.components.overlap.OverlapTracker;
import com.danilafe.duality.ecs.components.overlap.Overlapping;
import com.danilafe.duality.ecs.systems.util.DualSystem;

public class OverlapSystem extends DualSystem {

    public OverlapSystem() {
        super(Family.all(Overlapping.class, CollisionBox.class).get(),
                Family.all(OverlapTracker.class, CollisionBox.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity tracker : entitiesB) {
            OverlapTracker trackerComponent = tracker.getComponent(OverlapTracker.class);
            CollisionBox collisionBox = tracker.getComponent(CollisionBox.class);

            trackerComponent.entities.clear();
            for (Entity overlapCheck : entitiesA) {
                CollisionBox otherCollisionBox = overlapCheck.getComponent(CollisionBox.class);
                if (collisionBox.box.overlaps(otherCollisionBox.box))
                    trackerComponent.entities.add(overlapCheck);
            }
        }
    }

}
