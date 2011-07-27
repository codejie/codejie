#ifndef VEC3D_H
#define VEC3D_H

//from http://code.google.com/p/wowmodelviewer/source/browse/trunk/src/vec3d.h

#include <iostream>
#include <cmath>

#define PI 3.1415926535897932384626433832795
#define PIOVER180 (PI/180.0)

class vec3d 
{
public:
	float x,y,z;

	vec3d(float x0 = 0.0f, float y0 = 0.0f, float z0 = 0.0f) : x(x0), y(y0), z(z0) {}
	vec3d(const vec3d& v) : x(v.x), y(v.y), z(v.z) {}
	void reset()
	{
		x = y = z = 0.0f;
	}

	vec3d& operator= (const vec3d &v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return *this;
	}

	/*
	float[3] operator= (const vec3d &v)
	{
		float f[3] = {v.x, v.y, v.z};
		return f;
	}
	*/

	vec3d operator + (const vec3d &v) const
	{
		vec3d r(x+v.x,y+v.y,z+v.z);
		return r;
	}

	vec3d operator - (const vec3d &v) const
	{
		vec3d r(x-v.x,y-v.y,z-v.z);
		return r;
	}

	float operator* (const vec3d &v) const
	{
		return x*v.x + y*v.y + z*v.z;
	}

	vec3d operator* (float d) const
	{
		vec3d r(x*d,y*d,z*d);
		return r;
	}

	vec3d operator/ (float d) const
	{
		vec3d r(x/d,y/d,z/d);
		return r;
	}

	friend vec3d operator* (float d, const vec3d& v)
	{
		return v * d;
	}

	// Cross Product
	vec3d operator% (const vec3d &v) const
	{
		vec3d r(y*v.z-z*v.y, z*v.x-x*v.z, x*v.y-y*v.x);
		return r;
	}

	vec3d& operator+= (const vec3d &v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
		return *this;
	}

	vec3d& operator-= (const vec3d &v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return *this;
	}

	vec3d& operator*= (float d)
	{
		x *= d;
		y *= d;
		z *= d;
		return *this;
	}

	float lengthSquared() const
	{
		return x*x+y*y+z*z;
	}

	float length() const
	{
		return sqrtf(x*x+y*y+z*z);
	}

	vec3d& normalize()
	{
		this->operator*= (1.0f/length());
		return *this;
	}

	vec3d operator~ () const
	{
		vec3d r(*this);
		r.normalize();
		return r;
	}

	friend std::istream& operator>>(std::istream& in, vec3d& v)
	{
		in >> v.x >> v.y >> v.z;
		return in;
	}

	operator float*()
	{
		return (float*)this;
	}

};


class vec2d
{
public:
	float x,y;

	vec2d(float x0 = 0.0f, float y0 = 0.0f) : x(x0), y(y0) {}
	vec2d(const vec2d& v) : x(v.x), y(v.y) {}

	vec2d& operator= (const vec2d &v)
	{
		x = v.x;
		y = v.y;
		return *this;
	}

	vec2d operator+ (const vec2d &v) const
	{
		vec2d r(x+v.x,y+v.y);
		return r;
	}

	vec2d operator- (const vec2d &v) const
	{
		vec2d r(x-v.x,y-v.y);
		return r;
	}

	float operator* (const vec2d &v) const
	{
		return x*v.x + y*v.y;
	}

	vec2d operator* (float d) const
	{
		vec2d r(x*d,y*d);
		return r;
	}

	friend vec2d operator* (float d, const vec2d& v)
	{
		return v * d;
	}

	vec2d& operator+= (const vec2d &v)
	{
		x += v.x;
		y += v.y;
		return *this;
	}

	vec2d& operator-= (const vec2d &v)
	{
		x -= v.x;
		y -= v.y;
		return *this;
	}

	vec2d& operator*= (float d)
	{
		x *= d;
		y *= d;
		return *this;
	}

	float lengthSquared() const
	{
		return x*x+y*y;
	}


	float length() const
	{
		return sqrtf(x*x+y*y);
	}

	vec2d& normalize()
	{
		this->operator*= (1.0f/length());
		return *this;
	}

	vec2d operator~ () const
	{
		vec2d r(*this);
		r.normalize();
		return r;
	}


	friend std::istream& operator>>(std::istream& in, vec2d& v)
	{
		in >> v.x >> v.y;
		return in;
	}

	operator float*()
	{
		return (float*)this;
	}

};


inline void rotate(float x0, float y0, float *x, float *y, float angle)
{
	float xa = *x - x0, ya = *y - y0;
	*x = xa*cosf(angle) - ya*sinf(angle) + x0;
	*y = xa*sinf(angle) + ya*cosf(angle) + y0;
}

#endif
