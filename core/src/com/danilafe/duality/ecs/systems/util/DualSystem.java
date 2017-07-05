package com.danilafe.duality.ecs.systems.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class DualSystem extends EntitySystem {

    public Family familyA;
    public Family familyB;
    public ImmutableArray<Entity> entitiesA;
    public ImmutableArray<Entity> entitiesB;

    public DualSystem(Family a, Family b) {
        familyA = a;
        familyB = b;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entitiesA = engine.getEntitiesFor(familyA);
        entitiesB = engine.getEntitiesFor(familyB);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        entitiesA = entitiesB = null;
    }
}
