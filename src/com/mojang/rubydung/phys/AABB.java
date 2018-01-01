package com.mojang.rubydung.phys;

public class AABB {
	private float epsilon = 0.0F;
	public float x0;
	public float y0;
	public float z0;

	public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
	}

	public float x1;
	public float y1;
	public float z1;

	public AABB expand(float xa, float ya, float za) {
		float _x0 = x0;
		float _y0 = y0;
		float _z0 = z0;
		float _x1 = x1;
		float _y1 = y1;
		float _z1 = z1;

		if (xa < 0.0F)
			_x0 += xa;
		if (xa > 0.0F) {
			_x1 += xa;
		}
		if (ya < 0.0F)
			_y0 += ya;
		if (ya > 0.0F) {
			_y1 += ya;
		}
		if (za < 0.0F)
			_z0 += za;
		if (za > 0.0F) {
			_z1 += za;
		}
		return new AABB(_x0, _y0, _z0, _x1, _y1, _z1);
	}

	public AABB grow(float xa, float ya, float za) {
		float _x0 = x0 - xa;
		float _y0 = y0 - ya;
		float _z0 = z0 - za;
		float _x1 = x1 + xa;
		float _y1 = y1 + ya;
		float _z1 = z1 + za;

		return new AABB(_x0, _y0, _z0, _x1, _y1, _z1);
	}

	public float clipXCollide(AABB c, float xa) {
		if ((y1 <= y0) || (y0 >= y1))
			return xa;
		if ((z1 <= z0) || (z0 >= z1)) {
			return xa;
		}
		if ((xa > 0.0F) && (x1 <= x0)) {
			float max = x0 - x1 - epsilon;
			if (max < xa)
				xa = max;
		}
		if ((xa < 0.0F) && (x0 >= x1)) {
			float max = x1 - x0 + epsilon;
			if (max > xa) {
				xa = max;
			}
		}
		return xa;
	}

	public float clipYCollide(AABB c, float ya) {
		if ((x1 <= x0) || (x0 >= x1))
			return ya;
		if ((z1 <= z0) || (z0 >= z1)) {
			return ya;
		}
		if ((ya > 0.0F) && (y1 <= y0)) {
			float max = y0 - y1 - epsilon;
			if (max < ya)
				ya = max;
		}
		if ((ya < 0.0F) && (y0 >= y1)) {
			float max = y1 - y0 + epsilon;
			if (max > ya) {
				ya = max;
			}
		}
		return ya;
	}

	public float clipZCollide(AABB c, float za) {
		if ((x1 <= x0) || (x0 >= x1))
			return za;
		if ((y1 <= y0) || (y0 >= y1)) {
			return za;
		}
		if ((za > 0.0F) && (z1 <= z0)) {
			float max = z0 - z1 - epsilon;
			if (max < za)
				za = max;
		}
		if ((za < 0.0F) && (z0 >= z1)) {
			float max = z1 - z0 + epsilon;
			if (max > za) {
				za = max;
			}
		}
		return za;
	}

	public boolean intersects(AABB c) {
		if ((x1 <= x0) || (x0 >= x1))
			return false;
		if ((y1 <= y0) || (y0 >= y1))
			return false;
		if ((z1 <= z0) || (z0 >= z1))
			return false;
		return true;
	}

	public void move(float xa, float ya, float za) {
		x0 += xa;
		y0 += ya;
		z0 += za;
		x1 += xa;
		y1 += ya;
		z1 += za;
	}
}
