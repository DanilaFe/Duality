package com.danilafe.duality.serialized;

import com.badlogic.gdx.utils.ObjectMap;

public class Resources {

    public static class Animation {
        public String rawTexture;
        public ObjectMap<String, Integer[]> animations;
        public int[] dimensions;
    }

    public ObjectMap<String, Animation> animations;
    public ObjectMap<String, String> textures;
    public String[] shaders;
    public String[] rawTextures;

}
