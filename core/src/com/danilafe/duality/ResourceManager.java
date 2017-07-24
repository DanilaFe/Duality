package com.danilafe.duality;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.danilafe.duality.animation.AnimationData;
import com.danilafe.duality.serialized.Resources;
import com.danilafe.duality.serialized.Tiles;

public class ResourceManager {

    private AssetManager assetManager;
    private ObjectMap<String, AnimationData> animations;
    private ObjectMap<String, String> textures;
    private ObjectMap<String, ShaderProgram> shaders;
    private ObjectMap<String, Tiles.Tile> tiles;

    public ResourceManager() {
        Resources resources = new Json().fromJson(Resources.class, Gdx.files.internal("resources.json"));
        Tiles tiles = new Json().fromJson(Tiles.class, Gdx.files.internal("tiles.json"));
        loadAssets(resources);
        loadShaders(resources);
        loadTextures(resources);
        loadAnimations(resources);
        loadTiles(tiles);
    }

    private void loadAssets(Resources sources) {
        assetManager = new AssetManager();
        for (String rawTexture : sources.rawTextures) {
            assetManager.load("textures/" + rawTexture, Texture.class);
        }
        assetManager.finishLoading();
    }

    private void loadShaders(Resources sources) {
        shaders = new ObjectMap<>();
        ShaderProgram.pedantic = false;
        for (String shader : sources.shaders) {
            shaders.put(shader, new ShaderProgram(
                    Gdx.files.internal("shaders/" + shader + "/vertex.glsl"),
                    Gdx.files.internal("shaders/" + shader + "/fragment.glsl")));
        }
    }

    private void loadTextures(Resources sources) {
        textures = new ObjectMap<>();
        for (String texture : sources.textures.keys()) {
            textures.put(texture, sources.textures.get(texture));
        }
    }

    private void loadAnimations(Resources sources) {
        animations = new ObjectMap<>();
        for (String animation : sources.animations.keys()) {
            Resources.Animation anim = sources.animations.get(animation);
            AnimationData data = new AnimationData(assetManager.get("textures/" + anim.rawTexture, Texture.class),
                    anim.dimensions[0], anim.dimensions[1]);
            for (String subAnimation : anim.animations.keys()) {
                Integer[] copyFrom = anim.animations.get(subAnimation);
                int[] copyInto = new int[copyFrom.length];
                for (int i = 0; i < copyFrom.length; i++) copyInto[i] = copyFrom[i];
                data.registerAnimation(subAnimation, copyInto);
            }
            animations.put(animation, data);
        }
    }

    private void loadTiles(Tiles tilesData){
        tiles = new ObjectMap<>();
        for(String key : tilesData.tileDefinitions.keys()){
            tiles.put(key, tilesData.tileDefinitions.get(key));
        }
    }

    public AnimationData getAnimation(String name) {
        return animations.get(name);
    }

    public Texture getTexture(String name) {
        return assetManager.get("textures/" + textures.get(name), Texture.class);
    }

    public ShaderProgram getShader(String name) {
        return shaders.get(name);
    }

    public Tiles.Tile getTile(String name){
        return tiles.get(name);
    }

}
