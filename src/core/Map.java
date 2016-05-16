package core;

import entity.Enemy;

// stage를 구상한다.
public class Map {

	private GamePanel panel;

	public void nextStage() {

		panel.enemies.clear();

		if (panel.stage == 1) {
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(1, 5, 2, 1));
			}
		}
		if (panel.stage == 2) {
			for (int i = 0; i < panel.enemybullets.size(); i++) {
				panel.enemybullets.remove(i);
				i--;
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(1, 5, 2, 2));
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(2, 5, 3, 3));
			}
		}
		if (panel.stage == 3) {
			for (int i = 0; i < panel.enemybullets.size(); i++) {
				panel.enemybullets.remove(i);
				i--;
			}
			for (int i = 0; i < 5; i++) {
				panel.enemies.add(new Enemy(1, 10, 2, 3));
			}
			for (int i = 0; i < 5; i++) {
				panel.enemies.add(new Enemy(2, 10, 3, 4));
			}
		}
		if (panel.stage == 4) {
			for (int i = 0; i < panel.enemybullets.size(); i++) {
				panel.enemybullets.remove(i);
				i--;
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(1, 5, 2, 1));
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(2, 10, 3, 3));
			}
			panel.enemies.add(new Enemy(3, 30, 3, 40));
		}
		if (panel.stage == 5) {
			for (int i = 0; i < panel.enemybullets.size(); i++) {
				panel.enemybullets.remove(i);
				i--;
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(1, 15, 2, 3));
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(2, 15, 3, 5));
			}
			for (int i = 0; i < 4; i++) {
				panel.enemies.add(new Enemy(3, 40, 3, 50));
			}
		}
		if (panel.stage == 6) {
			panel._running = false;
		}
	}
}
