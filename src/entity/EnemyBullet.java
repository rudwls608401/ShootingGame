package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GamePanel;

public class EnemyBullet implements Bullet {

	// FIELDS
	private double x;
	private double y;
	private int r;

	private double dx;
	private double dy;
	private double rad;
	private double speed;

	private Color color1;

	public EnemyBullet(double angle, double x, double y) {

		this.x = x;
		this.y = y;
		r = 2;

		rad = Math.toRadians(angle);
		speed = 1;
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		color1 = Color.RED;

	}

	public void setx(double x) {
		x = x;
	}

	public void sety(double y) {
		y = y;
	}

	public void setr(double r) {
		r = r;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public void setdx(double dx) {
		dx = dx;
	}

	public void setdy(double dy) {
		dy = dy;
	}

	public double getdx() {
		return dx;
	}

	public double getdy() {
		return dy;
	}

	public void update() {
		x += dx;
		y += dy;
	}

	public void draw(Graphics2D g) {
		g.setColor(color1);
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	}

}
