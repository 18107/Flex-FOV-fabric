#version 110

/* The position of the vertex as two-dimensional vector */
attribute vec2 vertex;

/* Write interpolated texture coordinate to fragment shader */
varying vec2 texcoord;

void main(void) {
  gl_Position = vec4(vertex, 0.0, 1.0);
  texcoord = vertex;
}
