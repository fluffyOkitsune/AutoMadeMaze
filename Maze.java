package maze;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Maze extends Applet implements Runnable, KeyListener {
	Thread thread = null;
	Dimension size;
	Image back;
	Graphics buffer;

	int block[][];
	boolean track[][];
	int dx[] = { 0, 1, 0, -1 };
	int dy[] = { 1, 0, -1, 0 };

	final int mapSize = 37;

	int X;
	int Y;

	int goalX;
	int goalY;

	public void init() {
		size = getSize();
		back = createImage(size.width, size.height);
		buffer = back.getGraphics();

		block = new int[mapSize][mapSize];
		track = new boolean[mapSize][mapSize];

		makeMaze();

		while (true) {
			X = (int) (mapSize * Math.random());
			Y = (int) (mapSize * Math.random());
			if (block[X][Y] == 0)
				break;
		}
		while (true) {
			goalX = (int) (mapSize * Math.random());
			goalY = (int) (mapSize * Math.random());
			if (block[goalX][goalY] == 0)
				break;
		}
		track[X][Y] = true;

		thread = new Thread(this);
		thread.start();

		addKeyListener(this);
	}

	private void makeMaze() {
		/* 全体をクリア */
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				block[i][j] = 0;
			}
		}

		/* 外枠をセット */
		for (int i = 0; i < mapSize; i++) {
			block[0][i] = 1;
			block[mapSize - 1][i] = 1;
			block[i][0] = 1;
			block[i][mapSize - 1] = 1;
		}

		/* 基準点をセット */
		for (int i = 1; i < mapSize / 2; i++) {
			for (int j = 1; j < mapSize / 2; j++) {
				block[i * 2][j * 2] = 1;
			}
		}

		/* 迷路作成 */
		for (int i = 1; i < mapSize / 2; i++) {
			for (int j = 1; j < mapSize / 2; j++) {
				if (i == 1) {
					int d = (int) (Math.random() * 4);
					block[i * 2 + dx[d]][j * 2 + dy[d]] = 1;
				} else {
					boolean flag = true;
					while (flag) {
						int d = (int) (Math.random() * 3);
						if (block[i * 2 + dx[d]][j * 2 + dy[d]] == 0) {
							block[i * 2 + dx[d]][j * 2 + dy[d]] = 1;
							flag = false;
						}
					}
				}
			}
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		buffer.setColor(Color.black);
		buffer.fillRect(0, 0, size.width, size.height);

		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if (block[i][j] == 1) {
					buffer.setColor(Color.white);
					buffer.fillRect(i * 15, j * 15, 15, 15);

					buffer.setColor(Color.blue);
					buffer.drawRect(i * 15 + 1, j * 15 + 1, 13, 13);
				}
				if (track[i][j]) {
					buffer.setColor(Color.gray);
					buffer.fillRect(i * 15 + 1, j * 15 + 1, 14, 14);
				}
			}
		}

		buffer.setColor(Color.red);
		buffer.fillRect(X * 15 + 1, Y * 15 + 1, 13, 13);

		buffer.setColor(Color.blue);
		buffer.fillRect(15 * goalX, 15 * goalY, 13, 13);

		g.drawImage(back, 0, 0, this);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (block[X][Y - 1] == 0) {
				Y--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (block[X][Y + 1] == 0) {
				Y++;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (block[X - 1][Y] == 0) {
				X--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (block[X + 1][Y] == 0) {
				X++;
			}
		}

		track[X][Y] = true;
		repaint();

		/* ゴール */
		if (X == goalX && Y == goalY) {
			makeMaze();
			for (int i = 0; i < mapSize; i++) {
				for (int j = 0; j < mapSize; j++) {
					track[i][j] = false;
				}
			}
			while (true) {
				X = (int) (mapSize * Math.random());
				Y = (int) (mapSize * Math.random());
				if (block[X][Y] == 0)
					break;
			}
			while (true) {
				goalX = (int) (mapSize * Math.random());
				goalY = (int) (mapSize * Math.random());
				if (block[goalX][goalY] == 0)
					break;
			}
			track[X][Y] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}
}