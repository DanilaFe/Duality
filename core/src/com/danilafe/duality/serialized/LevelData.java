package com.danilafe.duality.serialized;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelData {

    public static class Tile {
        public String entityName = "wall";

        public String defaultFrame = "default";
        public String wallFrame = "default";
        public String topCornerFrame = "default";
        public String topFrame = "default";
        public String bottomCornerFrame = "default";
        public String bottomFrame = "default";
    }

    public static class Coordinate {
        public int x = 0;
        public int y = 0;
        public String tile = "wall";
    }

    public static class Chunk {
        public Coordinate[] tiles = new Coordinate[0];
        public Vector2 offset = new Vector2();
    }

    public ObjectMap<String, Tile> tileDefinitions = new ObjectMap<>();
    public Chunk[] chunks = new Chunk[0];

}
