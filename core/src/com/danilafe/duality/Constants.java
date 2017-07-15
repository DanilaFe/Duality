package com.danilafe.duality;

import com.badlogic.gdx.graphics.Color;

import java.util.function.Function;

public class Constants {

    public static final float TILE_SIZE = 16;
    public static final int VIEW_WIDTH = 128;
    public static final float TRANSITION_DURATION = .5f;
    public static final Color CLEAR_COLOR = new Color(Color.BLACK);
    public static final float PLAYER_ACCELERATION = 256;
    public static final float PLAYER_VELOCITY_LIMIT = 96;
    public static final float PLAYER_VELOCITY_JUMP = 172;
    public static final float PLAYER_FRICTION_STANDING = 1;
    public static final float PLAYER_FRICTION_MOVING = .25f;
    public static final int POSITION_DEPENDENCE = 128;
    public static final float SWAP_DELAY = 1f;
    public static final float PUSH_DECREASE = .75f;
    public static final Function<Float, Float> FOLLOW_CURVE =
            (x) -> (float) (x / Math.sqrt(1 + Math.pow(x, 2)));
}
