package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.danilafe.duality.ecs.components.*;

public class ActiveGroupSystem extends EntitySystem {

    public IntIntMap keysToId;
    public IntMap<Boolean> idTransitions;

    public ActiveGroupSystem() {
        keysToId = new IntIntMap();
        idTransitions = new IntMap<>();
    }

    public void registerGroup(int id, int key, boolean transiton) {
        keysToId.put(key, id);
        idTransitions.put(id, transiton);
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
        Array<Entity> cameraEntities = new Array<>();
        ImmutableArray<Entity> activeGroups = engine.getEntitiesFor(Family.all(ActiveGroup.class).get());

        for (Entity cameraEntity : activeGroups) {
            ActiveGroup group = cameraEntity.getComponent(ActiveGroup.class);
            if (group.switchId == id) cameraEntities.add(cameraEntity);
        }
        if (cameraEntities.size == 0) return;
        for (Entity cameraEntity : activeGroups) {
            ActiveGroup group = cameraEntity.getComponent(ActiveGroup.class);
            group.active = group.switchId == id;
        }

        Entity toFollow = null;
        if (cameraEntities.size == 1) {
            toFollow = cameraEntities.first();
        } else if (cameraEntities.size > 1) {
            Average averageComponent = (toFollow = getPositioningEntity(engine)).getComponent(Average.class);
            averageComponent.entities.clear();
            averageComponent.entities.addAll(cameraEntities);
        }

        engine.getEntitiesFor(Family.all(Following.class, Camera.class).get())
                .first().getComponent(Following.class).otherEntity = toFollow;
        engine.getSystem(RenderSystem.class).increasing = idTransitions.get(id);
    }

    @Override
    public void update(float deltaTime) {
        for (IntIntMap.Entry entry : keysToId.entries()) {
            if (Gdx.input.isKeyPressed(entry.key)) switchGroup(entry.value);
        }
    }
}
