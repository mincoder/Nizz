package com.mojang.rubydung;

import com.mojang.rubydung.phys.AABB;

public class Player {
	private com.mojang.rubydung.level.Level level;
	public float xo;
	public float yo;
	public float zo;
	public float x;
	public float y;
	public float z;
	public float xd;
	public float yd;
	public float zd;
	public float yRot;
	public float xRot;
	public AABB bb;
	public boolean onGround = false;

	public Player(com.mojang.rubydung.level.Level level) {
		this.level = level;
		resetPos();
	}

	private void resetPos() {
		float x = (float) Math.random() * level.width;
		float y = level.depth + 10;
		float z = (float) Math.random() * level.height;
		setPos(x, y, z);
	}

	private void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		float w = 0.3F;
		float h = 0.9F;
		bb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
	}

	public void turn(float xo, float yo) {
		yRot = ((float) (yRot + xo * 0.15D));
		xRot = ((float) (xRot - yo * 0.15D));
		if (xRot < -90.0F)
			xRot = -90.0F;
		if (xRot > 90.0F)
			xRot = 90.0F;
	}

	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		float xa = 0.0F;
		float ya = 0.0F;

		if (org.lwjgl.input.Keyboard.isKeyDown(19)) {
			resetPos();
		}
		if ((org.lwjgl.input.Keyboard.isKeyDown(200))
				|| (org.lwjgl.input.Keyboard.isKeyDown(17)))
			ya -= 1.0F;
		if ((org.lwjgl.input.Keyboard.isKeyDown(208))
				|| (org.lwjgl.input.Keyboard.isKeyDown(31)))
			ya += 1.0F;
		if ((org.lwjgl.input.Keyboard.isKeyDown(203))
				|| (org.lwjgl.input.Keyboard.isKeyDown(30)))
			xa -= 1.0F;
		if ((org.lwjgl.input.Keyboard.isKeyDown(205))
				|| (org.lwjgl.input.Keyboard.isKeyDown(32)))
			xa += 1.0F;
		if ((org.lwjgl.input.Keyboard.isKeyDown(57))
				|| (org.lwjgl.input.Keyboard.isKeyDown(219))) {
			if (onGround) {
				yd = 0.12F;
			}
		}

		moveRelative(xa, ya, onGround ? 0.02F : 0.005F);

		yd = ((float) (yd - 0.005D));
		move(xd, yd, zd);
		xd *= 0.91F;
		yd *= 0.98F;
		zd *= 0.91F;

		if (onGround) {
			xd *= 0.8F;
			zd *= 0.8F;
		}
	}

	public void move(float xa, float ya, float za) {
		float xaOrg = xa;
		float yaOrg = ya;
		float zaOrg = za;

		java.util.List<AABB> aABBs = level.getCubes(bb.expand(xa, ya, za));

		for (int i = 0; i < aABBs.size(); i++)
			ya = ((AABB) aABBs.get(i)).clipYCollide(bb, ya);
		bb.move(0.0F, ya, 0.0F);

		for (int i = 0; i < aABBs.size(); i++)
			xa = ((AABB) aABBs.get(i)).clipXCollide(bb, xa);
		bb.move(xa, 0.0F, 0.0F);

		for (int i = 0; i < aABBs.size(); i++)
			za = ((AABB) aABBs.get(i)).clipZCollide(bb, za);
		bb.move(0.0F, 0.0F, za);

		onGround = ((yaOrg != ya) && (yaOrg < 0.0F));

		if (xaOrg != xa)
			xd = 0.0F;
		if (yaOrg != ya)
			yd = 0.0F;
		if (zaOrg != za) {
			zd = 0.0F;
		}
		x = ((bb.x0 + bb.x1) / 2.0F);
		y = (bb.y0 + 1.62F);
		z = ((bb.z0 + bb.z1) / 2.0F);
	}

	public void moveRelative(float xa, float za, float speed) {
		float dist = xa * xa + za * za;
		if (dist < 0.01F) {
			return;
		}
		dist = speed / (float) Math.sqrt(dist);
		xa *= dist;
		za *= dist;

		float sin = (float) Math.sin(yRot * 3.141592653589793D / 180.0D);
		float cos = (float) Math.cos(yRot * 3.141592653589793D / 180.0D);

		xd += xa * cos - za * sin;
		zd += za * cos + xa * sin;
	}
}
