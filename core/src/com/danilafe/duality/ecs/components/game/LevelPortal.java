package com.danilafe.duality.ecs.components.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.danilafe.duality.level.Level;

import java.util.function.Supplier;

public class LevelPortal implements Component, Pool.Poolable {

    public Supplier<Level> levelSupplier;

    @Override
    public void reset() {
        levelSupplier = null;
    }

}
