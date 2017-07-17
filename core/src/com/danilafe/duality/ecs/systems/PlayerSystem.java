package com.danilafe.duality.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.danilafe.duality.Constants;
import com.danilafe.duality.ecs.components.game.Input;
import com.danilafe.duality.ecs.components.game.Player;
import com.danilafe.duality.ecs.components.graphics.Animated;
import com.danilafe.duality.ecs.components.physics.*;
import com.danilafe.duality.ecs.components.switching.ActiveGroup;

public class PlayerSystem extends IteratingSystem {

    public PlayerSystem() {
        super(Family.all(Player.class, Acceleration.class, Velocity.class, FrictionEntity.class,
                Animated.class, SurfaceTracker.class, ActiveGroup.class, Input.class, PlatformWalker.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActiveGroup group = entity.getComponent(ActiveGroup.class);
        Acceleration acceleration = entity.getComponent(Acceleration.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        FrictionEntity frictionEntity = entity.getComponent(FrictionEntity.class);
        Animated animated = entity.getComponent(Animated.class);
        PlatformWalker walker = entity.getComponent(PlatformWalker.class);
        Input input = entity.getComponent(Input.class);

        if (velocity.velocity.x > Constants.PLAYER_VELOCITY_LIMIT) velocity.velocity.x = Constants.PLAYER_VELOCITY_LIMIT;
        else if (velocity.velocity.x < -Constants.PLAYER_VELOCITY_LIMIT) velocity.velocity.x = -Constants.PLAYER_VELOCITY_LIMIT;
        animated.flipHorizontal = velocity.velocity.x > 0;

        if (!group.active) {
            acceleration.acceleration.x = 0;
            frictionEntity.reduceAmount = Constants.PLAYER_FRICTION_STANDING;
            return;
        }

        float xAccel = input.controlData.horizontalAccel() * Constants.PLAYER_ACCELERATION;
        acceleration.acceleration.x = xAccel;
        frictionEntity.reduceAmount = (xAccel == 0 || Math.signum(xAccel) != Math.signum(velocity.velocity.x)) ? Constants.PLAYER_FRICTION_STANDING : Constants.PLAYER_FRICTION_MOVING;

        walker.solid = !input.controlData.fallPressed();

        if (input.controlData.jumpPressed() && entity.getComponent(SurfaceTracker.class).onSurface)
            velocity.velocity.y = Constants.PLAYER_VELOCITY_JUMP;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
