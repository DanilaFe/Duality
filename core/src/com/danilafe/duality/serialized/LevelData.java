package com.danilafe.duality.serialized;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelData {

    private static class Positioned {
        public int[] coords = new int[2];
    }

    public static class Tile {
        public String entityName = "wall";

        public String defaultFrame = "default";
        public String wallFrame = "default";
        public String topCornerFrame = "default";
        public String topFrame = "default";
        public String bottomCornerFrame = "default";
        public String bottomFrame = "default";
    }

    public static class Coordinate extends Positioned {
        public String tile = "wall";
    }

    public static class Chunk {
        public PlayerSpawn[] players = new PlayerSpawn[0];
        public DecorativeEntity[] decorations = new DecorativeEntity[0];
        public Coordinate[] tiles = new Coordinate[0];
        public GeneralEntity[] entities = new GeneralEntity[0];
        public LevelPortal[] levelPortals = new LevelPortal[0];
        public Vector2 offset = new Vector2();
    }

    public static class DecorativeEntity extends Positioned {
        public String resourceName = "wall";
        public String animationName = "default";
        public boolean loop = true;
    }

    public static class PlayerSpawn extends Positioned {
        public String entityName = "player";
        public int switchId = 0;
    }

    public static class GeneralEntity extends Positioned {
        public String entityName = "";
    }

    public static class LevelPortal extends Positioned {
        public String loadType = "internal";
        public String levelName = "";
    }

    public static class SwitchGroup {
        public boolean active = false;
    }

    public ObjectMap<String, Tile> tileDefinitions = new ObjectMap<>();
    public ObjectMap<String, SwitchGroup> groups = new ObjectMap<>();
    public Chunk[] chunks = new Chunk[0];

}
