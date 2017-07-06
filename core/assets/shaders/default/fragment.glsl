#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_background;
uniform float u_transition;
uniform vec4 u_clearColor;

void main() {
    float m_targetOpacity = texture2D(u_texture, v_texCoords).w;
    if(u_clearColor == texture2D(u_texture, v_texCoords)) m_targetOpacity = 0.0;

    gl_FragColor = v_color * texture2D(u_background, v_texCoords) *
        (m_targetOpacity * u_transition + ((1.0 - m_targetOpacity) * (1.0 - u_transition)));
}