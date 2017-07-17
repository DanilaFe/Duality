package com.danilafe.duality.ecs.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class Colored implements Component, Pool.Poolable {

    public Color color = new Color(Color.WHITE);

    @Override
    public void reset() {
        color.set(Color.WHITE);
    }

}
