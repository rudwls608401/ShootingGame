package function;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import core.GamePanel;

public class Power implements Function {

	private double x;// power up item이 나오는 x좌표
	private double y;// power up item이 나오는 y좌표
	private int r;// power up item의 반지름

	private int type;// 어떤 type의 item인지 결정
	private Color color1;

	public Power(int type, double x, double y) {

		this.type = type;
		this.x = x;
		this.y = y;

	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public double getr() {
		return r;
	}

	public int getType() {
		return type;
	}

	@Override
	public void update() {
		y += 2;
	}

	@Override
	public void draw(Graphics2D g) {
		if (type == 1) {
			color1 = Color.PINK;
			r = 5;
			g.setColor(color1);
			g.fillOval((int) (x - r), (int) (y - r), 4 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
		}
		if (type == 2) {
			color1 = Color.YELLOW;
			r = 5;
			g.setColor(color1);
			g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
		}
		if (type == 3) {
			color1 = Color.YELLOW;
			r = 5;
			g.setColor(color1);
			g.fillRect((int) (x - r), (int) (y - r), 4 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
		}
	
	}
}
