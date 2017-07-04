#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_background;
uniform float u_transition;

void main() {
    gl_FragColor = v_color * texture2D(u_background, v_texCoords) *
        (texture2D(u_texture, v_texCoords).w * u_transition + (1.0 - texture2D(u_texture, v_texCoords)).w * (1.0 - u_transition));
}