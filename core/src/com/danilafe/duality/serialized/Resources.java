package com.danilafe.duality.serialized;

import com.badlogic.gdx.utils.ObjectMap;

public class Resources {

    public static class Animation {
        public String rawTexture;
        public ObjectMap<String, Integer[]> animations = new ObjectMap<>();
        public int[] dimensions = new int[2];
    }

    public ObjectMap<String, Animation> animations = new ObjectMap<>();
    public ObjectMap<String, String> textures = new ObjectMap<>();
    public String[] shaders = new String[0];
    public String[] rawTextures = new String[0];

}
