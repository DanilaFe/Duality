package com.danilafe.duality.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public interface GameRunnable {

    public void run(Engine engine, Entity self, float deltaTime);

}
