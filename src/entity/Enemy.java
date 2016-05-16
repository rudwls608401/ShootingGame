package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import core.GamePanel;

public class Enemy implements SpaceShip {

	private double x;
	private double y;
	private int r;

	private double dx;// x의 순간변화량
	private double dy;
	private double rad;
	private double speed;

	private int health;
	private int type;
	private int rank;

	private Color color;

	private boolean ready;
	private boolean dead;

	private boolean hit;
	private long hitTimer;

	private boolean slow;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	public Enemy(int type, int r, int speed, int health) {

		x = Math.random() * GamePanel.WIDTH / 2;
		y = -r;

		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);

		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		ready = false;
		dead = false;

		hit = false;
		hitTimer = 0;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 1500;

		this.type = type;
		this.r = r;
		this.speed = speed;
		this.health = health;

		// 초급 level 적
		if (type == 1) {
			color = Color.BLUE;
		}

		// 중급 level 적
		if (type == 2) {
			color = Color.GREEN;
		}

		// 고급 level 적
		if (type == 3) {
			color = Color.RED;
		}

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
	
	// type마다 점수를 다르게 주어야한다.
	public int getType() {
		return type;
	}

	public boolean isDead() {
		return dead;
	}

	public void setFiring(boolean b) {
		firing = b;
	}

	public void hit() {
		health--;
		if (health <= 0) {
			dead = true;
		}
		hit = true;
		hitTimer = System.nanoTime();
	}

	public void timer() {
		// 총알에 대한 delay를 준다.
		if (hit) {
			long elapsed = (System.nanoTime() - hitTimer) / 1000000;
			if (elapsed > 50) {
				hit = false;
				hitTimer = 0;
			}
		}

		// 총알에 대한 delay를 준다.
		long elapsed = (System.nanoTime() - firingTimer) / 1000000;

		if (elapsed > firingDelay) {

			firingTimer = System.nanoTime();// use

			GamePanel.enemybullets.add(new EnemyBullet(90, x, y));
		}

	}

	public void update() {

		x += dx;
		y += dy;

		if (!ready) {
			if (x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
				ready = true;
			}
		}

		// frame밖으로 나가지 않게 해줌.
		if (x < r && dx < 0)
			dx = -dx;
		if (y < r && dy < 0)
			dy = -dy;
		if (x > GamePanel.WIDTH - r && dx > 0)
			dx = -dx;
		if (y > GamePanel.HEIGHT - r && dy > 0)
			dy = -dy;
		
		timer();

	}

	public void draw(Graphics2D g) {

		if (hit) {
			g.setColor(Color.WHITE);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		} else {
			g.setColor(color);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);

			g.setStroke(new BasicStroke(3));
			g.setColor(color.WHITE);
			g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
	}

}
