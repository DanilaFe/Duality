package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.danilafe.duality.ecs.components.*;
import com.danilafe.duality.ecs.systems.util.DualSystem;
import com.danilafe.duality.level.Level;

public class LevelSystem extends DualSystem {

    public Level currentLevel;
    public Level toLoad;

    public LevelSystem() {
        super(Family.all(Player.class, ActiveGroup.class, Input.class).get(),
                Family.all(OverlapTracker.class, LevelPortal.class).get());
    }

    @Override
    public void update(float deltaTime) {
        toLoad = null;
        for (Entity portalEntity : entitiesB) {
            LevelPortal portal = portalEntity.getComponent(LevelPortal.class);
            OverlapTracker tracker = portalEntity.getComponent(OverlapTracker.class);

            boolean canTransition = true;
            boolean transitionRequested = false;
            for (Entity otherEntity : entitiesA) {
                if (otherEntity.getComponent(ActiveGroup.class).active) {
                    canTransition &= tracker.entities.contains(otherEntity, false);
                    transitionRequested = transitionRequested || otherEntity.getComponent(Input.class).controlData.interactPressed();
                }
            }

            if (canTransition && transitionRequested && portal.levelSupplier != null) {
                toLoad = portal.levelSupplier.get();
                return;
            }
        }
    }
}
