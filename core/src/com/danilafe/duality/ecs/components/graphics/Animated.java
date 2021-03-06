package com.danilafe.duality.ecs.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.danilafe.duality.animation.AnimationData;

public class Animated implements Component, Pool.Poolable {

    public AnimationData animationData;
    public ImmutableArray<Integer> currentFrames;
    public String currentAnimation;
    public int currentFrame;
    public float frameDelay;
    public float currentDelay;
    public boolean loop;
    public boolean flipVertical;
    public boolean flipHorizontal;

    public void play(String name, boolean doLoop) {
        if (currentAnimation == null || !currentAnimation.equals(name)) {
            currentFrames = animationData.getAnimation(name);
            currentAnimation = name;
            currentFrame = 0;
            currentDelay = frameDelay;
        }
        loop = doLoop;
    }

    @Override
    public void reset() {
        animationData = null;
        currentFrames = null;
        currentAnimation = null;
        currentFrame = 0;
        frameDelay = 0;
        currentDelay = 0;
        loop = false;
        flipVertical = false;
        flipHorizontal = false;
    }

}
