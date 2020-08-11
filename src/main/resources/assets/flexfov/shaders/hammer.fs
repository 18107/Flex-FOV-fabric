#version 110

#define M_PI 3.14159265

/* This comes interpolated from the vertex shader */
varying vec2 texcoord;

/* The 6 textures to be rendered */
uniform sampler2D texFront;
uniform sampler2D texBack;
uniform sampler2D texLeft;
uniform sampler2D texRight;
uniform sampler2D texTop;
uniform sampler2D texBottom;

uniform int antialiasing;

uniform vec2 pixelOffset[16];

uniform vec4 backgroundColor;

uniform vec2 cursorPos;

uniform bool drawCursor;

uniform float zoom;

vec3 rotate(vec3 ray, vec2 angle) {

  //rotate y
  float y = -sin(angle.y)*ray.z;
  float z = cos(angle.y)*ray.z;
  ray.y = y;
  ray.z = z;

  //rotate x
  float x = -sin(angle.x)*ray.z;
  z = cos(angle.x)*ray.z;
  ray.x = x;
  ray.z = z;

  return ray;
}

vec3 calcZoom(vec3 ray) {
	float z = ray.z - 1.0;
	float temp = zoom*zoom*(ray.x*ray.x + ray.y*ray.y);
	float denom = temp + z*z;
	float numer = temp - z*z;
	return vec3(-2.0*zoom*ray.x*z/denom, -2.0*zoom*ray.y*z/denom, numer/denom);
}

void main(void) {
  /* Ray-trace a cube */

	//Anti-aliasing
	vec4 colorN[16];

	for (int loop = 0; loop < antialiasing; loop++) {

		//create ray
		vec3 ray = vec3(0.0, 0.0, -1.0);

		//hammer stuff http://paulbourke.net/geometry/transformationprojection/
		float x = (texcoord.x+pixelOffset[loop].x);
		float y = (texcoord.y+pixelOffset[loop].y);
		float z = sqrt(1.0 - x*x/2.0 - y*y/2.0);
		float longitude = 2.0*atan((sqrt(2.0)*z*x)/(2.0*z*z - 1.0));
		float latitude = asin(sqrt(2.0)*z*y);

		if (x*x + y*y > 1.0) {
			gl_FragColor = backgroundColor;
			return;
		}

		//rotate ray
		ray = rotate(ray, vec2(longitude, latitude));
		ray = calcZoom(ray);

		//find which side to use
		if (abs(ray.x) > abs(ray.y)) {
			if (abs(ray.x) > abs(ray.z)) {
				if (ray.x > 0.0) {
					//right
					float x = ray.z / ray.x;
					float y = ray.y / ray.x;
					colorN[loop] = vec4(texture2D(texRight, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				} else {
					//left
					float x = -ray.z / -ray.x;
					float y = ray.y / -ray.x;
					colorN[loop] = vec4(texture2D(texLeft, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				}
			} else {
				if (ray.z > 0.0) {
					//back
					float x = -ray.x / ray.z;
					float y = ray.y / ray.z;
					colorN[loop] = vec4(texture2D(texBack, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				} else {
					//front
					float x = ray.x / -ray.z;
					float y = ray.y / -ray.z;
					colorN[loop] = vec4(texture2D(texFront, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				}
			}
		} else {
			if (abs(ray.y) > abs(ray.z)) {
				if (ray.y > 0.0) {
					//top
					float x = ray.x / ray.y;
					float y = ray.z / ray.y;
					colorN[loop] = vec4(texture2D(texTop, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				} else {
					//bottom
					float x = ray.x / -ray.y;
					float y = -ray.z / -ray.y;
					colorN[loop] = vec4(texture2D(texBottom, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				}
			} else {
				if (ray.z > 0.0) {
					//back
					float x = -ray.x / ray.z;
					float y = ray.y / ray.z;
					colorN[loop] = vec4(texture2D(texBack, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				} else {
					//front
					float x = ray.x / -ray.z;
					float y = ray.y / -ray.z;
					colorN[loop] = vec4(texture2D(texFront, vec2((x+1.0)/2.0, (y+1.0)/2.0)).rgb, 1.0);
				}
			}
		}

		if (drawCursor) {
			vec2 normalAngle = cursorPos*2.0 - 1.0;
			float x = ray.x / -ray.z;
			float y = ray.y / -ray.z;
			if (x <= normalAngle.x + 0.01 && y <= normalAngle.y + 0.01 &&
				x >= normalAngle.x - 0.01 && y >= normalAngle.y - 0.01 &&
				ray.z < 0.0) {
				colorN[loop] = vec4(1.0, 1.0, 1.0, 1.0);
			}
		}
	}

	if (antialiasing == 16) {
	  vec4 corner[4];
	  corner[0] = mix(mix(colorN[0], colorN[1], 2.0/3.0), mix(colorN[4], colorN[5], 3.0/5.0), 5.0/8.0);
	  corner[1] = mix(mix(colorN[3], colorN[2], 2.0/3.0), mix(colorN[7], colorN[6], 3.0/5.0), 5.0/8.0);
	  corner[2] = mix(mix(colorN[12], colorN[13], 2.0/3.0), mix(colorN[8], colorN[9], 3.0/5.0), 5.0/8.0);
	  corner[3] = mix(mix(colorN[15], colorN[14], 2.0/3.0), mix(colorN[11], colorN[10], 3.0/5.0), 5.0/8.0);
	  gl_FragColor = mix(mix(corner[0], corner[1], 0.5), mix(corner[2], corner[3], 0.5), 0.5);
	}
	else if (antialiasing == 4) {
		gl_FragColor = mix(mix(colorN[0], colorN[1], 0.5), mix(colorN[2], colorN[3], 0.5), 0.5);
	}
	else { //if antialiasing == 1
		gl_FragColor = colorN[0];
	}
}
