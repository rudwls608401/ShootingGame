package core;

import javax.swing.JFrame;

public class ShootingGameMain {

	public static void main(String[] args) {

		JFrame window = new JFrame("Space Shooting Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.setContentPane(new GamePanel());

		window.pack();
		window.setVisible(true);
	}
}
