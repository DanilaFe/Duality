package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Rectangle;
import com.danilafe.duality.ecs.components.physics.CollisionBox;
import com.danilafe.duality.ecs.components.physics.Platform;
import com.danilafe.duality.ecs.components.physics.Solid;
import com.danilafe.duality.ecs.components.physics.SurfaceTracker;
import com.danilafe.duality.ecs.systems.util.DualSystem;

public class SurfaceSystem extends DualSystem {

    public SurfaceSystem() {
        super(Family.all(CollisionBox.class).one(Solid.class, Platform.class).get(),
                Family.all(SurfaceTracker.class, CollisionBox.class).get());
    }

    private boolean isOnSurface(Rectangle entity, Rectangle surface) {
        return !(entity.x > surface.x + surface.width || entity.x + entity.width < surface.x) &&
                entity.y == surface.y + surface.height;
    }

    @Override
    public void update(float deltaTime) {
        for (Entity tracker : entitiesB) {
            CollisionBox trackerBox = tracker.getComponent(CollisionBox.class);
            SurfaceTracker trackerComponent = tracker.getComponent(SurfaceTracker.class);
            trackerComponent.onSurface = false;
            if (trackerComponent.lastSurface != null) {
                trackerComponent.onSurface = isOnSurface(trackerBox.box, trackerComponent.lastSurface.getComponent(CollisionBox.class).box);
                if (!trackerComponent.onSurface) trackerComponent.lastSurface = null;
                else continue;
            }

            for (Entity surfaceEntity : entitiesA) {
                trackerComponent.onSurface = isOnSurface(trackerBox.box, surfaceEntity.getComponent(CollisionBox.class).box);
                if (trackerComponent.onSurface) {
                    trackerComponent.lastSurface = surfaceEntity;
                    return;
                }
            }
        }
    }
}
