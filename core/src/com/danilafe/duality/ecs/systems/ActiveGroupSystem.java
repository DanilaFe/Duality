package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.danilafe.duality.Constants;
import com.danilafe.duality.ecs.components.game.Input;
import com.danilafe.duality.ecs.components.graphics.Camera;
import com.danilafe.duality.ecs.components.graphics.CameraTracker;
import com.danilafe.duality.ecs.components.physics.Position;
import com.danilafe.duality.ecs.components.positioning.Average;
import com.danilafe.duality.ecs.components.positioning.Following;
import com.danilafe.duality.ecs.components.switching.ActiveGroup;

public class ActiveGroupSystem extends EntitySystem {

    private float delay;
    private int currentGroup;
    public IntIntMap nextGroupMap;
    public IntMap<Boolean> groupTransitions;

    public ActiveGroupSystem() {
        nextGroupMap = new IntIntMap();
        groupTransitions = new IntMap<>();
    }

    private Entity getPositioningEntity(PooledEngine engine) {
        ImmutableArray<Entity> averageEntities = engine.getEntitiesFor(Family.all(Average.class, Position.class).get());
        if (averageEntities.size() > 0) return averageEntities.first();

        Entity newEntity = engine.createEntity();
        newEntity.add(engine.createComponent(Average.class));
        newEntity.add(engine.createComponent(Position.class));
        engine.addEntity(newEntity);
        return newEntity;
    }

    public void switchGroup(int id) {
        PooledEngine engine = (PooledEngine) getEngine();
        Entity averageEntity = getPositioningEntity(engine);
        Array<Entity> cameraEntities = new Array<>();
        ImmutableArray<Entity> activeGroups = engine.getEntitiesFor(Family.all(ActiveGroup.class).get());

        for (Entity cameraEntity : activeGroups) {
            ActiveGroup group = cameraEntity.getComponent(ActiveGroup.class);
            if (group.switchId == id && cameraEntity.getComponent(CameraTracker.class) != null) {
                cameraEntities.add(cameraEntity);
            }
        }
        if (cameraEntities.size == 0) return;
        for (Entity cameraEntity : activeGroups) {
            ActiveGroup group = cameraEntity.getComponent(ActiveGroup.class);
            group.active = group.switchId == id;
        }

        Average averageComponent = averageEntity.getComponent(Average.class);
        averageComponent.entities.clear();
        averageComponent.entities.addAll(cameraEntities);

        engine.getEntitiesFor(Family.all(Following.class, Camera.class).get())
                .first().getComponent(Following.class).otherEntity = averageEntity;
        engine.getSystem(RenderSystem.class).increasing = groupTransitions.get(id);
        currentGroup = id;
    }

    @Override
    public void update(float deltaTime) {
        delay -= deltaTime;
        if (delay < 0) delay = 0;

        for (Entity entity : getEngine().getEntitiesFor(Family.all(Input.class, ActiveGroup.class).get())) {
            if (entity.getComponent(ActiveGroup.class).active &&
                    entity.getComponent(Input.class).controlData.switchPressed() && delay == 0) {
                switchGroup(nextGroupMap.get(currentGroup, 0));
                delay = Constants.SWAP_DELAY;
            }
        }
    }
}
