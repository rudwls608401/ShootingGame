package core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import entity.Enemy;
import entity.EnemyBullet;
import entity.PlayerBullet;
import entity.Player;
import function.Power;
import function.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static int WIDTH = 500;
	public static int HEIGHT = 500;

	private Thread _thread;
	public static boolean _running;

	public static Player player;
	public static ArrayList<PlayerBullet> playerbullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<EnemyBullet> enemybullets;
	public static ArrayList<Power> powers;

	private BufferedImage _image;
	private Graphics2D _g;

	private int FPS = 30;// FRAME PER SEC
	private double averageFPS;// average FRAME PER SEC

	private Map map;

	private long gameTimer;// game stage timer
	private long gameTimerDifference;
	public static int stage;
	private boolean stageStart;
	private int stageDelay = 2000;
	private JLabel universe1;

	public GamePanel() {
		super();

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		setLayout(new BorderLayout(0, 0));

		JLabel lblBackGround = new JLabel("universe1");
		lblBackGround.setIcon(new ImageIcon("C:\\Users\\rudwl_000\\workspace\\SE_Shooting_Game\\universe1.jpeg"));
		add(lblBackGround);

		requestFocus();
	}

	public void addNotify() {// Keyboard listener시 필요
		super.addNotify();
		if (_thread == null) {
			_thread = new Thread(this);
			_thread.start();// Thread 클래스는 start 실행 시 run 메소드가 수행되도록 코딩되어있다.
		}
		addKeyListener(this);// 키보드 listener를 불러준다.
	}

	// ***********************************************<Thread_Run>***********************************************//

	public void run() {// thread 실행.

		_running = true;

		// background sound
		Sound backgroundsound = new Sound("bg.wav", true);

		_image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		_g = (Graphics2D) _image.getGraphics();
		_g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		_g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// make instance
		player = new Player();
		playerbullets = new ArrayList<PlayerBullet>();
		enemies = new ArrayList<Enemy>();
		enemybullets = new ArrayList<EnemyBullet>();
		powers = new ArrayList<Power>();
		map = new Map();

		// initialize
		gameTimer = 0;
		gameTimerDifference = 0;
		stageStart = true;
		stage = 0;

		long startTime;
		long startTimerDifference;
		long wait;
		long total = 0;
		int viewCount = 0;
		int maxviewCount = 30;
		long goalTime = 1000 / FPS;

		while (_running) {

			startTime = System.nanoTime();

			gameUpdate();
			gamePrint();

			Graphics g2 = this.getGraphics();
			g2.drawImage(_image, 0, 0, null);
			g2.dispose();

			startTimerDifference = (System.nanoTime() - startTime) / 1000000;
			wait = goalTime - startTimerDifference;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {

			}

			total += System.nanoTime() - startTime;
			viewCount++;
			if (viewCount == maxviewCount) {
				averageFPS = 1000.0 / ((total / viewCount) / 1000000);
				viewCount = 0;
				total = 0;
			}
		}

		String s;
		int length;
		_g.setColor(new Color(0, 0, 0));
		_g.fillRect(0, 0, WIDTH, HEIGHT);
		_g.setColor(Color.WHITE);
		_g.setFont(new Font("Century Gothic", Font.PLAIN, 30));
		if (stage == 6) {
			s = "C L E A R";
		} else {
			s = "G A M E   O V E R";
		}
		length = (int) _g.getFontMetrics().getStringBounds(s, _g).getWidth();
		_g.drawString(s, (WIDTH - length) / 2, (HEIGHT) / 2);
		s = "Final Score: " + player.getScore();
		length = (int) _g.getFontMetrics().getStringBounds(s, _g).getWidth();
		_g.drawString(s, (WIDTH - length) / 2, (HEIGHT) / 2 + 30);
		Graphics g2 = this.getGraphics();
		g2.drawImage(_image, 0, 0, null);
		g2.dispose();

	}

	// ***********************************************<Game_Update>***********************************************//

	private void gameUpdate() {

		if (gameTimer == 0 && enemies.size() == 0) {
			stage++;
			stageStart = false;
			gameTimer = System.nanoTime();
		} else {
			gameTimerDifference = (System.nanoTime() - gameTimer) / 1000000;
			if (gameTimerDifference > stageDelay) {
				stageStart = true;
				gameTimer = 0;
				gameTimerDifference = 0;
			}
		}

		if (stageStart && enemies.size() == 0) {
			map.nextStage();
		}

		if (player.getx() < player.getr())
			player.setx(player.getr());
		if (player.gety() < player.getr())
			player.sety(player.getr());
		if (player.getx() > GamePanel.WIDTH - player.getr())
			player.setx(GamePanel.WIDTH - player.getr());
		if (player.gety() > GamePanel.HEIGHT - player.getr())
			player.sety(GamePanel.HEIGHT - player.getr());
		player.update();

		for (int i = 0; i < playerbullets.size(); i++) {
			playerbullets.get(i).update();

			if (playerbullets.get(i).getx() < -playerbullets.get(i).getr()
					|| playerbullets.get(i).getx() > GamePanel.WIDTH + playerbullets.get(i).getr()
					|| playerbullets.get(i).gety() < -playerbullets.get(i).getr()
					|| playerbullets.get(i).gety() > GamePanel.HEIGHT + playerbullets.get(i).getr()) {
				playerbullets.remove(i);
				i--;
			}
		}

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}

		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemybullets.size(); j++) {
				enemybullets.get(j).update();

				if (enemybullets.get(j).getx() < -enemybullets.get(j).getr()
						|| enemybullets.get(j).getx() > GamePanel.WIDTH + enemybullets.get(j).getr()
						|| enemybullets.get(j).gety() < -enemybullets.get(j).getr()
						|| enemybullets.get(j).gety() > GamePanel.HEIGHT + enemybullets.get(j).getr()) {
					enemybullets.remove(j);
					j--;
				}
			}
		}

		for (int i = 0; i < powers.size(); i++) {
			powers.get(i).update();

			if (powers.get(i).gety() > GamePanel.HEIGHT) {
				powers.remove(i);
				i--;
			}
		}

		collision();// 충돌처리
		checkDead();// 적이 죽었는지 판ㄷ

	}

	private void collision() {

		double bx;// bullet의 좌표
		double by;
		double br;

		double px;// player의 좌표
		double py;
		double pr;

		double ex;// enemy의 좌표
		double ey;
		double er;

		double x;// power의 좌표
		double y;
		double r;

		double subx;// 뺄때 사용
		double suby;

		double distance;// 두 entity사이의 거리

		for (int i = 0; i < playerbullets.size(); i++) {// player가 쏜 총알과 적이 충돌했을
			// 경우.

			PlayerBullet b = playerbullets.get(i);
			bx = b.getx();
			by = b.gety();
			br = b.getr();

			for (int j = 0; j < enemies.size(); j++) {

				Enemy e = enemies.get(j);
				ex = e.getx();
				ey = e.gety();
				er = e.getr();
				subx = bx - ex;
				suby = by - ey;
				distance = Math.sqrt(subx * subx + suby * suby);

				if (distance < br + er) {
					Sound explo = new Sound("explo.wav", false);
					e.hit();
					playerbullets.remove(i);
					i--;
					break;
				}

			}
		}

		if (!player.isRecovering()) {// 적의 총알과 player가 충돌했을 경우.
			for (int i = 0; i < enemybullets.size(); i++) {

				EnemyBullet b = enemybullets.get(i);
				bx = b.getx();
				by = b.gety();
				br = b.getr();
				px = player.getx();
				py = player.gety();
				pr = player.getr();
				subx = bx - px;
				suby = by - py;
				distance = Math.sqrt(subx * subx + suby * suby);

				if (distance < br + pr) {
					player.losePower();
					enemybullets.remove(i);
					i--;
					break;
				}
			}
		}

		if (!player.isRecovering()) {// player와 적이, 총알과 충돌할 때.
			px = player.getx();
			py = player.gety();
			pr = player.getr();
			for (int i = 0; i < enemies.size(); i++) {

				Enemy e = enemies.get(i);
				ex = e.getx();
				ey = e.gety();
				er = e.getr();
				subx = px - ex;
				suby = py - ey;
				distance = Math.sqrt(subx * subx + suby * suby);

				if (distance < pr + er) {
					player.loseLife();
				}
			}
		}

		px = player.getx();
		py = player.gety();
		pr = player.getr();

		for (int i = 0; i < powers.size(); i++) {// player와 power가 충돌할때

			Power p = powers.get(i);
			x = p.getx();
			y = p.gety();
			r = p.getr();
			subx = px - x;
			suby = py - y;
			distance = Math.sqrt(subx * subx + suby * suby);

			if (distance < pr + r) {

				int type = p.getType();

				if (type == 1) {// player의 life가 증가한다.
					player.gainLife();
				}
				if (type == 2) {// power가 1증가한다.
					player.increasePower(1);
				}
				if (type == 3) {// power가 2증가한다.
					player.increasePower(2);
				}

				powers.remove(i);
				i--;
			}
		}
	}

	private void checkDead() {
		if (player.isDead()) {// player가 죽었는지 확인
			_running = false;
		}

		for (int i = 0; i < enemies.size(); i++) {// 적이 죽었는지 확인한다.
			if (enemies.get(i).isDead()) {

				Enemy e = enemies.get(i);

				double rand = Math.random();// 랜덤을 만들어서 확률적으로 item이 나오게 한다.
				if (rand <= 0.005)
					powers.add(new Power(1, e.getx(), e.gety()));
				else if (rand < 0.120)
					powers.add(new Power(2, e.getx(), e.gety()));
				else if (rand < 0.200)
					powers.add(new Power(3, e.getx(), e.gety()));

				player.addScore(e.getType());
				enemies.remove(i);
				i--;
			}
		}
	}

	// ***********************************************<Game_Print>***********************************************//

	private void gamePrint() {

		_g.setColor(new Color(0, 0, 0));
		_g.fillRect(0, 0, WIDTH, HEIGHT);
		if (gameTimer != 0) {

			String s1 = null;
			String s2 = null;
			int length;
			_g.setColor(new Color(0, 0, 35));
			_g.fillRect(0, 0, WIDTH, HEIGHT);
			_g.setColor(Color.WHITE);
			_g.setFont(new Font("Century Gothic", Font.PLAIN, 30));
			if (stage == 1) {
				s1 = "stage 1";
				s2 = "level 1 enemies : 4";
			} else if (stage == 2) {
				s1 = "stage 2";
				s2 = "level 1, 2 enemies : 4, 4";
			} else if (stage == 3) {
				s1 = "stage 3";
				s2 = "level 1, 2 enemies : 5, 5";
			} else if (stage == 4) {
				s1 = "stage 4";
				s2 = "level 1, 2, 3 enemies : 4, 4, 1";
			} else if (stage == 5) {
				s1 = "stage 5";
				s2 = "The Last Stage : BOSS!!!";
			}
			length = (int) _g.getFontMetrics().getStringBounds(s1, _g).getWidth();
			_g.drawString(s1, (WIDTH - length) / 2, (HEIGHT) / 2);
			length = (int) _g.getFontMetrics().getStringBounds(s2, _g).getWidth();
			_g.drawString(s2, (WIDTH - length) / 2, (HEIGHT) / 2 + 30);

			int alpha = (int) (255 * Math.sin(3.14 * gameTimerDifference / stageDelay));
			if (alpha > 255)
				alpha = 255;
		}

		// player의 목숨을 그려준다.
		for (int i = 0; i < player.getLives(); i++) {
			_g.setColor(Color.WHITE);
			_g.fillOval(20 + (20 * i), 450, player.getr() * 2, player.getr() * 2);
			_g.setStroke(new BasicStroke(3));
			_g.setColor(Color.WHITE.darker());
			_g.drawOval(20 + (20 * i), 450, player.getr() * 2, player.getr() * 2);
			_g.setStroke(new BasicStroke(1));
		}

		// player의 power를 그려준다.
		_g.setColor(Color.YELLOW);
		_g.fillRect(20, 470, player.getPower() * 8, 8);
		_g.setColor(Color.YELLOW.darker());
		_g.setStroke(new BasicStroke(2));
		for (int i = 0; i < player.getRequiredPower(); i++) {
			_g.drawRect(20 + 8 * i, 470, 8, 8);
		}
		_g.setStroke(new BasicStroke(1));

		player.draw(_g);
		for (int i = 0; i < playerbullets.size(); i++) {
			playerbullets.get(i).draw(_g);
		}
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(_g);
		}
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemybullets.size(); j++) {
				enemybullets.get(j).draw(_g);
			}
		}
		for (int i = 0; i < powers.size(); i++) {
			powers.get(i).draw(_g);
		}

		_g.setColor(Color.WHITE);
		_g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		_g.drawString("Score: " + player.getScore(), WIDTH - 110, 470);

	}

	// ***********************************************<Key_Listener>***********************************************//

	@Override
	public void keyPressed(KeyEvent key) {

		switch (key.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setUp(true);
			break;
		case KeyEvent.VK_DOWN:
			player.setDown(true);
			break;
		case KeyEvent.VK_LEFT:
			player.setLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRight(true);
			break;
		case KeyEvent.VK_Z:
			player.setFiring(true);
			Sound fire = new Sound("fire.wav", false);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setUp(false);
			break;
		case KeyEvent.VK_DOWN:
			player.setDown(false);
			break;
		case KeyEvent.VK_LEFT:
			player.setLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRight(false);
			break;
		case KeyEvent.VK_Z:
			player.setFiring(false);

			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
