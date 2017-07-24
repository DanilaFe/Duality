package com.danilafe.duality.serialized;

import com.badlogic.gdx.utils.ObjectMap;

public class Tiles {

    public static class Tile {
        public String entityName = "wall";

        public String defaultFrame = "default";
        public String wallFrame = "default";
        public String topCornerFrame = "default";
        public String topFrame = "default";
        public String bottomCornerFrame = "default";
        public String bottomFrame = "default";
    }

    public ObjectMap<String, Tile> tileDefinitions = new ObjectMap<>();

}
