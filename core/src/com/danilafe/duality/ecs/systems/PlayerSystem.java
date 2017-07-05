package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.danilafe.duality.ecs.components.Acceleration;
import com.danilafe.duality.ecs.components.Player;
import com.danilafe.duality.ecs.components.Velocity;

public class PlayerSystem extends IteratingSystem {

    static final float PLAYER_ACCELERATION = 32;

    public PlayerSystem(){
        super(Family.all(Player.class, Acceleration.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Player player = entity.getComponent(Player.class);
        Acceleration acceleration = entity.getComponent(Acceleration.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        if(!player.active) {
            acceleration.acceleration.x = 0;
            return;
        }

        float xAccel = 0;
        if(Gdx.input.isKeyPressed(player.leftKey)) xAccel -= PLAYER_ACCELERATION;
        if(Gdx.input.isKeyPressed(player.rightKey)) xAccel += PLAYER_ACCELERATION;
        acceleration.acceleration.x = xAccel;

        if(Gdx.input.isKeyJustPressed(player.jumpKey)) velocity.velocity.y = -128;
    }

}
